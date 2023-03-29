package com.kravchenko.apps.gooddeed.repository;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.database.AppDatabase;
import com.kravchenko.apps.gooddeed.database.entity.Initiative;
import com.kravchenko.apps.gooddeed.util.Resource;
import com.kravchenko.apps.gooddeed.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kravchenko.apps.gooddeed.repository.CategoryRepository.databaseWriteExecutor;

public class InitiativeRepository {

    private static final String COLLECTION_INITIATIVES = "initiatives";
    private static final String FIELD_INITIATIVE_ID = "initiativeId";
    private static final String FIELD_IMAGE_URI = "imgUri";
    private final FirebaseFirestore mFirestore;
    private final FirebaseStorage mStorage;
    private final FirebaseAuth mAuth;
    private final MutableLiveData<Resource<Initiative>> initiativeSave;
    private MutableLiveData<Resource<List<Initiative>>> firestoreInitiatives;
    private MutableLiveData<Resource<List<Initiative>>> savedInitiatives;

    public InitiativeRepository() {
        initiativeSave = new MutableLiveData<>();
        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
    }

    public LiveData<Resource<Initiative>> savingInitiative() {
        return initiativeSave;
    }

    public void saveInitiative(Initiative initiative) {
        //save initiative
        StorageReference initiativesFolderRef = mStorage.getReference()
                .child(COLLECTION_INITIATIVES);

        mFirestore.collection(COLLECTION_INITIATIVES)
                .add(initiative)
                .addOnSuccessListener(reference -> {
                    String newInitiativeId = reference.getId();
                    reference.update(FIELD_INITIATIVE_ID, newInitiativeId)
                            .addOnSuccessListener(unused -> {
                                DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(
                                        FirebaseAuth.getInstance().getCurrentUser().getUid());
                                userRef.get().addOnSuccessListener(userSnapshot -> {
                                    ArrayList<String> initiativesCreated = new ArrayList<>();
                                    Map<String, Object> data = new HashMap<>();
                                    if (userSnapshot.get("initiativesCreated") != null) {
                                        initiativesCreated = (ArrayList<String>) userSnapshot.get("initiativesCreated");
                                    }
                                    initiativesCreated.add(newInitiativeId);
                                    data.put("initiativesCreated", initiativesCreated);
                                    userRef.update(data);
                                });

                                // uploading picture to firebase storage
                                if (initiative.getImgUri() != null) {
                                    StorageReference initiativePicRef = initiativesFolderRef.child(reference.getId())
                                            .child(Uri.parse(initiative.getImgUri()).getLastPathSegment());
                                    UploadTask uploadTask = initiativePicRef.putFile(Uri.parse(initiative.getImgUri()));
                                    uploadTask.continueWithTask(task -> {
                                        if (!task.isSuccessful() && task.getException() != null) {
                                            throw task.getException();
                                        }
                                        return initiativePicRef.getDownloadUrl();
                                    }).addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            String imageUri = task.getResult().toString();
                                            initiative.setImgUri(imageUri);
                                            // update image uri in Firestore
                                            reference.update(FIELD_IMAGE_URI, imageUri)
                                                    .addOnSuccessListener(unused1 -> initiativeSave.setValue(Resource.success(null)))
                                                    .addOnFailureListener(e -> initiativeSave.setValue(Resource.error(e.getMessage(), null)));
                                        }
                                    });
                                } else {
                                    initiativeSave.setValue(Resource.success(null));
                                }
                            })
                            .addOnFailureListener(e -> initiativeSave.setValue(Resource.error(e.getMessage(), null)));
                })
                .addOnFailureListener(e -> initiativeSave.setValue(Resource.error(e.getMessage(), null)));
    }

    private void loadInitiativesFromFirestore() {
        firestoreInitiatives.setValue(Resource.loading(Utils.getString(R.string.loading), null));
        mFirestore.collection(COLLECTION_INITIATIVES)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Initiative> initiativeList = task.getResult().toObjects(Initiative.class);
                        databaseWriteExecutor.execute(() ->
                                AppDatabase.getInstance().initiativeDao().insertAllInitiatives(initiativeList));
                        firestoreInitiatives.setValue(Resource.success(initiativeList));
                    } else {
                        firestoreInitiatives.setValue(Resource.error(task.getException().getMessage(), null));
                    }
                });
    }

    private void loadInitiativesFromLocalDb() {
        savedInitiatives.setValue(Resource.loading(Utils.getString(R.string.loading), null));
        databaseWriteExecutor.execute(() ->
                savedInitiatives.postValue(Resource.success(AppDatabase.getInstance().initiativeDao().getAllInitiatives())));
    }

    public void clearInitiativesInDatabase() {
        databaseWriteExecutor.execute(() ->
                AppDatabase.getInstance().initiativeDao().clearInitiativesTable());
    }

    public LiveData<Resource<List<Initiative>>> getSavedInitiativesLiveData() {
        if (savedInitiatives == null) {
            savedInitiatives = new MutableLiveData<>();
            loadInitiativesFromLocalDb();
        }
        return savedInitiatives;
    }

    public LiveData<Resource<List<Initiative>>> getInitiativesFromFirestoreLiveData() {
        if (firestoreInitiatives == null) {
            firestoreInitiatives = new MutableLiveData<>();
            loadInitiativesFromFirestore();
        }
        return firestoreInitiatives;
    }

    public FirebaseUser getAuthUser() {
        return mAuth.getCurrentUser();
    }
}

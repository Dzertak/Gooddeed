package com.kravchenko.apps.gooddeed.repository;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.database.entity.Initiative;
import com.kravchenko.apps.gooddeed.util.Resource;

public class InitiativeRepository {

    private static final String COLLECTION_INITIATIVES = "initiatives";
    private static final String FIELD_INITIATIVE_ID = "initiativeId";
    private static final String FIELD_IMAGE_URI = "imgUri";
    private final FirebaseFirestore mFirestore;
    private final FirebaseStorage mStorage;
    private final FirebaseAuth mAuth;
    private final MutableLiveData<Resource<Initiative>> initiativeSave;

    public InitiativeRepository() {
        initiativeSave = new MutableLiveData<>();
        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
    }

    public MutableLiveData<Resource<Initiative>> savingInitiative() {
        return initiativeSave;
    }

    public void saveInitiative(Initiative initiative) {
        initiativeSave.setValue(Resource.loading(R.string.loading, null));
        //save initiative
        StorageReference initiativesFolderRef = mStorage.getReference()
                .child(COLLECTION_INITIATIVES);

        mFirestore.collection(COLLECTION_INITIATIVES)
                .add(initiative)
                .addOnSuccessListener(reference -> reference.update(FIELD_INITIATIVE_ID, reference.getId())
                        .addOnSuccessListener(unused -> {
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
                        .addOnFailureListener(e -> initiativeSave.setValue(Resource.error(e.getMessage(), null))))
                .addOnFailureListener(e -> initiativeSave.setValue(Resource.error(e.getMessage(), null)));
    }

    public FirebaseUser getAuthUser() {
        return mAuth.getCurrentUser();
    }
}

package com.kravchenko.apps.gooddeed.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kravchenko.apps.gooddeed.database.entity.FirestoreUser;
import com.kravchenko.apps.gooddeed.util.Resource;

import static com.kravchenko.apps.gooddeed.repository.AuthRepository.COLLECTION_USERS;

public class ProfileRepository {

    private final FirebaseAuth mAuth;
    private final FirebaseFirestore mFirestore;
    private MutableLiveData<Resource<FirestoreUser>> mUser;

    public ProfileRepository() {
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
    }

    public LiveData<Resource<FirestoreUser>> getUser() {
        if (mUser == null) {
            mUser = new MutableLiveData<>();
            getUserData();
        }
        return mUser;
    }

    public void getUserData() {
        if (mAuth.getCurrentUser() != null) {
            DocumentReference userDocRef = mFirestore
                    .collection(COLLECTION_USERS)
                    .document(mAuth.getCurrentUser().getUid());
            userDocRef.get().addOnCompleteListener(snapshotTask -> {
                if (snapshotTask.isSuccessful()) {
                    mUser.setValue(Resource.success(snapshotTask.getResult().toObject(FirestoreUser.class)));
                } else {
                    mUser.setValue(Resource.error("Get doc failed: " + snapshotTask.getException(), null));
                }
            });
        }
    }
}

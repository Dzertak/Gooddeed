package com.kravchenko.apps.gooddeed.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.database.entity.FirestoreUser;
import com.kravchenko.apps.gooddeed.util.Resource;
import com.kravchenko.apps.gooddeed.util.Utils;

import java.util.HashMap;
import java.util.Map;

import static com.kravchenko.apps.gooddeed.repository.AuthRepository.COLLECTION_USERS;

public class ProfileRepository {

    private final String FIELD_FIRSTNAME = "firstName";
    private final String FIELD_LASTNAME = "lastName";
    private final String FIELD_IMAGEURL = "imageUrl";
    private final String FIELD_EMAIL = "email";
    private final String FIELD_DESCRIPTION = "description";
    private final FirebaseAuth mAuth;
    private final FirebaseFirestore mFirestore;
    private DocumentReference mUserDocRef;
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
            mUserDocRef = mFirestore
                    .collection(COLLECTION_USERS)
                    .document(mAuth.getCurrentUser().getUid());
            mUserDocRef.get().addOnCompleteListener(snapshotTask -> {
                if (snapshotTask.isSuccessful()) {
                    mUser.setValue(Resource.success(snapshotTask.getResult().toObject(FirestoreUser.class)));
                } else {
                    mUser.setValue(Resource.error(snapshotTask.getException().getMessage(), null));
                }
            });
        }
    }

    public void updateUser(String firstName, String lastName, String imageUrl, String email, String description) {
        // Updating only a few fields
        Map<String, Object> userMap = new HashMap<>();
        userMap.put(FIELD_FIRSTNAME, firstName);
        userMap.put(FIELD_LASTNAME, lastName);
        userMap.put(FIELD_IMAGEURL, imageUrl);
        userMap.put(FIELD_EMAIL, email);
        userMap.put(FIELD_DESCRIPTION, description);
        FirestoreUser user = mUser.getValue().data;
        mUser.setValue(Resource.loading(Utils.getString(R.string.loading), null));
        mUserDocRef.update(userMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setImageUrl(imageUrl);
                user.setEmail(email);
                user.setDescription(description);
                mUser.setValue(Resource.success(user));
            } else {
                // Error updating document
                mUser.setValue(Resource.error(task.getException().getMessage(), null));
            }
        });
    }
}

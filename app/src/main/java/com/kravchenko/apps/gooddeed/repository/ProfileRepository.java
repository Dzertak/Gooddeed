package com.kravchenko.apps.gooddeed.repository;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.database.entity.FirestoreUser;
import com.kravchenko.apps.gooddeed.util.Resource;
import com.kravchenko.apps.gooddeed.util.Utils;

import java.util.HashMap;
import java.util.Map;

import static com.kravchenko.apps.gooddeed.repository.AuthRepository.COLLECTION_USERS;

public class ProfileRepository {

    private static final String USER_PATH = "users";
    private static final String IMAGES_PATH = "images";
    private final FirebaseAuth mAuth;
    private final FirebaseFirestore mFirestore;
    private final FirebaseStorage mStorage;
    private DocumentReference mUserDocRef;
    private MutableLiveData<Resource<FirestoreUser>> mUser;

    public ProfileRepository() {
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance();
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

    public void updateUser(String firstName, String lastName, Uri imageUri, String description) {
        if (mUser.getValue() != null && mUser.getValue().data != null) {
            FirestoreUser user = mUser.getValue().data;
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setDescription(description);

            StorageReference profilePicRef = mStorage.getReference()
                    .child(USER_PATH)
                    .child(mAuth.getCurrentUser().getUid())
                    .child(IMAGES_PATH)
                    .child(imageUri.getLastPathSegment());
            UploadTask uploadTask = profilePicRef.putFile(imageUri);

            uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful() && task.getException() != null) {
                    throw task.getException();
                }
                // Continue with the task to get the download URL
                return profilePicRef.getDownloadUrl();

            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    user.setImageUrl(task.getResult().toString());
                }
                updateUserInFirestore(user);
            });
        }
    }

    private void updateUserInFirestore(FirestoreUser user) {
        String FIELD_FIRSTNAME = "firstName";
        String FIELD_LASTNAME = "lastName";
        String FIELD_IMAGEURL = "imageUrl";
        String FIELD_DESCRIPTION = "description";

        // Updating only some fields
        Map<String, Object> userMap = new HashMap<>();
        userMap.put(FIELD_FIRSTNAME, user.getFirstName());
        userMap.put(FIELD_LASTNAME, user.getLastName());
        userMap.put(FIELD_IMAGEURL, user.getImageUrl());
        userMap.put(FIELD_DESCRIPTION, user.getDescription());
        mUser.setValue(Resource.loading(Utils.getString(R.string.loading), null));
        mUserDocRef.update(userMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                mUser.setValue(Resource.success(user));
            } else {
                mUser.setValue(Resource.error(task.getException().getMessage(), null));
            }
        });
    }
}

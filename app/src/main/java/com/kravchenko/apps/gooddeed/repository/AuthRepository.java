package com.kravchenko.apps.gooddeed.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kravchenko.apps.gooddeed.AppInstance;
import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.database.entity.FirestoreUser;
import com.kravchenko.apps.gooddeed.util.Resource;
import com.kravchenko.apps.gooddeed.util.Utils;

public class AuthRepository {

    public static final String COLLECTION_USERS = "users";
    public static final String FLAG_NEW_LOGIN = "com.kravchenko.apps.gooddeed.NEW_LOGIN";
    private final FirebaseAuth mAuth;
    private final FirebaseFirestore mFirestore;
    private final GoogleSignInClient mGoogleSignIn;
    private final MutableLiveData<Resource<FirebaseUser>> mUser;
    private final MutableLiveData<Resource<Object>> actionMarker;

    public AuthRepository() {
        Context context = AppInstance.getAppContext();
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mUser = new MutableLiveData<>();
        actionMarker = new MutableLiveData<>();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))  // OAuth 2.0 web client ID
                .requestEmail()
                .build();
        mGoogleSignIn = GoogleSignIn.getClient(context, gso);

        if (mAuth.getCurrentUser() != null) {
            mUser.setValue(Resource.success(mAuth.getCurrentUser()));
        } else if (GoogleSignIn.getLastSignedInAccount(context) != null) {
            firebaseAuthWithGoogle(GoogleSignIn.getLastSignedInAccount(context).getIdToken());
        }
    }

    public void loginWithEmailAndPassword(String email, String password) {
        isEmailRegistered(email, isRegistered -> {
            mUser.setValue(Resource.loading(Utils.getString(R.string.loading), null));
            if (isRegistered) {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Login successful
                                mUser.setValue(Resource.success(mAuth.getCurrentUser(), FLAG_NEW_LOGIN));
                            } else {
                                mUser.setValue(Resource.error(task.getException().getMessage(), null));
                            }
                        });
            } else {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                mUser.setValue(Resource.success(mAuth.getCurrentUser(), FLAG_NEW_LOGIN));
                                addUserToFirebase(new FirestoreUser(
                                        mAuth.getCurrentUser().getUid(),
                                        null,
                                        null,
                                        mAuth.getCurrentUser().getEmail(),
                                        "5.0",
                                        null,
                                        null, null, null));
                            } else {
                                mUser.setValue(Resource.error(task.getException().getMessage(), null));
                            }
                        });
            }
        });
    }

    public void firebaseAuthWithGoogle(String idToken) {
        mUser.setValue(Resource.loading(Utils.getString(R.string.loading), null));
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(authResultTask -> {
                    if (authResultTask.isSuccessful() && mAuth.getCurrentUser() != null) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        mUser.setValue(Resource.success(user, FLAG_NEW_LOGIN));
                        String lastName = "";
                        if (user.getDisplayName() != null && user.getDisplayName().contains(" ")) {
                            String displayName = user.getDisplayName();
                            lastName = displayName.substring(displayName.indexOf(" ")).trim();
                        }

                        addUserToFirebase(new FirestoreUser(
                                user.getUid(),
                                user.getDisplayName().split("\\s+")[0],
                                lastName,
                                user.getEmail(),
                                "5.0",
                                null,
                                user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : null,
                                null, null));
                    } else {
                        mUser.setValue(Resource.error(authResultTask.getException().getMessage(), null));
                    }
                });
    }

    public GoogleSignInClient loginWithGoogle() {
        return mGoogleSignIn;
    }

    // Checks if user exists in Firebase
    // Adds user document if user doesn't exist in Firebase.
    private void addUserToFirebase(FirestoreUser user) {
        DocumentReference userDocRef = mFirestore.collection(COLLECTION_USERS).document(user.getUserId());
        userDocRef.get()
                .addOnCompleteListener(snapshotTask -> {
                    if (snapshotTask.isSuccessful()) {
                        if (!snapshotTask.getResult().exists()) {
                            userDocRef.set(user);
                        }
                    }
                });
    }

    private void isEmailRegistered(String email, OnEmailCheckListener listener) {
        mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(task ->
                listener.onResult(!task.getResult().getSignInMethods().isEmpty()));
    }

    public LiveData<Resource<FirebaseUser>> getUser() {
        return mUser;
    }

    public void changePassword(String newPassword) {
        actionMarker.setValue(Resource.loading("Loading...", null));
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            firebaseUser.updatePassword(newPassword)
                    .addOnSuccessListener(aVoid -> {
                        actionMarker.setValue(Resource.success(null));
                        actionMarker.setValue(Resource.inactive());
                    })
                    .addOnFailureListener(e -> {
                                actionMarker.setValue(Resource.error(e.getLocalizedMessage(), null));
                                actionMarker.setValue(Resource.inactive());
                            }
                    );
        } else {
            actionMarker.setValue(Resource.error(AppInstance.getAppContext().getString(R.string.default_error_msg), null));
            actionMarker.setValue(Resource.inactive());
        }
    }

    public void changeEmail(String newEmail) {
        actionMarker.setValue(Resource.loading("Loading...", null));
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            firebaseUser.updateEmail(newEmail)
                    .addOnSuccessListener(aVoid -> {
                                actionMarker.setValue(Resource.success(null));
                                actionMarker.setValue(Resource.inactive());
                            }
                    )
                    .addOnFailureListener(e -> {
                                e.printStackTrace();
                                actionMarker.setValue(Resource.error(e.getLocalizedMessage(), null));
                                actionMarker.setValue(Resource.inactive());
                            }
                    );
        } else {
            actionMarker.setValue(Resource.error(AppInstance.getAppContext().getString(R.string.default_error_msg), null));
        }
    }

    public MutableLiveData<Resource<Object>> getActionMarker() {
        return actionMarker;
    }

    public void loginWithPassword(String password) {
        actionMarker.setValue(Resource.loading(Utils.getString(R.string.loading), null));
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            actionMarker.setValue(Resource.success(null));
                        } else {
                            mUser.setValue(Resource.error(task.getException().getMessage(), null));
                        }
                        actionMarker.setValue(Resource.inactive());
                    });
        } else {
            actionMarker.setValue(Resource.error(Utils.getString(R.string.loading), null));
            actionMarker.setValue(Resource.inactive());
        }

    }

    private interface OnEmailCheckListener {
        void onResult(boolean isRegistered);
    }

}

package com.kravchenko.apps.gooddeed.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class AuthRepository {

    public static final String COLLECTION_USERS = "users";
    private final String TAG = "TAG_DEBUG_" + getClass().getSimpleName();
    private final FirebaseAuth mAuth;
    private final FirebaseFirestore mFirestore;
    private final GoogleSignInClient mGoogleSignIn;
    private final MutableLiveData<Resource<FirebaseUser>> mUser;

    public AuthRepository() {
        Context context = AppInstance.getAppContext();
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mUser = new MutableLiveData<>();
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
            mUser.setValue(Resource.loading("Loading...", null));
            if (isRegistered) {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Login successful
                                mUser.setValue(Resource.success(mAuth.getCurrentUser()));
                                addUserToFirebase(new FirestoreUser(
                                        mAuth.getCurrentUser().getUid(),
                                        null,
                                        null,
                                        mAuth.getCurrentUser().getEmail(),
                                        "5.0",
                                        null,
                                        null,null));
                                Log.d(TAG, "User logged in successfully");
                            } else {
                                mUser.setValue(Resource.error(task.getException().getMessage(), null));
                                Log.w(TAG, "Sign in failure: " + task.getException());
                            }
                        });
            } else {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "New user created successfully");
                                mUser.setValue(Resource.success(mAuth.getCurrentUser()));
                                addUserToFirebase(new FirestoreUser(
                                        mAuth.getCurrentUser().getUid(),
                                        null,
                                        null,
                                        mAuth.getCurrentUser().getEmail(),
                                        "5.0",
                                        null,
                                        null,null));
                            } else {
                                mUser.setValue(Resource.error(task.getException().getMessage(), null));
                                Log.w(TAG, "Registration failure: " + task.getException());
                            }
                        });
            }
        });
    }

    public void firebaseAuthWithGoogle(String idToken) {
        mUser.setValue(Resource.loading("Loading...", null));
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(authResultTask -> {
                    if (authResultTask.isSuccessful()) {
                        Log.d(TAG, "Sign in with credential: success");
                        mUser.setValue(Resource.success(mAuth.getCurrentUser()));
                        addUserToFirebase(new FirestoreUser(
                                mAuth.getCurrentUser().getUid(),
                                mAuth.getCurrentUser().getDisplayName().split("\\s+")[0],
                                mAuth.getCurrentUser().getDisplayName().split("\\s+")[1],
                                mAuth.getCurrentUser().getEmail(),
                                "5.0",
                                null,
                                mAuth.getCurrentUser().getPhotoUrl().toString(),
                                null));
                    } else {
                        mUser.setValue(Resource.error(authResultTask.getException().getMessage(), null));
                        Log.w(TAG, "Sign in with credential: failure" + authResultTask.getException());
                    }
                });
    }

    public GoogleSignInClient loginWithGoogle() {
        return mGoogleSignIn;
    }

    public void signOutUser() {
        if (mAuth.getCurrentUser() != null) {
            mAuth.signOut();
            mGoogleSignIn.signOut();
            mUser.setValue(Resource.inactive());
            Log.d(TAG, "User logged out");
        }
    }

    // Checks if user exists in Firebase
    // Adds user document if user doesn't exist in Firebase.
    private void addUserToFirebase(FirestoreUser user) {
        DocumentReference userDocRef = mFirestore.collection(COLLECTION_USERS).document(user.getUserId());
        userDocRef.get()
                .addOnCompleteListener(snapshotTask -> {
                    if (snapshotTask.isSuccessful()) {
                        if (snapshotTask.getResult().exists()) {
                            // User exists in Firebase
                            // mUser.setValue(Resource.success(snapshotTask.getResult().toObject(User.class)));
                            Log.d(TAG, "DocumentSnapshot data: " + snapshotTask.getResult().getData());
                        } else {
                            userDocRef.set(user).addOnCompleteListener(setUserTask -> {
                                if (setUserTask.isSuccessful()) {
                                    Log.d(TAG, "Document added successfully");
                                } else {
                                    Log.w(TAG, "Error adding document" + setUserTask.getException());
                                }
                            });
                        }
                    } else {
                        Log.d(TAG, "Get doc failed with " + snapshotTask.getException());
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

    private interface OnEmailCheckListener {
        void onResult(boolean isRegistered);
    }
}

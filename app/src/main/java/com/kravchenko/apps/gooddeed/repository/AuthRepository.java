package com.kravchenko.apps.gooddeed.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.kravchenko.apps.gooddeed.R;

public class AuthRepository {

    private final String TAG = "TAG_DEBUG_" + getClass().getSimpleName();
    private final FirebaseAuth mAuth;
    private final GoogleSignInClient mGoogleSignIn;
    private final MutableLiveData<FirebaseUser> mUser;

    public AuthRepository(Context context) {
        mAuth = FirebaseAuth.getInstance();
        mUser = new MutableLiveData<>();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))  // OAuth 2.0 web client ID
                .requestEmail()
                .build();
        mGoogleSignIn = GoogleSignIn.getClient(context, gso);

        if (mAuth.getCurrentUser() != null) {
            mUser.setValue(mAuth.getCurrentUser());
        } else if (GoogleSignIn.getLastSignedInAccount(context) != null) {
            firebaseAuthWithGoogle(GoogleSignIn.getLastSignedInAccount(context).getIdToken());
        }
    }

    public void loginWithEmailAndPassword(String email, String password) {
        isEmailRegistered(email, isRegistered -> {
            if (isRegistered) {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Login successful
                                mUser.setValue(mAuth.getCurrentUser());
                                Log.d(TAG, "User logged in successfully");
                            } else {
                                mUser.setValue(null);
                                Log.w(TAG, "Sign in failure: " + task.getException());
                            }
                        });
            } else {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "New user created successfully");
                                mUser.setValue(mAuth.getCurrentUser());
                            } else {
                                mUser.setValue(null);
                                Log.w(TAG, "Registration failure: " + task.getException());
                            }
                        });
            }
        });
    }

    public void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Sign in with credential: success");
                        mUser.setValue(mAuth.getCurrentUser());
                    } else {
                        Log.w(TAG, "Sign in with credential: failure" + task.getException());
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
        }
    }

    private void isEmailRegistered(String email, OnEmailCheckListener listener) {
        mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(task ->
                listener.onResult(!task.getResult().getSignInMethods().isEmpty()));
    }

    public LiveData<FirebaseUser> getUser() {
        return mUser;
    }

    private interface OnEmailCheckListener {
        void onResult(boolean isRegistered);
    }
}

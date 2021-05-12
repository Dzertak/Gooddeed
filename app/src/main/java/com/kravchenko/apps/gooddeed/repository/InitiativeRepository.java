package com.kravchenko.apps.gooddeed.repository;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.database.entity.Initiative;
import com.kravchenko.apps.gooddeed.util.Resource;

public class InitiativeRepository {

    private static final String COLLECTION_INITIATIVES = "initiatives";
    private static final String FIELD_INITIATIVE_ID = "initiativeId";
    private final FirebaseFirestore mFirestore;
    private final FirebaseAuth mAuth;
    private MutableLiveData<Resource<Initiative>> initiativeSave;

    public InitiativeRepository() {
        initiativeSave = new MutableLiveData<>();
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
    }

    public MutableLiveData<Resource<Initiative>> savingInitiative() {
        return initiativeSave;
    }

    public void saveInitiative(Initiative initiative) {
        initiativeSave.setValue(Resource.loading(R.string.loading, null));
        //save initiative
        // TODO: set initiativeId from Firestore
        mFirestore.collection(COLLECTION_INITIATIVES)
                .add(initiative)
                .addOnSuccessListener(reference -> reference.update(FIELD_INITIATIVE_ID, reference.getId())
                        .addOnSuccessListener(unused -> initiativeSave.setValue(Resource.success(null)))
                        .addOnFailureListener(e -> initiativeSave.setValue(Resource.error(e.getMessage(), null))))
                .addOnFailureListener(e -> initiativeSave.setValue(Resource.error(e.getMessage(), null)));
    }

    public FirebaseUser getAuthUser() {
        return mAuth.getCurrentUser();
    }
}

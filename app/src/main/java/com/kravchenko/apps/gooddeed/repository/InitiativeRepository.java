package com.kravchenko.apps.gooddeed.repository;

import androidx.lifecycle.MutableLiveData;

import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.database.entity.Initiative;
import com.kravchenko.apps.gooddeed.util.Resource;

public class InitiativeRepository {

    private MutableLiveData<Resource<Initiative>> initiativeSave;

    public InitiativeRepository(){
        initiativeSave = new MutableLiveData<>();
    }

    public MutableLiveData<Resource<Initiative>> savingInitiative(){
        return initiativeSave;
    }

    public void saveInitiative(Initiative initiative){
        initiativeSave.setValue(Resource.loading(R.string.loading, null));
        //save initiative
        initiativeSave.setValue(Resource.success(null));
    }
}

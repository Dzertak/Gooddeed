package com.kravchenko.apps.gooddeed.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kravchenko.apps.gooddeed.database.entity.Initiative;
import com.kravchenko.apps.gooddeed.util.Resource;

public class InitiativeViewModel extends ViewModel {

    private final MutableLiveData<Initiative> initiativeMutableLiveData;

    public InitiativeViewModel() {
        super();
        initiativeMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<Initiative> getInitiative(){
        return initiativeMutableLiveData;
    }

    public void updateInitiative(Initiative initiative){
        initiativeMutableLiveData.setValue(initiative);
    }
}

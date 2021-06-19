package com.kravchenko.apps.gooddeed.util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.firestore.FirebaseFirestore;
import com.kravchenko.apps.gooddeed.database.AppDatabase;
import com.kravchenko.apps.gooddeed.database.entity.Initiative;

import java.util.List;

public class SyncWorker extends Worker {

    public static final String COLLECTION_INITIATIVES = "initiatives";
    private final Context mContext;
    private final AppDatabase mDatabase = AppDatabase.getInstance();
    List<Initiative> firestoreInitiativeList;

    public SyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mContext = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        List<Initiative> savedInitiativeList = mDatabase.initiativeDao().getAllInitiatives();
        FirebaseFirestore.getInstance().collection(COLLECTION_INITIATIVES).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                firestoreInitiativeList = task.getResult().toObjects(Initiative.class);
                for (int i = 0; i < firestoreInitiativeList.size(); i++) {
                    Initiative initiative = firestoreInitiativeList.get(i);
                    if (!savedInitiativeList.contains(initiative)) {
                       mDatabase.initiativeDao().insertInitiative(initiative);
                    }
                }
            }
        });
        return Result.success();
    }
}

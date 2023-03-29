package com.kravchenko.apps.gooddeed.database.dao;

import androidx.room.Dao;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.kravchenko.apps.gooddeed.database.entity.Initiative;

import java.util.List;

@Dao
public abstract class InitiativeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract void insertInitiative(Initiative initiative);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract void insertAllInitiatives(List<Initiative> initiativeList);

    @Query("SELECT * FROM initiative")
    public abstract List<Initiative> getAllInitiatives();

    @Query("DELETE FROM initiative")
    public abstract void clearInitiativesTable();
}

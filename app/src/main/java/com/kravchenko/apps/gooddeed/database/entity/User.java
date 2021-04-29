package com.kravchenko.apps.gooddeed.database.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public class User {
    @PrimaryKey(autoGenerate = true)
    private long userId;
    @Ignore
    private String firebaseUserId;

    private String name;
    private String lastName;
    private String email;
    private int rate;
    private String description;
    private String imgID;


}

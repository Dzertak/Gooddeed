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

    private int rate;
    private String name;
    private String lastName;
    private String email;
    private String imgUri;

    public User(int rate, String name, String lastName, String email, String imgUri) {
        this.rate = rate;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.imgUri = imgUri;
    }

    public long getUserId() {
        return userId;
    }

    public String getFirebaseUserId() {
        return firebaseUserId;
    }

    public int getRate() {
        return rate;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getImgUri() {
        return imgUri;
    }
}

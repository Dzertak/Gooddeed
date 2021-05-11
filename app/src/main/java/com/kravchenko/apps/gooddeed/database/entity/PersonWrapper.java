package com.kravchenko.apps.gooddeed.database.entity;

public class PersonWrapper {
    private String imageUrl;
    private String personName;
    private String personId;

    public PersonWrapper(String imageUrl, String personName, String personId) {
        this.imageUrl = imageUrl;
        this.personName = personName;
        this.personId = personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonId() {
        return personId;
    }
}

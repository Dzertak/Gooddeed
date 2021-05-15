package com.kravchenko.apps.gooddeed;

import com.kravchenko.apps.gooddeed.database.entity.FirestoreUser;
import com.kravchenko.apps.gooddeed.database.entity.category.Category;

import java.util.ArrayList;
import java.util.List;

public class Settings {

    public static volatile CurrentUser CURRENT_USER;

//    public Settings(){
//        CURRENT_USER = new CurrentUser();
//    }

    public static class CurrentUser {
        private String userId;
        private String imageUrl;
        private String email;
        private String firstName;
        private String lastName;
        private String rate;
        private String description;
        private List<Long> subscriptions;
        private List<String> chats;

        public CurrentUser(String currentUser){

        }

        public CurrentUser(FirestoreUser firestoreUser) {
            this.userId = firestoreUser.getUserId();
            this.firstName = firestoreUser.getFirstName();
            this.lastName = firestoreUser.getLastName();
            this.email = firestoreUser.getEmail();
            this.rate = firestoreUser.getRate();
            this.description = firestoreUser.getDescription();
            this.imageUrl = firestoreUser.getImageUrl();
            this.chats = firestoreUser.getChats();
            this.subscriptions = firestoreUser.getSubscriptions();
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public List<Long> getSubscriptions() {
            return subscriptions;
        }

        public void setSubscriptions(List<Long> subscriptions) {
            this.subscriptions = subscriptions;
        }

        public List<String> getChats() {
            return chats;
        }

        public void setChats(List<String> chats) {
            this.chats = chats;
        }
    }
}

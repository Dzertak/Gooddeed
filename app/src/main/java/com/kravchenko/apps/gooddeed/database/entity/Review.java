package com.kravchenko.apps.gooddeed.database.entity;

public class Review {
    private String comment;
    private String initiativeId;
    private double rate;
    private String reviewerId;

    public Review() {
    }

    public String getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(String reviewerId) {
        this.reviewerId = reviewerId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getInitiativeId() {
        return initiativeId;
    }

    public void setInitiativeId(String initiativeId) {
        this.initiativeId = initiativeId;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}

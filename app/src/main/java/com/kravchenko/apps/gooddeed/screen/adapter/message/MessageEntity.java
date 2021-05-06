package com.kravchenko.apps.gooddeed.screen.adapter.message;

public class MessageEntity {
    private String dateAndTime;
    private String sender;
    private String textOfMessage;

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public MessageEntity() {}

    public String getTextOfMessage() {
        return textOfMessage;
    }

    public void setTextOfMessage(String textOfMessage) {
        this.textOfMessage = textOfMessage;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    //TODO File
}
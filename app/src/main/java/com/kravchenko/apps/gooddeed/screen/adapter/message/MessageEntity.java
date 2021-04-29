package com.kravchenko.apps.gooddeed.screen.adapter.message;

public class MessageEntity {
    private long number;
    private String sender;
    private String textOfMessage;

    public String getTextOfMessage() {
        return textOfMessage;
    }

    public void setTextOfMessage(String textOfMessage) {
        this.textOfMessage = textOfMessage;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

//TODO File
}

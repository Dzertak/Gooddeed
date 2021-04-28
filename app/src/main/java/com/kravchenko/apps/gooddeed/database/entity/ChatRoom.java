package com.kravchenko.apps.gooddeed.database.entity;

import com.kravchenko.apps.gooddeed.screen.adapter.message.MessageEntity;

import java.util.ArrayList;

public class ChatRoom {
    private String chatRoomId;
    private String chatRoomName;
    private ArrayList<String> chatRoomMembers;
    private ArrayList<MessageEntity> listOfMessages;

    public ChatRoom(String chatRoomId, String chatRoomName, ArrayList<String> chatRoomMembers, ArrayList<MessageEntity> listOfMessages) {
        this.chatRoomId = chatRoomId;
        this.chatRoomName = chatRoomName;
        this.chatRoomMembers = chatRoomMembers;
        this.listOfMessages = listOfMessages;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public String getChatRoomName() {
        return chatRoomName;
    }

    public void setChatRoomName(String chatRoomName) {
        this.chatRoomName = chatRoomName;
    }

    public ArrayList<String> getChatRoomMembers() {
        return chatRoomMembers;
    }

    public void setChatRoomMembers(ArrayList<String> chatRoomMembers) {
        this.chatRoomMembers = chatRoomMembers;
    }

    public ArrayList<MessageEntity> getListOfMessages() {
        return listOfMessages;
    }

    public void setListOfMessages(ArrayList<MessageEntity> listOfMessages) {
        this.listOfMessages = listOfMessages;
    }
}

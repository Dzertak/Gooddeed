package com.kravchenko.apps.gooddeed.database.entity;

import com.kravchenko.apps.gooddeed.screen.adapter.message.MessageEntity;

import java.util.ArrayList;
import java.util.List;

public class ChatRoom {
    private String chatRoomId;
    private String chatRoomName;
    private ArrayList<String> members;
    private List<MessageEntity> listOfMessages;
    private String imageUrl;

    ChatRoom() {
    }

    public ChatRoom(String chatRoomId, String chatRoomName, ArrayList<String> members, ArrayList<MessageEntity> listOfMessages, String imageUrl) {
        this.chatRoomId = chatRoomId;
        this.chatRoomName = chatRoomName;
        this.members = members;
        this.listOfMessages = listOfMessages;
        this.imageUrl = imageUrl;
    }

    public MessageEntity getLastMessage() {
        if (listOfMessages.size()!=0)
        return listOfMessages.get(listOfMessages.size() - 1);
        else return null;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public ArrayList<String> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }

    public List<MessageEntity> getListOfMessages() {
        return listOfMessages;
    }

    public void setListOfMessages(ArrayList<MessageEntity> listOfMessages) {
        this.listOfMessages = listOfMessages;
    }
}

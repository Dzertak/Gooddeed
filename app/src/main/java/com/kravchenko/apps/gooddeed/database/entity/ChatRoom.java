package com.kravchenko.apps.gooddeed.database.entity;

import com.kravchenko.apps.gooddeed.screen.adapter.message.MessageEntity;

import java.util.ArrayList;
import java.util.List;

public class ChatRoom {
    private String chatRoomId;
    private String chatRoomName;
    private ArrayList<String> chatRoomMembers;
    private List<MessageEntity> listOfMessages;
    private String imageUrl;

    ChatRoom() {
    }

    public ChatRoom(String chatRoomId, String chatRoomName, ArrayList<String> chatRoomMembers, ArrayList<MessageEntity> listOfMessages, String imageUrl) {
        this.chatRoomId = chatRoomId;
        this.chatRoomName = chatRoomName;
        this.chatRoomMembers = chatRoomMembers;
        this.listOfMessages = listOfMessages;
        this.imageUrl = imageUrl;
    }

    public MessageEntity getLastMessage() {
        return listOfMessages.get(listOfMessages.size() - 1);
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

    public ArrayList<String> getChatRoomMembers() {
        return chatRoomMembers;
    }

    public String getChatRoomMembersCount() {
        return String.valueOf(chatRoomMembers.size());
    }

    public void setChatRoomMembers(ArrayList<String> chatRoomMembers) {
        this.chatRoomMembers = chatRoomMembers;
    }

    public List<MessageEntity> getListOfMessages() {
        return listOfMessages;
    }

    public void setListOfMessages(ArrayList<MessageEntity> listOfMessages) {
        this.listOfMessages = listOfMessages;
    }
}

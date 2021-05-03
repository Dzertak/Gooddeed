package com.kravchenko.apps.gooddeed.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.kravchenko.apps.gooddeed.database.entity.ChatRoom;
import com.kravchenko.apps.gooddeed.repository.ChatRepository;
import com.kravchenko.apps.gooddeed.screen.adapter.message.MessageEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatViewModel extends ViewModel {

    private final ChatRepository chatRepository;

    public ChatViewModel() {
        chatRepository = new ChatRepository();
    }

    public void initComponentsForCurrentChat(String currentChatRoomId) {
        chatRepository.initComponentsForCurrentChat(currentChatRoomId);
    }

    public LiveData<String> getInitiativeIconUri() {
       return chatRepository.getInitiativeIconUri();
    }

    public LiveData<String> getInitiativeTitle() {
        return chatRepository.getInitiativeTitle();
    }

    public void sendMessage(String message) {
        chatRepository.sendMessage(message);
    }

    public LiveData<String> getChatRoomMembersCount() {
        return chatRepository.getChatRoomMembersCount();
    }

    public LiveData<Boolean> arraysForMessagesAreFilled() {
        return chatRepository.arraysForMessagesAreFilled();
    }

    public LiveData<ArrayList<MessageEntity>> getListOfMessages() {
        return chatRepository.getListOfMessages();
    }

    public LiveData<HashMap<String, String>> getFullNames() {
        return chatRepository.getFullNames();
    }

    public LiveData<HashMap<String, String>> getAvatarUrls() {
        return chatRepository.getAvatarUrls();
    }

    public void getDataForChatRooms() {
        chatRepository.getDataForChatRooms();
    }

    public LiveData<List<ChatRoom>> getChatroomsOfCurrentUser() {
        return chatRepository.getChatroomsOfCurrentUser();
    }
}

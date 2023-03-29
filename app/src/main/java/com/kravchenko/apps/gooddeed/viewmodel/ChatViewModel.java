package com.kravchenko.apps.gooddeed.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.kravchenko.apps.gooddeed.database.entity.ChatRoom;
import com.kravchenko.apps.gooddeed.repository.ChatRepository;
import com.kravchenko.apps.gooddeed.util.Resource;

import java.util.HashMap;
import java.util.List;

public class ChatViewModel extends ViewModel {

    private final ChatRepository chatRepository;

    public ChatViewModel() {
        chatRepository = new ChatRepository();
    }

    public void initComponentsForCurrentChat(String currentInitiativeId) {
        chatRepository.initComponentsForCurrentChat(currentInitiativeId);
    }

    public void sendMessage(String message) {
        chatRepository.sendMessage(message);
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

    public LiveData<Resource<List<ChatRoom>>> getChatroomsOfCurrentUser() {
        return chatRepository.getChatRoomsOfCurrentUser();
    }
}

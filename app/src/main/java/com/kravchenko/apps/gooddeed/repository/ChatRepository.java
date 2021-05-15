package com.kravchenko.apps.gooddeed.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kravchenko.apps.gooddeed.database.entity.ChatRoom;
import com.kravchenko.apps.gooddeed.database.entity.FirestoreUser;
import com.kravchenko.apps.gooddeed.screen.adapter.message.MessageEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatRepository {
    private DatabaseReference myRefCurrentChat;
    private String userId;
    private String currentInitiativeId;
    private ArrayList<String> chatRoomMembersIds;
    private HashMap<String, String> fullNames;
    private HashMap<String, String> avatarUrls;
    private MutableLiveData<HashMap<String, String>> fullNamesLiveData;
    private MutableLiveData<HashMap<String, String>> avatarUrlsLiveData;
    private MutableLiveData<List<ChatRoom>> chatRoomsOfCurrentUserLiveData;
    private final MutableLiveData<ChatRoom> currentChatRoomLiveData;
    private List<String> listOfChatIds;

    public ChatRepository() {
        initializeComponents();
        currentChatRoomLiveData = new MutableLiveData<>();
    }

    public void initComponentsForCurrentChat(String currentInitiativeId) {
        this.currentInitiativeId = currentInitiativeId;

        myRefCurrentChat = FirebaseDatabase.getInstance().getReference("chats").child(currentInitiativeId);
        myRefCurrentChat.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot currentChatSnapshot) {
                FirebaseFirestore.getInstance().collection("initiatives")
                        .document(currentInitiativeId).get().addOnSuccessListener(currentInitiativeSnapshot -> {

                    ChatRoom chatRoom;
                    if (currentChatSnapshot.getValue() == null) {
                        ArrayList<String> chatRoomMembersIds = new ArrayList<>();
                        chatRoomMembersIds.add(userId);
                        chatRoom = new ChatRoom(currentInitiativeId, String.valueOf(currentInitiativeSnapshot.get("title")),
                                chatRoomMembersIds, new ArrayList<>(), String.valueOf(currentInitiativeSnapshot.get("imgUri")));
                        myRefCurrentChat.setValue(chatRoom);
                    } else {
                        chatRoom = currentChatSnapshot.getValue(ChatRoom.class);
                        ArrayList<String> chatRoomMembers = null;
                        if (chatRoom != null) {
                            chatRoomMembers = chatRoom.getMembers();
                        }
                        if (chatRoomMembers != null && !chatRoomMembers.contains(userId)) {
                            chatRoomMembers.add(userId);
                            chatRoom.setMembers(chatRoomMembers);
                            myRefCurrentChat.child("members").setValue(chatRoomMembers);
                        }
                    }
                    currentChatRoomLiveData.setValue(chatRoom);
                    if (chatRoom != null) {
                        chatRoomMembersIds = chatRoom.getMembers();
                    }
                    for (String memberId : chatRoomMembersIds) {
                        FirebaseFirestore.getInstance().collection("users").document(memberId)
                                .get().addOnSuccessListener(documentSnapshot -> {
                            String firstName, lastName, imageUrl;
                            firstName = (String) documentSnapshot.get("firstName");
                            lastName = (String) documentSnapshot.get("lastName");
                            imageUrl = (String) documentSnapshot.get("imageUrl");
                            if (firstName != null || lastName != null)
                                fullNames.put(memberId, firstName + " " + lastName);
                            if (imageUrl != null) avatarUrls.put(memberId, imageUrl);
                        });
                    }
                    fullNamesLiveData.setValue(fullNames);
                    avatarUrlsLiveData.setValue(avatarUrls);
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(userId);
        userRef.get().addOnSuccessListener(documentSnapshot -> {
            FirestoreUser firestoreUser = documentSnapshot.toObject(FirestoreUser.class);
            List<String> chats = new ArrayList<>();
            if (firestoreUser != null && firestoreUser.getChats() != null) {
                chats = firestoreUser.getChats();
            }
            if (!chats.contains(currentInitiativeId)) {
                chats.add(currentInitiativeId);
                Map<String, Object> map = new HashMap<>();
                map.put("chats", chats);
                userRef.update(map);
            }
        });
    }

    public void sendMessage(String message) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", userId);
        hashMap.put("textOfMessage", message);
        hashMap.put("timeInMillis", System.currentTimeMillis());
        FirebaseDatabase.getInstance().getReference().child("chats").child(currentInitiativeId)
                .child("messages").push().setValue(hashMap);
    }

    public LiveData<HashMap<String, String>> getFullNames() {
        return fullNamesLiveData;
    }

    public LiveData<HashMap<String, String>> getAvatarUrls() {
        return avatarUrlsLiveData;
    }

    public void getDataForChatRooms() {
        List<ChatRoom> chatRoomsOfCurrentUser = new ArrayList<>();
        DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(userId);
        userRef.get().addOnSuccessListener(documentSnapshot -> {
            FirestoreUser firestoreUser = documentSnapshot.toObject(FirestoreUser.class);
            if (firestoreUser != null && firestoreUser.getChats() != null) {
                listOfChatIds = firestoreUser.getChats();
            }
            for (String roomId : listOfChatIds) {
                DatabaseReference chatRefFromList = FirebaseDatabase.getInstance().getReference("chats").child(roomId);
                chatRefFromList.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<MessageEntity> arrayListOfMessageEntities = new ArrayList<>();
                        for (DataSnapshot message : snapshot.child("messages").getChildren()) {
                            MessageEntity messageEntity = new MessageEntity();
                            messageEntity.setSender(String.valueOf(message.child("sender").getValue()));
                            messageEntity.setTimeInMillis((Long) message.child("timeInMillis").getValue());
                            messageEntity.setTextOfMessage(String.valueOf(message.child("textOfMessage").getValue()));
                            arrayListOfMessageEntities.add(messageEntity);
                        }
                        ChatRoom chatroom = new ChatRoom(roomId, String.valueOf(snapshot.child("chatRoomName").getValue()),
                                (ArrayList<String>) snapshot.child("members").getValue(),
                                arrayListOfMessageEntities, String.valueOf(snapshot.child("imageUrl").getValue()));
                        chatRoomsOfCurrentUser.add(chatroom);
                        chatRoomsOfCurrentUserLiveData.setValue(chatRoomsOfCurrentUser);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
    }

    public LiveData<List<ChatRoom>> getChatRoomsOfCurrentUser() {
        return chatRoomsOfCurrentUserLiveData;
    }

    private void initializeComponents() {
        listOfChatIds = new ArrayList<>();
        chatRoomMembersIds = new ArrayList<>();
        chatRoomMembersIds = new ArrayList<>();
        fullNamesLiveData = new MutableLiveData<>();
        avatarUrlsLiveData = new MutableLiveData<>();
        fullNames = new HashMap<>();
        avatarUrls = new HashMap<>();
        chatRoomsOfCurrentUserLiveData = new MutableLiveData<>();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}

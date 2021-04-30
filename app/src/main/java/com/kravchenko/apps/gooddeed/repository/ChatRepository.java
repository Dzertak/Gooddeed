package com.kravchenko.apps.gooddeed.repository;

import android.util.Log;

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
import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.database.entity.ChatRoom;
import com.kravchenko.apps.gooddeed.database.entity.FirestoreUser;
import com.kravchenko.apps.gooddeed.screen.adapter.message.MessageEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatRepository {
    private DatabaseReference myRefCurrentChatroom;
    private ArrayList<MessageEntity> listOfMessages;
    private String userId;
    private long messageNumber;
    private String currentChatRoomId;
    private ArrayList<String> chatRoomMembersIds;
    private MutableLiveData<String> initiativeIconUri;
    private MutableLiveData<String> initiativeTitle;
    private MutableLiveData<String> chatRoomMembersCount;
    private MutableLiveData<ArrayList<MessageEntity>> listOfMessagesLiveData;
    private MutableLiveData<Boolean> arraysForMessagesAreFilled;
    private HashMap<String, String> fullNames;
    private HashMap<String, String> avatarUrls;
    private MutableLiveData<HashMap<String, String>> fullNamesLiveData;
    private MutableLiveData<HashMap<String, String>> avatarUrlsLiveData;
    private MutableLiveData<Boolean> arraysForChatroomsListAreFilled;
    private ArrayList<String> chatroomsOfCurrentUser;
    private HashMap<String, String> allChatRoomNames;
    private HashMap<String, String> lastMessages;
    private MutableLiveData<HashMap<String, String>> allChatRoomNamesLiveData;
    private MutableLiveData<HashMap<String, String>> lastMessagesLiveData;
    private MutableLiveData<ArrayList<String>> chatroomsOfCurrentUserLiveData;
    private static final String TAG = "gooddeed_tag";

    public ChatRepository() {
        initializeComponents();
    }

    public void initComponentsForCurrentChat(String currentChatRoomId) {
        this.currentChatRoomId = currentChatRoomId;
        initiativeTitle.setValue("Initiative title"); //TODO get name of Initiative
        //For correct work, it must be created and instance of initiative and set into database,
        //and also it must be created a chat with same id and name and photo in database.
        //and then we just get it here by initiative id.

        //For now I will check here if the chat exists or not
        myRefCurrentChatroom = FirebaseDatabase.getInstance().getReference("Chats").child(currentChatRoomId);
        myRefCurrentChatroom.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null) {
                    //There's no such chat room in DB and we must create it. It must be when we create initiative
                    chatRoomMembersIds.add(userId);
                    ChatRoom chatroom = new ChatRoom(currentChatRoomId, initiativeTitle.getValue(), chatRoomMembersIds, null);
                    myRefCurrentChatroom.setValue(chatroom);
                } else {
                    //Chat room exists
                    for (DataSnapshot ds : snapshot.child("chatRoomMembers").getChildren()) {
                        String memberID = String.valueOf(ds.getValue());
                        chatRoomMembersIds.add(memberID);
                    }
                    if (!chatRoomMembersIds.contains(userId)) {
                        chatRoomMembersIds.add(userId);
                        myRefCurrentChatroom.child("chatRoomMembers").setValue(chatRoomMembersIds);
                    }
                }
                chatRoomMembersCount.setValue(String.valueOf(chatRoomMembersIds.size()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //Adding this chat id to this user in Firestore
        DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(userId);
        userRef.get().addOnSuccessListener(documentSnapshot -> {
            FirestoreUser firestoreUser = documentSnapshot.toObject(FirestoreUser.class);
            ArrayList<String> chats = new ArrayList<>();
            if (firestoreUser != null && firestoreUser.getChats() != null) {
                chats = firestoreUser.getChats();
            }
            if (!chats.contains(currentChatRoomId)) {
                chats.add(currentChatRoomId);
                Map<String, Object> map = new HashMap<>();
                map.put("chats", chats);
                userRef.update(map);
            }
        });

        myRefCurrentChatroom.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //get an Image of Initiative from Firebase Storage?? for now it's
                initiativeIconUri.setValue(String.valueOf(R.drawable.photogeo));
                //get name of chat room
                if (snapshot.child("chatRoomName").getValue() != null) {
                    initiativeTitle.setValue(snapshot.child("chatRoomName").getValue().toString());
                }
                messageNumber = snapshot.child("messages").getChildrenCount();
                listOfMessages = new ArrayList<>();
                for (DataSnapshot ds : snapshot.child("messages").getChildren()) {
                    MessageEntity messageEntity = ds.getValue(MessageEntity.class);
                    listOfMessages.add(messageEntity);
                }
                listOfMessagesLiveData.setValue(listOfMessages);
                if (listOfMessagesLiveData.getValue() != null && fullNamesLiveData != null && avatarUrls != null)
                    arraysForMessagesAreFilled.setValue(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        for (String userId : chatRoomMembersIds) {
            FirebaseFirestore.getInstance().collection("users").document(userId)
                    .get().addOnSuccessListener(documentSnapshot -> {
                String firstName, lastName, imageUrl;
                firstName = (String) documentSnapshot.get("firstName");
                lastName = (String) documentSnapshot.get("lastName");
                imageUrl = (String) documentSnapshot.get("imageUrl");
                if (firstName != null || lastName != null)
                    fullNames.put(userId, firstName + " " + lastName);
                if (imageUrl != null) avatarUrls.put(userId, imageUrl);
            });
        }
        fullNamesLiveData.setValue(fullNames);
        avatarUrlsLiveData.setValue(avatarUrls);
        if (listOfMessagesLiveData.getValue() != null && avatarUrls != null)
            arraysForMessagesAreFilled.setValue(true);
    }

    public void sendMessage(String message) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("number", messageNumber + 1);
        hashMap.put("sender", userId);
        hashMap.put("textOfMessage", message);
        FirebaseDatabase.getInstance().getReference().child("Chats").child(currentChatRoomId)
                .child("messages").push().setValue(hashMap);
    }

    public LiveData<String> getInitiativeIconUri() {
        return initiativeIconUri;
    }

    public LiveData<String> getInitiativeTitle() {
        return initiativeTitle;
    }

    public LiveData<String> getChatRoomMembersCount() {
        return chatRoomMembersCount;
    }

    public LiveData<Boolean> arraysForMessagesAreFilled() {
        return arraysForMessagesAreFilled;
    }

    public LiveData<ArrayList<MessageEntity>> getListOfMessages() {
        return listOfMessagesLiveData;
    }

    public LiveData<HashMap<String, String>> getFullNames() {
        return fullNamesLiveData;
    }

    public LiveData<HashMap<String, String>> getAvatarUrls() {
        return avatarUrlsLiveData;
    }

    public LiveData<Boolean> arraysForChatroomsListAreFilled() {
        return arraysForChatroomsListAreFilled;
    }

    public void getDataForChatRooms() {
        chatroomsOfCurrentUser = new ArrayList<>();
        DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(userId);
        userRef.get().addOnSuccessListener(documentSnapshot -> {
            FirestoreUser firestoreUser = documentSnapshot.toObject(FirestoreUser.class);

            if (firestoreUser != null && firestoreUser.getChats() != null) {
                chatroomsOfCurrentUser = firestoreUser.getChats();
                Log.i(TAG, firestoreUser.getChats().toString());
            }
        });
        for (String roomId : chatroomsOfCurrentUser) {

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Chats").child(roomId);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String lastMessageOfThisChat = null;
                    if (snapshot.child("chatRoomName").getValue() != null) {
                        allChatRoomNames.put(roomId, snapshot.child("chatRoomName").getValue().toString());
                    }
                    snapshot.child("messages").getChildren();
                    for (DataSnapshot ds : snapshot.child("messages").getChildren()) {
                        MessageEntity messageEntity = ds.getValue(MessageEntity.class);
                        lastMessageOfThisChat = messageEntity.getTextOfMessage();
                    }
                    lastMessages.put(roomId, lastMessageOfThisChat);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        allChatRoomNamesLiveData.setValue(allChatRoomNames);
        lastMessagesLiveData.setValue(lastMessages);
        chatroomsOfCurrentUserLiveData.setValue(chatroomsOfCurrentUser);
    }

    public LiveData<HashMap<String, String>> getAllChatRoomNamesLiveData() {
        return allChatRoomNamesLiveData;
    }

    public LiveData<HashMap<String, String>> getLastMessagesLiveData() {
        return lastMessagesLiveData;
    }

    public LiveData<ArrayList<String>> getChatroomsOfCurrentUser() {
        return chatroomsOfCurrentUserLiveData;
    }

    private void initializeComponents() {
        arraysForMessagesAreFilled = new MutableLiveData<>();
        arraysForChatroomsListAreFilled = new MutableLiveData<>();
        initiativeIconUri = new MutableLiveData<>();
        initiativeTitle = new MutableLiveData<>();
        chatRoomMembersCount = new MutableLiveData<>();
        listOfMessagesLiveData = new MutableLiveData<>();
        chatRoomMembersIds = new ArrayList<>();
        fullNamesLiveData = new MutableLiveData<>();
        avatarUrlsLiveData = new MutableLiveData<>();
        fullNames = new HashMap<>();
        avatarUrls = new HashMap<>();
        allChatRoomNames = new HashMap<>();
        lastMessages = new HashMap<>();
        allChatRoomNamesLiveData = new MutableLiveData<>();
        lastMessagesLiveData = new MutableLiveData<>();
        chatroomsOfCurrentUserLiveData = new MutableLiveData<>();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}

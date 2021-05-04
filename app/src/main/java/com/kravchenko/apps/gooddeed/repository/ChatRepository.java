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
    private DatabaseReference myRefCurrentChatroom;
    private String userId;
    private String currentChatRoomId;
    private ArrayList<String> chatRoomMembersIds;
    private HashMap<String, String> fullNames;
    private HashMap<String, String> avatarUrls;
    private MutableLiveData<HashMap<String, String>> fullNamesLiveData;
    private MutableLiveData<HashMap<String, String>> avatarUrlsLiveData;
    private MutableLiveData<List<ChatRoom>> chatroomsOfCurrentUserLiveData;
    private static final String TAG = "gooddeed_tag";
    private final MutableLiveData<ChatRoom> currentChatRoomLiveData;
    private List<String> listOfchatIds;

    public ChatRepository() {
        initializeComponents();
        currentChatRoomLiveData = new MutableLiveData<>();
    }

    public void initComponentsForCurrentChat(String currentChatRoomId) {
        this.currentChatRoomId = currentChatRoomId;
        String initiativeTitle = "Initiative title"; //TODO get name of Initiative
        //For correct work, it must be created and instance of initiative and set into database,
        //and also it must be created a chat with same id and name and photo in database.
        //and then we just get it here by chat room id.

        //For now I will check here if the chat exists or not
        myRefCurrentChatroom = FirebaseDatabase.getInstance().getReference("Chats").child(currentChatRoomId);
        myRefCurrentChatroom.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ChatRoom currentChatRoom = null;
                if (snapshot.getValue() == null) {
                    //There's no such chat room in DB and we must create it. It must be when we create initiative
                    ArrayList<String> chatRoomMembersIds = new ArrayList<>();
                    chatRoomMembersIds.add(userId);
                    currentChatRoom = new ChatRoom(currentChatRoomId, initiativeTitle, chatRoomMembersIds, new ArrayList<>(), "default");
                    myRefCurrentChatroom.setValue(currentChatRoom);
                } else {
                    //Chat room exists
                    //Adding current user to this chat if it's needed
                    currentChatRoom = snapshot.getValue(ChatRoom.class);
                    ArrayList<String> chatRoomMembers = currentChatRoom.getChatRoomMembers();
                    if (!chatRoomMembers.contains(userId)) {
                        chatRoomMembers.add(userId);
                        currentChatRoom.setChatRoomMembers(chatRoomMembers);
                        myRefCurrentChatroom.child("chatRoomMembers").setValue(chatRoomMembers);
                    }
                }
                currentChatRoomLiveData.setValue(currentChatRoom);
                chatRoomMembersIds = currentChatRoom.getChatRoomMembers();
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //Adding this chat id to this user in Firestore
        DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(userId);
        userRef.get().addOnSuccessListener(documentSnapshot -> {
            FirestoreUser firestoreUser = documentSnapshot.toObject(FirestoreUser.class);
            List<String> chats = new ArrayList<>();
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
    }

    public void sendMessage(String message) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", userId);
        hashMap.put("textOfMessage", message);
        hashMap.put("timeInMillis",System.currentTimeMillis());
        FirebaseDatabase.getInstance().getReference().child("Chats").child(currentChatRoomId)
                .child("messages").push().setValue(hashMap);
    }

    public LiveData<HashMap<String, String>> getFullNames() {
        return fullNamesLiveData;
    }

    public LiveData<HashMap<String, String>> getAvatarUrls() {
        return avatarUrlsLiveData;
    }

    public void getDataForChatRooms() {
        List<ChatRoom> chatroomsOfCurrentUser = new ArrayList<>();
        DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(userId);
        userRef.get().addOnSuccessListener(documentSnapshot -> {
            FirestoreUser firestoreUser = documentSnapshot.toObject(FirestoreUser.class);
            if (firestoreUser != null && firestoreUser.getChats() != null) {
                listOfchatIds = firestoreUser.getChats();
            }
            for (String roomId : listOfchatIds) {
                DatabaseReference chatRefFromList = FirebaseDatabase.getInstance().getReference("Chats").child(roomId);
                chatRefFromList.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<MessageEntity> arrayListOfMessageEntities = new ArrayList<>();
                        for (DataSnapshot message : snapshot.child("messages").getChildren()) {
                            MessageEntity messageEntity = new MessageEntity();
                            messageEntity.setSender(message.child("sender").getValue().toString());
                            messageEntity.setTimeInMillis((Long) message.child("timeInMillis").getValue());
                            messageEntity.setTextOfMessage(message.child("textOfMessage").getValue().toString());
                            arrayListOfMessageEntities.add(messageEntity);
                        }
                        ChatRoom chatroom = new ChatRoom(
                                roomId,
                                snapshot.child("chatRoomName").getValue().toString(),
                                (ArrayList<String>) snapshot.child("chatRoomMembers").getValue(),
                                arrayListOfMessageEntities,
                                snapshot.child("imageUrl").getValue().toString());
                        chatroomsOfCurrentUser.add(chatroom);
                        chatroomsOfCurrentUserLiveData.setValue(chatroomsOfCurrentUser);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
    }

    public LiveData<List<ChatRoom>> getChatroomsOfCurrentUser() {
        return chatroomsOfCurrentUserLiveData;
    }

    private void initializeComponents() {
        listOfchatIds = new ArrayList<>();
        chatRoomMembersIds = new ArrayList<>();
        chatRoomMembersIds = new ArrayList<>();
        fullNamesLiveData = new MutableLiveData<>();
        avatarUrlsLiveData = new MutableLiveData<>();
        fullNames = new HashMap<>();
        avatarUrls = new HashMap<>();
        chatroomsOfCurrentUserLiveData = new MutableLiveData<>();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}

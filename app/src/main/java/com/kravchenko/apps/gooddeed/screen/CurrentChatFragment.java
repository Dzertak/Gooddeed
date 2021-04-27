package com.kravchenko.apps.gooddeed.screen;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.database.entity.ChatRoom;
import com.kravchenko.apps.gooddeed.databinding.FragmentCurrentChatBinding;
import com.kravchenko.apps.gooddeed.screen.adapter.MessageAdapter;
import com.kravchenko.apps.gooddeed.screen.adapter.MessageEntity;

import java.util.ArrayList;
import java.util.List;

public class CurrentChatFragment extends Fragment {

    private String currentChatRoomId;
    private DatabaseReference myRefChatroom;
    private FragmentCurrentChatBinding currentChatBinding;
    private MessageAdapter messageAdapter;
    List<MessageEntity> listOfMessages;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        currentChatBinding = FragmentCurrentChatBinding.inflate(inflater,container,false);
        return currentChatBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            currentChatRoomId = getArguments().getString("initiative_id");
        }
        //For correct work, it must be created and instance of initiative and set into database,
        //and also it must be created a chat with same id and name and photo in database.
        //and then we just get it here by initiative id.

        //For now I will check here if the chat exists or not

        myRefChatroom = FirebaseDatabase.getInstance().getReference("Chats").child(currentChatRoomId);

        myRefChatroom.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null) {
                    //There's no such chatRoom in DB and we must create it.
                    String chatRoomName = "Name of initiative"; //TODO get name of Initiative
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    ArrayList<String> usersOfChatRoom = new ArrayList<>();
                    usersOfChatRoom.add(userId);
                    ChatRoom chatroom = new ChatRoom(currentChatRoomId, chatRoomName,usersOfChatRoom,null);
                    myRefChatroom.setValue(chatroom);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        myRefChatroom.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //get an Image of Initiative from Firebase Storage?? for now it's
                Glide.with(view).load(R.drawable.photogeo).circleCrop().into(currentChatBinding.currentChatroomLogo);
                currentChatBinding.tvChatroomName.setText(snapshot.child("chatRoomName").getValue().toString());
                String members = snapshot.child("chatRoomMembers").getChildrenCount()+" "+getString(R.string.people_in_chat);
                currentChatBinding.tvMembers.setText(members);
                //work with recycler
                listOfMessages = new ArrayList<>();
                for(DataSnapshot ds : snapshot.child("messages").getChildren()) {
                    MessageEntity messageEntity = ds.getValue(MessageEntity.class);
                    listOfMessages.add(messageEntity);
                }
                //MessageEntity lastItem = listOfMessages.get(listOfMessages.size() - 1);
                currentChatBinding.recyclerMessages.setHasFixedSize(true);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                messageAdapter = new MessageAdapter(listOfMessages);
                currentChatBinding.recyclerMessages.setAdapter(messageAdapter);
                currentChatBinding.recyclerMessages.setLayoutManager(linearLayoutManager);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        myRefChatroom.child("messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //recyclerview.notifydatasetchanged;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
package com.kravchenko.apps.gooddeed.screen.chat;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.database.entity.ChatRoom;
import com.kravchenko.apps.gooddeed.databinding.FragmentChatCurrentBinding;
import com.kravchenko.apps.gooddeed.screen.adapter.message.MessageAdapter;
import com.kravchenko.apps.gooddeed.screen.adapter.message.MessageEntity;
import com.kravchenko.apps.gooddeed.viewmodel.ChatViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CurrentChatFragment extends Fragment {

    private String currentChatRoomId;
    private FragmentChatCurrentBinding currentChatBinding;
    private ChatViewModel chatViewModel;
    private MessageAdapter messageAdapter;
    private ArrayList<MessageEntity> listOfMessages;
    private HashMap<String, String> fullNames;
    private HashMap<String, String> avatarUrls;
    private static final String TAG = "gooddeed_tag";
    private ChatRoom currentChatRoom;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        currentChatBinding = FragmentChatCurrentBinding.inflate(inflater, container, false);
        return currentChatBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listOfMessages = new ArrayList<>();
        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        chatViewModel.getFullNames().observe(getActivity(), fullnamesMap -> {
            fullNames = fullnamesMap;
        });
        chatViewModel.getAvatarUrls().observe(getActivity(), avatarUrlsMap -> {
            avatarUrls = avatarUrlsMap;
        });
        if (getArguments() != null) {
            currentChatRoomId = getArguments().getString("chatroom_id");
        }
        chatViewModel.initComponentsForCurrentChat(currentChatRoomId);
        FirebaseDatabase.getInstance().getReference("Chats").child(currentChatRoomId).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot message : snapshot.child("messages").getChildren()) {
                            MessageEntity messageEntity = new MessageEntity();
                            messageEntity.setNumber((Long) message.child("number").getValue());
                            messageEntity.setSender(String.valueOf(message.child("sender").getValue()));
                            messageEntity.setTextOfMessage(String.valueOf(message.child("textOfMessage").getValue()));
                            listOfMessages.add(messageEntity);
                        }

                        currentChatRoom = snapshot.getValue(ChatRoom.class);
                        if (currentChatRoom != null) {
                            if (!currentChatRoom.getImageUrl().equals("default")) {
                                Glide.with(getActivity()).load(Uri.parse(currentChatRoom.getImageUrl())).circleCrop().into(currentChatBinding.currentChatroomLogo);
                            }
                            String text = getString(R.string.people_in_chat) + " " + currentChatRoom.getChatRoomMembersCount();
                            currentChatBinding.tvMembers.setText(text);
                            currentChatRoom.setListOfMessages(listOfMessages);
                            currentChatBinding.recyclerMessages.setHasFixedSize(true);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                            messageAdapter = new MessageAdapter(currentChatRoom, fullNames, avatarUrls, getActivity());
                            currentChatBinding.recyclerMessages.setAdapter(messageAdapter);
                            linearLayoutManager.setStackFromEnd(true);
                            currentChatBinding.recyclerMessages.setLayoutManager(linearLayoutManager);
                            //TODO scroll to last item which was read by user
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

        currentChatBinding.btnSendMessage.setOnClickListener(v -> {
            if (!currentChatBinding.etTypeMessage.getText().toString().trim().equals("")) {
                chatViewModel.sendMessage(currentChatBinding.etTypeMessage.getText().toString());
                currentChatBinding.etTypeMessage.setText("");
            }
        });
    }
}
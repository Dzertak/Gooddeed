package com.kravchenko.apps.gooddeed.screen.chat;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.database.entity.ChatRoom;
import com.kravchenko.apps.gooddeed.databinding.FragmentChatCurrentBinding;
import com.kravchenko.apps.gooddeed.screen.BaseFragment;
import com.kravchenko.apps.gooddeed.screen.adapter.message.MessageAdapter;
import com.kravchenko.apps.gooddeed.screen.adapter.message.MessageEntity;
import com.kravchenko.apps.gooddeed.viewmodel.ChatViewModel;

import java.util.ArrayList;
import java.util.HashMap;

public class CurrentChatFragment extends BaseFragment {

    private String currentInitiativeId;
    private FragmentChatCurrentBinding currentChatBinding;
    private ChatViewModel chatViewModel;
    private MessageAdapter messageAdapter;
    private HashMap<String, String> fullNames;
    private HashMap<String, String> avatarUrls;
    private ChatRoom currentChatRoom;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void clear() {

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
        currentChatBinding.linearLayoutGoToInitiative.setOnClickListener(v ->
                Navigation.findNavController(v).navigate
                        (R.id.action_currentChatFragment_to_currentInitiativeFragment, getArguments()));
        currentChatBinding.toolbarCurrentChat.setNavigationIcon(R.drawable.ic_back);
        currentChatBinding.toolbarCurrentChat.setNavigationOnClickListener(v ->
                requireActivity().onBackPressed());
        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        chatViewModel.getFullNames().observe(requireActivity(), fullNamesMap -> {
            fullNames = fullNamesMap;
        });
        chatViewModel.getAvatarUrls().observe(requireActivity(), avatarUrlsMap -> {
            avatarUrls = avatarUrlsMap;
        });
        if (getArguments() != null) {
            currentInitiativeId = getArguments().getString("initiative_id");
            currentChatBinding.toolbarCurrentChat.setOnClickListener(v ->
                    Navigation.findNavController(v).navigate
                            (R.id.action_currentChatFragment_to_chatInfoFragment, getArguments()));
        }
        chatViewModel.initComponentsForCurrentChat(currentInitiativeId);
        FirebaseDatabase.getInstance().getReference("Initiatives").child(currentInitiativeId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (getActivity() == null) {
                            return;
                        }
                        ArrayList<MessageEntity> listOfMessages = new ArrayList<>();
                        for (DataSnapshot message : snapshot.child("chat").child("messages").getChildren()) {
                            MessageEntity messageEntity = new MessageEntity();
                            messageEntity.setSender(String.valueOf(message.child("sender").getValue()));
                            messageEntity.setTimeInMillis((Long) message.child("timeInMillis").getValue());
                            messageEntity.setTextOfMessage(String.valueOf(message.child("textOfMessage").getValue()));
                            listOfMessages.add(messageEntity);
                        }

                        currentChatRoom = snapshot.child("chat").getValue(ChatRoom.class);
                        if (currentChatRoom != null) {
                            currentChatBinding.tvGoToInitiativeName.setText(String.valueOf(snapshot.child("title").getValue()));
                            currentChatBinding.tvChatroomName.setText(String.valueOf(snapshot.child("title").getValue()));
                            if (!String.valueOf(snapshot.child("imageUrl").getValue()).equals("default")) {
                                Glide.with(getActivity()).load(Uri.parse(String.valueOf(snapshot.child("imageUrl")
                                        .getValue()))).circleCrop().into(currentChatBinding.currentChatroomLogo);
                            }
                            String text = getString(R.string.people_in_chat) + " " + currentChatRoom.getMembers().size();
                            currentChatBinding.tvMembers.setText(text);
                            currentChatRoom.setListOfMessages(listOfMessages);
                            currentChatBinding.recyclerMessages.setHasFixedSize(true);
                            linearLayoutManager = new LinearLayoutManager(getContext());
                            messageAdapter = new MessageAdapter(currentChatRoom, fullNames, avatarUrls, getContext());
                            currentChatBinding.recyclerMessages.setAdapter(messageAdapter);
                            linearLayoutManager.setStackFromEnd(false);
                            currentChatBinding.recyclerMessages.setLayoutManager(linearLayoutManager);
                            linearLayoutManager.scrollToPosition(messageAdapter.getItemCount() - 1);
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
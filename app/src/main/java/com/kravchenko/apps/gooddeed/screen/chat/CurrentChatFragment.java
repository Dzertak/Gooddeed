package com.kravchenko.apps.gooddeed.screen.chat;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.databinding.FragmentChatCurrentBinding;
import com.kravchenko.apps.gooddeed.screen.adapter.message.MessageAdapter;
import com.kravchenko.apps.gooddeed.screen.adapter.message.MessageEntity;
import com.kravchenko.apps.gooddeed.viewmodel.ChatViewModel;

import java.util.ArrayList;
import java.util.HashMap;

public class CurrentChatFragment extends Fragment {

    private String currentChatRoomId;
    private FragmentChatCurrentBinding currentChatBinding;
    private ChatViewModel chatViewModel;
    private MessageAdapter messageAdapter;
    private ArrayList<MessageEntity> listOfMessages;
    private HashMap<String, String> fullNames;
    private HashMap<String, String> avatarUrls;

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
        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        if (getArguments() != null) {
            currentChatRoomId = getArguments().getString("chatroom_id");
        }
        chatViewModel.getInitiativeTitle().observe(getActivity(), s -> {
            currentChatBinding.tvChatroomName.setText(s);
        });
        chatViewModel.getInitiativeIconUri().observe(getActivity(), s -> {
            Glide.with(getActivity()).load(Uri.parse(s)).circleCrop().into(currentChatBinding.currentChatroomLogo);
        });
        chatViewModel.getChatRoomMembersCount().observe(getActivity(), s -> {
            String text = getString(R.string.people_in_chat) + " " + s;
            currentChatBinding.tvMembers.setText(text);
        });
        currentChatBinding.btnSendMessage.setOnClickListener(v -> {
            if (!currentChatBinding.etTypeMessage.getText().toString().trim().equals("")) {
                chatViewModel.sendMessage(currentChatBinding.etTypeMessage.getText().toString());
                currentChatBinding.etTypeMessage.setText("");
            }
        });
        chatViewModel.initComponentsForCurrentChat(currentChatRoomId);

        //recyclerview
        //MessageEntity lastItem = listOfMessages.get(listOfMessages.size() - 1);

        chatViewModel.getListOfMessages().observe(getActivity(), messageEntities -> listOfMessages = messageEntities);
        chatViewModel.getFullNames().observe(getActivity(), fullnamesMap -> fullNames = fullnamesMap);
        chatViewModel.getAvatarUrls().observe(getActivity(), avatarUrlsMap -> avatarUrls = avatarUrlsMap);

        chatViewModel.arraysForMessagesAreFilled().observe(getActivity(), filled -> {
            currentChatBinding.recyclerMessages.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            messageAdapter = new MessageAdapter(listOfMessages, fullNames, avatarUrls,getActivity());
            currentChatBinding.recyclerMessages.setAdapter(messageAdapter);
            currentChatBinding.recyclerMessages.setLayoutManager(linearLayoutManager);
            //currentChatBinding.recyclerMessages.scrollToPosition(listOfMessages.size());
            //TODO scroll to last item which was read by user
        });
    }
}
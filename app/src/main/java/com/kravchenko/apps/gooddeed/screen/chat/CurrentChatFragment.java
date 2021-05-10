package com.kravchenko.apps.gooddeed.screen.chat;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
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

    private String currentChatRoomId;
    private FragmentChatCurrentBinding currentChatBinding;
    private ChatViewModel chatViewModel;
    private MessageAdapter messageAdapter;
    private HashMap<String, String> fullNames;
    private HashMap<String, String> avatarUrls;
    private static final String TAG = "gooddeed_tag";
    private ChatRoom currentChatRoom;
    private LinearLayoutManager linearLayoutManager;

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
        currentChatBinding.linearLayoutGoToInitiative.setOnClickListener(v ->
                Navigation.findNavController(v).navigate
                        (R.id.action_currentChatFragment_to_currentInitiativeFragment, getArguments()));
        currentChatBinding.toolbarCurrentChat.setNavigationIcon(R.drawable.ic_back);
        currentChatBinding.toolbarCurrentChat.setNavigationOnClickListener(v ->
                getActivity().onBackPressed());
        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        chatViewModel.getFullNames().observe(getActivity(), fullnamesMap -> fullNames = fullnamesMap);
        chatViewModel.getAvatarUrls().observe(getActivity(), avatarUrlsMap -> avatarUrls = avatarUrlsMap);
        if (getArguments() != null) {
            currentChatRoomId = getArguments().getString("chatroom_id");
            currentChatBinding.toolbarCurrentChat.setOnClickListener(v ->
                    Navigation.findNavController(v).navigate
                            (R.id.action_currentChatFragment_to_chatInfoFragment, getArguments()));
        }
        chatViewModel.initComponentsForCurrentChat(currentChatRoomId);
        FirebaseDatabase.getInstance().getReference("Chats").child(currentChatRoomId).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (getActivity() == null) {
                            return;
                        }
                        ArrayList<MessageEntity> listOfMessages = new ArrayList<>();
                        for (DataSnapshot message : snapshot.child("messages").getChildren()) {
                            MessageEntity messageEntity = new MessageEntity();
                            messageEntity.setSender(String.valueOf(message.child("sender").getValue()));
                            messageEntity.setTimeInMillis((Long) message.child("timeInMillis").getValue());
                            messageEntity.setTextOfMessage(String.valueOf(message.child("textOfMessage").getValue()));
                            listOfMessages.add(messageEntity);
                        }

                        currentChatRoom = snapshot.getValue(ChatRoom.class);
                        if (currentChatRoom != null) {
                            currentChatBinding.tvGoToInitiativeName.setText(currentChatRoom.getChatRoomName());
                            currentChatBinding.tvChatroomName.setText(currentChatRoom.getChatRoomName());
                            if (!currentChatRoom.getImageUrl().equals("default")) {
                                Glide.with(getActivity()).load(Uri.parse(currentChatRoom.getImageUrl())).circleCrop().into(currentChatBinding.currentChatroomLogo);
                            }
                            String text = getString(R.string.people_in_chat) + " " + currentChatRoom.getChatRoomMembersCount();
                            currentChatBinding.tvMembers.setText(text);
                            currentChatRoom.setListOfMessages(listOfMessages);
                            currentChatBinding.recyclerMessages.setHasFixedSize(true);
                            linearLayoutManager = new LinearLayoutManager(getContext());
                            messageAdapter = new MessageAdapter(currentChatRoom, fullNames, avatarUrls, getContext());
                            currentChatBinding.recyclerMessages.setAdapter(messageAdapter);
                            linearLayoutManager.setStackFromEnd(false);
                            currentChatBinding.recyclerMessages.setLayoutManager(linearLayoutManager);
                            //TODO scroll to last item which was read by user
                        }
                        linearLayoutManager.scrollToPosition(messageAdapter.getItemCount() - 1);
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
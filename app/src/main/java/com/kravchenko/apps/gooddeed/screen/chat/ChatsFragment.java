package com.kravchenko.apps.gooddeed.screen.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.kravchenko.apps.gooddeed.databinding.FragmentChatsBinding;
import com.kravchenko.apps.gooddeed.screen.adapter.message.ChatRoomAdapter;
import com.kravchenko.apps.gooddeed.viewmodel.ChatViewModel;

public class ChatsFragment extends Fragment {
    private FragmentChatsBinding binding;
    private ChatViewModel chatViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        chatViewModel.getDataForChatRooms();
        chatViewModel.getChatroomsOfCurrentUser().observe(getActivity(), chatroomsOfCurrentUser -> {
            binding.recyclerChatRooms.setAdapter(new ChatRoomAdapter(chatViewModel,getContext(),chatroomsOfCurrentUser));
            binding.recyclerChatRooms.setLayoutManager(new LinearLayoutManager(getContext()));
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        chatViewModel.getChatroomsOfCurrentUser().removeObservers(getActivity());
        binding = null;
        chatViewModel = null;
    }
}
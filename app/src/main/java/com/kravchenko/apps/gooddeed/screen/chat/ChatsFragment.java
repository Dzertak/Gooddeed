package com.kravchenko.apps.gooddeed.screen.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.kravchenko.apps.gooddeed.DialogManager;
import com.kravchenko.apps.gooddeed.databinding.FragmentChatsBinding;
import com.kravchenko.apps.gooddeed.screen.BaseFragment;
import com.kravchenko.apps.gooddeed.screen.adapter.message.ChatRoomAdapter;
import com.kravchenko.apps.gooddeed.util.dialog.ProgressDialogFragment;
import com.kravchenko.apps.gooddeed.util.Resource;
import com.kravchenko.apps.gooddeed.viewmodel.ChatViewModel;

public class ChatsFragment extends BaseFragment {
    private FragmentChatsBinding binding;
    private ChatViewModel chatViewModel;

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
        binding = FragmentChatsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.toolbar);
        NavigationUI.setupWithNavController(binding.toolbar, getNavController());

        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        chatViewModel.getChatroomsOfCurrentUser().observe(getViewLifecycleOwner(), chatRoomsOfCurrentUser -> {
            if (chatRoomsOfCurrentUser != null){
                if (Resource.Status.LOADING.equals(chatRoomsOfCurrentUser.status)){
                    DialogManager.showDialog(getChildFragmentManager(), ProgressDialogFragment.TAG);
                } else if (Resource.Status.ERROR.equals(chatRoomsOfCurrentUser.status)){
                    DialogManager.hideDialog(getChildFragmentManager(), ProgressDialogFragment.TAG);
                } else {
                    DialogManager.hideDialog(getChildFragmentManager(), ProgressDialogFragment.TAG);
                    binding.recyclerChatRooms.setAdapter(new ChatRoomAdapter(chatViewModel, getContext(), chatRoomsOfCurrentUser.data));
                    binding.recyclerChatRooms.setLayoutManager(new LinearLayoutManager(getContext()));
                }
            }
        });
        chatViewModel.getDataForChatRooms();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        chatViewModel.getChatroomsOfCurrentUser().removeObservers(getViewLifecycleOwner());
        binding = null;
        chatViewModel = null;
    }
}
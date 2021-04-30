package com.kravchenko.apps.gooddeed.screen.adapter.message;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.viewmodel.ChatViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ViewHolder> {

    private ChatViewModel chatViewModel;
    private Context context;
    private HashMap<String, String> allChatRoomNames;
    private HashMap<String, String> lastMessages;
    private List<String> chatroomsOfCurrentUser;
    private static final String TAG = "gooddeed_tag";

    public ChatRoomAdapter(ChatViewModel chatViewModel, Context context, List<String> chatroomsOfCurrentUser) {
        //init components
        this.chatroomsOfCurrentUser = chatroomsOfCurrentUser;
        Log.i(TAG,chatroomsOfCurrentUser.size()+"");
        this.chatViewModel = chatViewModel;
        this.context = context;
        allChatRoomNames = new HashMap<>();
        lastMessages = new HashMap<>();
        chatViewModel.getAllChatRoomNamesLiveData().observe((LifecycleOwner) context, allChatRoomNames -> {
            this.allChatRoomNames = allChatRoomNames;
        });
        chatViewModel.getLastMessagesLiveData().observe((LifecycleOwner) context, lastMessagesLiveData -> {
            this.lastMessages = lastMessagesLiveData;
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i(TAG,"onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatroom_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //TODO
        Log.i(TAG,"onBindViewHolder");
        Glide.with(context).load(R.drawable.photogeo).circleCrop().into(holder.icon);
        String initiativeName = allChatRoomNames.get(chatroomsOfCurrentUser.get(position));
        if (initiativeName!=null) holder.textViewTitle.setText(initiativeName);
        String lastMessage = lastMessages.get(chatroomsOfCurrentUser.get(position));
        if (lastMessage!=null) holder.textViewLastMessage.setText(lastMessage);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewLastMessage;
        ImageView icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.i(TAG,"ViewHolder constructor");
            textViewTitle = itemView.findViewById(R.id.tv_username);
            textViewLastMessage = itemView.findViewById(R.id.tv_messagetext);
            icon = itemView.findViewById(R.id.imgview_sender_avatar);
        }
    }

    @Override
    public int getItemCount() {
        Log.i(TAG,"getItemCount size is "+chatroomsOfCurrentUser.size());
        return chatroomsOfCurrentUser.size();
    }
}

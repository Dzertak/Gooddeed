package com.kravchenko.apps.gooddeed.screen.adapter.message;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.database.entity.ChatRoom;
import com.kravchenko.apps.gooddeed.viewmodel.ChatViewModel;

import java.util.List;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ViewHolder> {

    private final ChatViewModel chatViewModel;
    private final Context context;
    private final List<ChatRoom> chatroomsOfCurrentUser;
    private final String TAG = "gooddeed_tag";

    public ChatRoomAdapter(ChatViewModel chatViewModel, Context context, List<ChatRoom> chatroomsOfCurrentUser) {
        this.chatroomsOfCurrentUser = chatroomsOfCurrentUser;
        this.chatViewModel = chatViewModel;
        this.context = context;
        //before it was hashmaps with names and last messages here
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chatroom, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatRoom chatroomEntity = chatroomsOfCurrentUser.get(position);
        if (chatroomEntity.getImageUrl().equals("default"))
            Glide.with(context).load(R.drawable.gooddeed_logo).circleCrop().into(holder.icon);
        else if (chatroomEntity.getImageUrl() != null)
            Glide.with(context).load(Uri.parse(chatroomEntity.getImageUrl())).circleCrop().into(holder.icon);
        holder.textViewTitle.setText(chatroomEntity.getChatRoomName());
        if (chatroomEntity.getLastMessage() != null)
            holder.textViewLastMessage.setText(chatroomEntity.getLastMessage().getTextOfMessage());
        holder.chatroomItemRoot.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString("chatroom_id", chatroomEntity.getChatRoomId());
            Navigation.findNavController(v).navigate(R.id.action_chatsFragment_to_currentChatFragment, args);
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewLastMessage;
        ImageView icon;
        ConstraintLayout chatroomItemRoot;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.initiative_name);
            textViewLastMessage = itemView.findViewById(R.id.last_message);
            icon = itemView.findViewById(R.id.initiative_image);
            chatroomItemRoot = itemView.findViewById(R.id.chatroom_item_root);
        }
    }

    @Override
    public int getItemCount() {
        return chatroomsOfCurrentUser.size();
    }
}

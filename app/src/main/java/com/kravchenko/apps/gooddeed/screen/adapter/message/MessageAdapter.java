package com.kravchenko.apps.gooddeed.screen.adapter.message;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.database.entity.ChatRoom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private final List<MessageEntity> listOfMessages;
    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;
    private final String currentUserId;
    private final HashMap<String, String> fullNames;
    private final HashMap<String, String> avatarUrls;
    private final Context context;

    public MessageAdapter(ArrayList<MessageEntity> listOfMessages, HashMap<String, String> fullNames, HashMap<String, String> avatarUrls, Context context) {
        this.fullNames = fullNames;
        this.avatarUrls = avatarUrls;
        this.listOfMessages = listOfMessages;
        this.context = context;
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == MSG_TYPE_RIGHT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_right, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_left, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        MessageEntity messageEntity = listOfMessages.get(position);
        holder.textViewMessage.setText(messageEntity.getTextOfMessage());
        if (getItemViewType(position) == MSG_TYPE_LEFT) {
            //if sender is not me - set username and avatar
            if (fullNames != null && fullNames.get(messageEntity.getSender()) != null) {
                holder.textViewUsername.setText(fullNames.get(messageEntity.getSender()));
            } else {
                holder.textViewUsername.setText(R.string.no_name);
            }
            if (avatarUrls != null && avatarUrls.get(messageEntity.getSender()) != null) {
                //TODO to load pictures from URL (from Firebase Storage)
                Glide.with(context).load(avatarUrls.get(messageEntity.getSender())).circleCrop()
                        .into(holder.avatar);
            } else {
                Glide.with(context).load(R.drawable.no_photo).circleCrop()
                        .into(holder.avatar);
            }
        }
    }

    @Override
    public int getItemCount() {
        return listOfMessages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewUsername;
        TextView textViewMessage;
        ImageView avatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUsername = itemView.findViewById(R.id.tv_username);
            textViewMessage = itemView.findViewById(R.id.tv_messagetext);
            avatar = itemView.findViewById(R.id.imgview_sender_avatar);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (listOfMessages.get(position).getSender().equals(currentUserId)) {
            return MSG_TYPE_RIGHT;
        } else return MSG_TYPE_LEFT;
    }
}

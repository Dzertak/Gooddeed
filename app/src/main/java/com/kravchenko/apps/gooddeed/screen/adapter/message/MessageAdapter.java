package com.kravchenko.apps.gooddeed.screen.adapter.message;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private final ChatRoom currentChatRoom;
    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;
    private final String currentUserId;
    private final HashMap<String, String> fullNames;
    private final HashMap<String, String> avatarUrls;
    private final Context context;
    private static final String TAG = "gooddeed_tag";
    private String previousDate;
    private String previousSender;

    public MessageAdapter(ChatRoom currentChatRoom, HashMap<String, String> fullNames, HashMap<String, String> avatarUrls, Context context) {
        this.fullNames = fullNames;
        this.avatarUrls = avatarUrls;
        this.currentChatRoom = currentChatRoom;
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
        //TODO add day and time
        MessageEntity messageEntity = currentChatRoom.getListOfMessages().get(position);
        holder.textViewMessage.setText(messageEntity.getTextOfMessage());
        Date date = new Date(messageEntity.getTimeInMillis());
        if (previousDate!=null && previousDate.equals(DateFormat.getDateInstance().format(date))){
            holder.textViewDay.setVisibility(View.GONE);
        }else{
            holder.textViewDay.setText(DateFormat.getDateInstance().format(date));
        }
        holder.textViewTime.setText(DateFormat.getTimeInstance().format(date));
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
            if (previousSender!=null && previousSender.equals(messageEntity.getSender())){
                holder.linearLayoutOfUser.setVisibility(View.GONE);
            }
        }
        previousDate = DateFormat.getDateInstance().format(date);
        previousSender = messageEntity.getSender();
    }

    @Override
    public int getItemCount() {
        return currentChatRoom.getListOfMessages().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewUsername, textViewMessage, textViewDay, textViewTime;
        ImageView avatar;
        LinearLayout linearLayoutOfUser;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayoutOfUser = itemView.findViewById(R.id.linearlayout_user_in_chat);
            textViewUsername = itemView.findViewById(R.id.tv_username);
            textViewMessage = itemView.findViewById(R.id.tv_messagetext);
            textViewDay = itemView.findViewById(R.id.tv_day);
            textViewTime = itemView.findViewById(R.id.tv_time);
            avatar = itemView.findViewById(R.id.imgview_sender_avatar);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (currentChatRoom.getListOfMessages().get(position).getSender().equals(currentUserId)) {
            return MSG_TYPE_RIGHT;
        } else return MSG_TYPE_LEFT;
    }
}

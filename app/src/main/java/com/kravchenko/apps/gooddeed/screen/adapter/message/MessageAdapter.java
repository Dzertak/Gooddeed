package com.kravchenko.apps.gooddeed.screen.adapter.message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.kravchenko.apps.gooddeed.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<MessageEntity> listOfMessages;
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private String currentUserId;

    public MessageAdapter(List<MessageEntity> listOfMessages) {
        this.listOfMessages = listOfMessages;
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType==MSG_TYPE_RIGHT){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item_right, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item_left, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        MessageEntity messageEntity = listOfMessages.get(position);
        holder.textViewMessage.setText(messageEntity.getTextOfMessage());
        if (getItemViewType(position)==MSG_TYPE_LEFT)holder.textViewUsername.setText(messageEntity.getSender());
        //holder.avatar....;

    }

    @Override
    public int getItemCount() {
        return listOfMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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

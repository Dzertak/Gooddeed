package com.kravchenko.apps.gooddeed.screen.adapter.message;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.database.entity.PersonWrapper;
import com.kravchenko.apps.gooddeed.util.annotation.InitiativeType;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.ViewHolder> {

    private final ArrayList<PersonWrapper> executors;
    private final boolean isUserAnOwner;
    private final Context context;
    private final String currentInitiativeId;
    private final int executorsCount;
    private final String type;

    public MembersAdapter(ArrayList<PersonWrapper> executors, boolean isUserAnOwner, Context context,
                          String currentInitiativeId, int executorsCount, String type) {
        this.executors = executors;
        this.isUserAnOwner = isUserAnOwner;
        this.context = context;
        this.currentInitiativeId = currentInitiativeId;
        this.executorsCount = executorsCount;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (executors != null) {
            if (executors.get(position).getImageUrl() != null)
                Glide.with(holder.itemView).load(Uri.parse(executors.get(position).getImageUrl())).into(holder.personAvatar);
            if (executors.get(position).getPersonName() != null)
                holder.personName.setText(executors.get(position).getPersonName());
            //TODO what is the difference between GROUP and UNLIMITED
            if (isUserAnOwner) {
                switch (type) {
                    case InitiativeType.SINGLE:
                        if (executorsCount == 0) {
                            holder.itemView.setOnClickListener(v -> {
                                PopupMenu popupMenu = new PopupMenu(context, holder.itemView);
                                popupMenu.getMenuInflater().inflate(R.menu.members_menu, popupMenu.getMenu());
                                popupMenu.setOnMenuItemClickListener(item -> {
                                    if (item.getItemId() == R.id.action_appoint_as_executor) {
                                        appointAsExecutor(executors.get(position).getPersonId());
                                    }
                                    return true;
                                });
                                popupMenu.show();
                            });
                        }
                        break;
                    case InitiativeType.GROUP:
                    case InitiativeType.UNLIMITED:
                        holder.itemView.setOnClickListener(v -> {
                            PopupMenu popupMenu = new PopupMenu(context, holder.itemView);
                            popupMenu.getMenuInflater().inflate(R.menu.members_menu, popupMenu.getMenu());
                            popupMenu.setOnMenuItemClickListener(item -> {
                                if (item.getItemId() == R.id.action_appoint_as_executor) {
                                    appointAsExecutor(executors.get(position).getPersonId());
                                }
                                return true;
                            });
                            popupMenu.show();
                        });
                        break;
                }
            }
        }
    }

    private void appointAsExecutor(String personId) {
        ArrayList<String> executors = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Initiatives").child(currentInitiativeId)
                .child("executors");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    executors.add((String) dataSnapshot.getValue());
                }
                if (!executors.contains(personId)) {
                    executors.add(personId);
                    reference.setValue(executors);
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return executors.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView personName;
        CircleImageView personAvatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            personAvatar = itemView.findViewById(R.id.person_avatar);
            personName = itemView.findViewById(R.id.person_name);
        }
    }
}

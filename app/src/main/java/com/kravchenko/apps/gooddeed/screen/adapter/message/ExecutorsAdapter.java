package com.kravchenko.apps.gooddeed.screen.adapter.message;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.database.entity.PersonWrapper;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ExecutorsAdapter extends RecyclerView.Adapter<ExecutorsAdapter.ViewHolder> {

    private final ArrayList<PersonWrapper> executors;
    private final boolean isUserAnOwner;
    private final Context context;
    private final String initiativeId;

    public ExecutorsAdapter(ArrayList<PersonWrapper> executors, boolean isUserAnOwner, Context context, String initiativeId) {
        this.executors = executors;
        this.isUserAnOwner = isUserAnOwner;
        this.context = context;
        this.initiativeId = initiativeId;
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
            if (isUserAnOwner) {
                holder.itemView.setOnClickListener(v -> {
                    PopupMenu popupMenu = new PopupMenu(context, holder.itemView);
                    popupMenu.getMenuInflater().inflate(R.menu.executor_menu, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(item -> {
                        switch (item.getItemId()) {
                            case (R.id.action_rate): {
                                Bundle args = new Bundle();
                                args.putString("person_id", executors.get(position).getPersonId());
                                args.putString("initiative_id", initiativeId);
                                Navigation.findNavController(v).navigate(R.id.action_chatInfoFragment_to_ratingFragment, args);
                                break;
                            }
                            case (R.id.action_remove_executor): {
                                removeExecutor(executors.get(position).getPersonId());
                                break;
                            }
                        }
                        return true;
                    });
                    popupMenu.show();
                });
            }
        }
    }

    private void removeExecutor(String personId) {
        ArrayList<String> executors = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Initiatives").child(initiativeId)
                .child("executors");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    executors.add((String) dataSnapshot.getValue());
                }
                if (executors.contains(personId)) {
                    executors.remove(personId);
                    reference.setValue(executors);
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

package com.kravchenko.apps.gooddeed.screen.adapter.myinitiatives;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.database.entity.Initiative;
import com.kravchenko.apps.gooddeed.databinding.ItemRvInitiativeBinding;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class InitiativeRecyclerAdapter extends RecyclerView.Adapter<InitiativeRecyclerAdapter.ViewHolder> {

    private final Context context;
    private List<Initiative> initiativeList;
    private OnInitiativeRecyclerClickListener listener;

    public InitiativeRecyclerAdapter(@NonNull List<Initiative> initiativeList, @NonNull Context context) {
        this.initiativeList = initiativeList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRvInitiativeBinding binding = ItemRvInitiativeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        ViewHolder viewHolder = new ViewHolder(binding);

        binding.getRoot().setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(v, viewHolder.getAdapterPosition());
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Initiative item = initiativeList.get(position);
        holder.binding.getRoot().setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString("initiative_id", item.getInitiativeId());
            Navigation.findNavController(v).navigate(R.id.action_myInitiativesFragment_to_currentInitiativeFragment, args);
        });
        holder.binding.tvTitle.setText(item.getTitle());
        holder.binding.tvDescription.setText(item.getDescription());
        if (item.getTimestamp() != 0) {
            Date date = new Date(item.getTimestamp());
            String dateTimeText = DateFormat.getDateInstance().format(date) + " " + DateFormat.getTimeInstance().format(date);
            holder.binding.dateAndTime.setText(dateTimeText);
        }
        if (item.getImgUri() != null && !item.getImgUri().isEmpty()) {
            Glide.with(context)
                    .load(item.getImgUri()).circleCrop()
                    .into(holder.binding.ivImage);
        } else {
            Glide.with(context)
                    .load(R.drawable.gooddeed_logo).circleCrop()
                    .into(holder.binding.ivImage);
        }
    }

    @Override
    public int getItemCount() {
        return initiativeList.size();
    }

    public OnInitiativeRecyclerClickListener getListener() {
        return listener;
    }

    public void setListener(OnInitiativeRecyclerClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<Initiative> initiatives) {
        this.initiativeList = initiatives;
    }

    public List<Initiative> getInitiatives() {
        return initiativeList;
    }

    public interface OnInitiativeRecyclerClickListener {
        void onItemClick(View v, int position);
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        ItemRvInitiativeBinding binding;

        public ViewHolder(@NonNull ItemRvInitiativeBinding itemBinding) {
            super(itemBinding.getRoot());
            binding = itemBinding;
        }
    }
}

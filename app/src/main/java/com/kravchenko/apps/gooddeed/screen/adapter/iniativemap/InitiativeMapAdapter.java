package com.kravchenko.apps.gooddeed.screen.adapter.iniativemap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.database.entity.Initiative;
import com.kravchenko.apps.gooddeed.databinding.ItemInitiativeMapBinding;
import com.kravchenko.apps.gooddeed.util.TimeUtil;
import com.kravchenko.apps.gooddeed.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class InitiativeMapAdapter extends RecyclerView.Adapter<InitiativeMapAdapter.ViewHolder> {

    private Context context;
    private List<Initiative> initiatives;


    public InitiativeMapAdapter(Context context, List<Initiative> initiatives) {
        this.context = context;
        this.initiatives = initiatives;
    }

    public InitiativeMapAdapter() {
        initiatives = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemInitiativeMapBinding binding = ItemInitiativeMapBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Initiative initiativeItem = initiatives.get(position);
        holder.binding.tvInitiativeTitle.setText(initiativeItem.getTitle());
        holder.binding.tvInitiativeDescription.setText(initiativeItem.getDescription());
        holder.binding.tvInitiativeTime.setText(TimeUtil.convertToDisplayTime(initiativeItem.getTimestamp()));
        holder.binding.imgCategory.setImageResource(Utils.getIconForCategory(initiativeItem.getCategoryId()));

        if (initiativeItem.getImgUri() != null && !initiativeItem.getImgUri().isEmpty()) {
            Glide.with(holder.binding.getRoot())
                    .load(initiativeItem.getImgUri())
                    .fallback(R.drawable.ic_gooddeed_logo)
                    .into(holder.binding.imgInitiative);
        } else {
            holder.binding.imgInitiative.setImageResource(R.drawable.gooddeed_logo);
        }
    }

    @Override
    public int getItemCount() {
        return initiatives.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        ItemInitiativeMapBinding binding;

        public ViewHolder(@NonNull ItemInitiativeMapBinding itemBinding) {
            super(itemBinding.getRoot());
            binding = itemBinding;
        }
    }

    public void setItems(List<Initiative> initiatives) {
        this.initiatives = initiatives;
    }

    public List<Initiative> getItems() {
        return initiatives;
    }
}

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
import com.kravchenko.apps.gooddeed.util.annotation.InitiativeType;

import java.util.ArrayList;
import java.util.List;

public class InitiativeMapAdapter extends RecyclerView.Adapter<InitiativeMapAdapter.ViewHolder> {

    private Context context;
    private List<Initiative> initiatives;
    private OnInitiativeClickListener listener;


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
        ViewHolder viewHolder = new ViewHolder(binding);
        binding.getRoot().setOnClickListener(v -> {
            if (listener != null) {
                listener.onInitiativeClick(initiatives.get(viewHolder.getAdapterPosition()), viewHolder.getAdapterPosition());
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Initiative initiativeItem = initiatives.get(position);
        holder.binding.tvInitiativeTitle.setText(initiativeItem.getTitle());
        holder.binding.tvInitiativeDescription.setText(initiativeItem.getDescription());
        holder.binding.tvInitiativeTime.setText(TimeUtil.convertToDisplayTime(initiativeItem.getTimestamp()));
        holder.binding.imgCategory.setImageResource(Utils.getIconForCategory(initiativeItem.getCategoryId()));
        if (InitiativeType.SINGLE.equals(initiativeItem.getType())) {
            holder.binding.imgType.setImageResource(R.drawable.ic_executor);
        } else if (InitiativeType.GROUP.equals(initiativeItem.getType())) {
            holder.binding.imgType.setImageResource(R.drawable.ic_executor_group);
        } else {
            holder.binding.imgType.setImageResource(R.drawable.ic_executor_unlimit);
        }

        if (initiativeItem.getImgUri() != null && !initiativeItem.getImgUri().isEmpty()) {
            Glide.with(holder.binding.getRoot())
                    .load(initiativeItem.getImgUri())
                    .fallback(R.drawable.ic_gooddeed_logo)
                    .into(holder.binding.imgInitiative);
        } else {
            holder.binding.imgInitiative.setImageResource(R.drawable.ic_gooddeed_logo);
        }
    }

    @Override
    public int getItemCount() {
        return initiatives.size();
    }

    public List<Initiative> getItems() {
        return initiatives;
    }

    public void setItems(List<Initiative> initiatives) {
        this.initiatives = initiatives;
    }

    public OnInitiativeClickListener getListener() {
        return this.listener;
    }

    public void setListener(OnInitiativeClickListener listener) {
        this.listener = listener;
    }

    public int getPositionByInitiativeId(String initiativeId) {
        for (Initiative initiative : initiatives) {
            if (initiativeId.equals(initiative.getInitiativeId()))
                return initiatives.indexOf(initiative);
        }
        return 0;
    }

    public String getInitiativeIdByPosition(int position) {
        return initiatives.get(position).getInitiativeId();
    }

    public interface OnInitiativeClickListener {

        void onInitiativeClick(Initiative initiative, int position);

    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        ItemInitiativeMapBinding binding;

        public ViewHolder(@NonNull ItemInitiativeMapBinding itemBinding) {
            super(itemBinding.getRoot());
            binding = itemBinding;
        }
    }
}

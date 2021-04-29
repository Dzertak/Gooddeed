package com.kravchenko.apps.gooddeed.screen.adapter.subscription;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.database.entity.Filter;
import com.kravchenko.apps.gooddeed.screen.adapter.filter.MainFilterRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final Context context;
    private List<String> subscriptions = new ArrayList<>();
    private static final int SUBSCRIPTION_TYPE = 0;
    private static final int SUBSCRIPTION_ADD_TYPE = 1;
    private boolean isEditable = false;


    public SubscriptionAdapter(Context context, List<String> subscriptions, boolean isEditable) {
        this.context = context;
        this.subscriptions = subscriptions;
        this.isEditable = isEditable;
        if (isEditable){
            //it's for make last item for adding new category
            subscriptions.add("");
        }
    }

    public void setSubscriptions(List<String> subscriptions) {
        this.subscriptions = subscriptions;
        if (isEditable){
            //it's for make last item for adding new category
            subscriptions.add("");
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == SUBSCRIPTION_TYPE){
            return new SubscriptionAdapter.SubViewHolder(inflater.inflate(R.layout.item_subscription, parent, false));
        } else return new SubscriptionAdapter.SubAddViewHolder(inflater.inflate(R.layout.item_subscription_add, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        return position == subscriptions.size()-1 && isEditable ? SUBSCRIPTION_ADD_TYPE : SUBSCRIPTION_TYPE;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == SUBSCRIPTION_TYPE){
            SubViewHolder viewHolderSub = (SubViewHolder) holder;
            viewHolderSub.textViewTitle.setText(subscriptions.get(position));
            if (!isEditable){
                viewHolderSub.imageViewRemove.setVisibility(View.GONE);
            } else {
                viewHolderSub.imageViewRemove.setOnClickListener(t -> {
                    Toast.makeText(context, "Remove Subscription", Toast.LENGTH_SHORT).show();
                });
            }
        } else {
            SubAddViewHolder viewHolderSub = (SubAddViewHolder) holder;
            viewHolderSub.textViewAdd.setOnClickListener(t -> Toast.makeText(context, "Add Category", Toast.LENGTH_SHORT).show());
        }
    }

    @Override
    public int getItemCount() {
        return subscriptions.size();
    }

    private static class SubViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewTitle;
        private final ImageView imageViewRemove;

        public SubViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_subscription_title);
            imageViewRemove = itemView.findViewById(R.id.image_view_subscription_remove);
        }
    }

    private static class SubAddViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewAdd;
        private final ImageView imageViewRemove;

        public SubAddViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAdd = itemView.findViewById(R.id.text_view_subscription_add);
            imageViewRemove = itemView.findViewById(R.id.image_view_subscription_remove);
        }
    }
}

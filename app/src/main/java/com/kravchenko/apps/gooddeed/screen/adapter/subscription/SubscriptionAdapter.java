package com.kravchenko.apps.gooddeed.screen.adapter.subscription;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.database.entity.Filter;
import com.kravchenko.apps.gooddeed.screen.adapter.filter.MainFilterRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private List<String> subscriptions = new ArrayList<>();


    public SubscriptionAdapter(Context context) {
        this.context = context;
        subscriptions.add("Уборка територий");
        subscriptions.add("Массаж");
        subscriptions.add("Ремонт техники");
        subscriptions.add("Иностранные языки");
        subscriptions.add("Услуги психолога или психотерапевта");
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_subscription, parent, false);
        return new SubscriptionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolderSub = (ViewHolder) holder;
        viewHolderSub.textViewTitle.setText(subscriptions.get(position));
    }

    @Override
    public int getItemCount() {
        return subscriptions.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_subscription_title);

        }
    }
}

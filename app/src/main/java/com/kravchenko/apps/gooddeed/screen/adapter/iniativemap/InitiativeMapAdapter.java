package com.kravchenko.apps.gooddeed.screen.adapter.iniativemap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.screen.adapter.subscription.SubscriptionAdapter;

import java.util.ArrayList;
import java.util.List;

public class InitiativeMapAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final Context context;
    private List<String> initiatives = new ArrayList<>();


    public InitiativeMapAdapter(Context context, List<String> initiatives) {
        this.context = context;
        this.initiatives = initiatives;

    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new InitiativeMapAdapter.InitiativeMapViewHolder(inflater.inflate(R.layout.item_initiative_map, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    private static class InitiativeMapViewHolder extends RecyclerView.ViewHolder {
        public InitiativeMapViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}

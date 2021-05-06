package com.kravchenko.apps.gooddeed.screen.adapter.myinitiatives;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kravchenko.apps.gooddeed.databinding.ItemViewPagerContainerBinding;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.PageHolder> {

    private final InitiativeRecyclerAdapter[] listAdapters;
    private final Context context;

    public ViewPagerAdapter(@NonNull InitiativeRecyclerAdapter[] listAdapters, @NonNull Context context) {
        this.listAdapters = listAdapters;
        this.context = context;
    }

    @NonNull
    @Override
    public PageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PageHolder(ItemViewPagerContainerBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PageHolder holder, int position) {
        holder.binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.binding.recyclerView.setAdapter(listAdapters[position]);
    }

    @Override
    public int getItemCount() {
        return listAdapters.length;
    }

    protected static class PageHolder extends RecyclerView.ViewHolder {

        ItemViewPagerContainerBinding binding;

        public PageHolder(@NonNull ItemViewPagerContainerBinding itemBinding) {
            super(itemBinding.getRoot());
            binding = itemBinding;
        }
    }
}

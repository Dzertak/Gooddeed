package com.kravchenko.apps.gooddeed.screen.adapter.filter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.database.entity.Filter;

import java.util.ArrayList;
import java.util.List;


public class MainFilterRecyclerViewAdapter extends RecyclerView.Adapter<MainFilterRecyclerViewAdapter.ViewHolder> {
    private final Context context;
    private List<Filter> filters = new ArrayList<>();
    private OnItemClickListener listener;

    public MainFilterRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_filter_list, parent, false);
        return new MainFilterRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Filter filter = filters.get(position);
        holder.textViewSelectedValue.setText(filter.getSelectedValue());
        holder.textViewFilterName.setText(context.getString(filter.getFilterName()));
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
        notifyDataSetChanged();
    }

    public Filter getFilterAt(int position) {
        return filters.get(position);
    }

    @Override
    public int getItemCount() {
        return filters.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewFilterName;
        private final TextView textViewSelectedValue;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewFilterName = itemView.findViewById(R.id.textView_listItem_filterName);
            textViewSelectedValue = itemView.findViewById(R.id.textView_listItem_selectedValue);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(filters.get(position));
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Filter filter);
    }
}

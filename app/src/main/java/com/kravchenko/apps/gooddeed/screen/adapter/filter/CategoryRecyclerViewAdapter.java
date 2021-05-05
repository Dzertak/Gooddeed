package com.kravchenko.apps.gooddeed.screen.adapter.filter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.database.entity.category.Category;
import com.kravchenko.apps.gooddeed.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.ViewHolder> {
    private final Context context;
    private List<Category> categories;
    private OnItemClickListener listener;

    public CategoryRecyclerViewAdapter(Context context) {
        this.context = context;
        this.categories = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.textViewCategoryTitle.setText(Utils.getString(category.getTitle()));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public Category getCategoryAt(int position) {
        return categories.get(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewCategoryTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCategoryTitle = itemView.findViewById(R.id.textViewCategoryTitle);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(categories.get(position));
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Category category);
    }
}

package com.kravchenko.apps.gooddeed.screen.adapter.filter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.database.entity.category.CategoryType;
import com.kravchenko.apps.gooddeed.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class CategoryTypeRecyclerViewAdapter extends RecyclerView.Adapter<CategoryTypeRecyclerViewAdapter.ViewHolder> {
    private final Context context;
    private List<CategoryType> categoryTypes;
    private OnItemClickListener listener;

    public CategoryTypeRecyclerViewAdapter(Context context) {
        this.context = context;
        this.categoryTypes = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_category_type, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryType categoryType = categoryTypes.get(position);
        holder.textViewCategoryTitle.setText(Utils.getString(categoryType.getTitle()));
    }

    public void setCategoryTypes(List<CategoryType> categoryTypes) {
        this.categoryTypes = categoryTypes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return categoryTypes.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public CategoryType getCategoryTypeAt(int position) {
        return categoryTypes.get(position);
    }

    public interface OnItemClickListener {
        void onItemClick(CategoryType categoryType);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewCategoryTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCategoryTitle = itemView.findViewById(R.id.textView_categoryTypeTitle);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(categoryTypes.get(position));
                }
            });
        }
    }
}

package com.kravchenko.apps.gooddeed.screen.adapter.filter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.database.entity.category.Category;
import com.kravchenko.apps.gooddeed.database.entity.category.CategoryType;
import com.kravchenko.apps.gooddeed.database.entity.category.CategoryTypeWithCategories;
import com.kravchenko.apps.gooddeed.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryTypeRecyclerViewAdapter extends RecyclerView.Adapter<CategoryTypeRecyclerViewAdapter.ViewHolder> {
    private final Context context;
    private List<CategoryTypeWithCategories> selectedCategories;
    private Map<Long, Integer> categorySizes;
    private OnItemClickListener listener;

    public CategoryTypeRecyclerViewAdapter(Context context) {
        this.context = context;
        this.selectedCategories = new ArrayList<>();
        this.categorySizes = new HashMap<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_category_type, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryTypeWithCategories categoryTypeWithCategories = selectedCategories.get(position);
        CategoryType categoryType = selectedCategories.get(position).getCategoryType();
        List<Category> categories = categoryTypeWithCategories.getCategories();
        holder.textViewCategoryTitle.setText(Utils.getString(categoryType.getTitle()));
        Integer integer = categorySizes.get(categoryType.getCategoryTypeId());
        if (integer != null) {
            if (categories.size() == integer) {
                holder.textViewCheck.setText(R.string.all);
            } else if (categories.isEmpty()) {
                holder.textViewCheck.setText(Utils.getString(R.string.arrow_symbol));
            } else if (categories.size() == 1) {
                holder.textViewCheck.setText(Utils.getString(categories.get(0).getTitle()));
            } else {
                holder.textViewCheck.setText(Utils.getString(categories.get(0).getTitle()) + ",\n" + Utils.getString(categories.get(1).getTitle()) + ",");
            }
        }
    }

    public void setSelectedCategories(List<CategoryTypeWithCategories> selectedCategories) {
        if (selectedCategories != null){
            this.selectedCategories = selectedCategories;
            notifyDataSetChanged();
        }
    }

    public void setCategorySizes(Map<Long, Integer> categorySizes) {
        this.categorySizes = categorySizes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return selectedCategories.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(CategoryType categoryType);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewCategoryTitle;
        private final TextView textViewCheck;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCategoryTitle = itemView.findViewById(R.id.tvCategoryTypeTitle);
            textViewCheck = itemView.findViewById(R.id.tvCategoryTypeCheck);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(selectedCategories.get(position).getCategoryType());
                }
            });
        }
    }
}

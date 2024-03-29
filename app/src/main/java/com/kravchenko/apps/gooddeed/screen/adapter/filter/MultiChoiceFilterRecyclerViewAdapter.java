package com.kravchenko.apps.gooddeed.screen.adapter.filter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import androidx.annotation.NonNull;

import com.kravchenko.apps.gooddeed.database.entity.category.Category;
import com.kravchenko.apps.gooddeed.screen.filterwindow.FilterCallBack;
import com.kravchenko.apps.gooddeed.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class MultiChoiceFilterRecyclerViewAdapter extends BaseCategoryRecyclerViewAdapter {
    private List<Category> selectedCategories;
    private boolean isSelectAll;

    public MultiChoiceFilterRecyclerViewAdapter(Context context,
                                                FilterCallBack filterCallBack) {
        super(context, filterCallBack);
        selectedCategories = new ArrayList<>();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        category = categories.get(position);
        holder.textViewCategoryTitle.setText(Utils.getString(category.getTitle()));
        if (holder.getAdapterPosition() != check) {
            holder.itemView.setOnClickListener(v -> clickItem(holder));
            if (isSelectAll || selectedCategories.contains(category)) {
                holder.imageViewCheck.setVisibility(View.VISIBLE);
                holder.itemView.setBackgroundColor(Color.LTGRAY);
            } else {
                holder.imageViewCheck.setVisibility(View.GONE);
                holder.itemView.setBackgroundColor(Color.WHITE);
            }
        }
    }

    @Override
    public void setSelectedCategories(List<Category> selectedCategories) {
        this.selectedCategories = selectedCategories;
        notifyDataSetChanged();
    }

    @Override
    public void selectAll() {
        if (categories.size() == selectedCategories.size()) {
            isSelectAll = false;
            selectedCategories.clear();
        } else {
            isSelectAll = true;
            selectedCategories.clear();
            selectedCategories.addAll(categories);
        }
        filterCallBack.setSelectedCategories(selectedCategories, category.getCategoryOwnerId());
        notifyDataSetChanged();
    }

    @Override
    public void clickItem(ViewHolder holder) {
        Category category = categories.get(holder.getAdapterPosition());
        if (holder.imageViewCheck.getVisibility() == View.GONE) {
            holder.imageViewCheck.setVisibility(View.VISIBLE);
            holder.itemView.setBackgroundColor(Color.LTGRAY);
            selectedCategories.add(category);
        } else {
            holder.imageViewCheck.setVisibility(View.GONE);
            holder.itemView.setBackgroundColor(Color.WHITE);
            selectedCategories.remove(category);
        }
        filterCallBack.setSelectedCategories(selectedCategories, category.getCategoryOwnerId());
    }
}

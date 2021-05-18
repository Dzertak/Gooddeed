package com.kravchenko.apps.gooddeed.screen.adapter.filter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import androidx.annotation.NonNull;

import com.kravchenko.apps.gooddeed.database.entity.category.Category;
import com.kravchenko.apps.gooddeed.util.Utils;

import java.util.Arrays;
import java.util.List;

public class SingleChoseFilterRecyclerViewAdapter extends BaseCategoryRecyclerViewAdapter {
    private final Category[] selectedCategories;

    public SingleChoseFilterRecyclerViewAdapter(Context context, FilterCallBack filterCallBack) {
        super(context, filterCallBack);
        this.selectedCategories = new Category[1];
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        category = categories.get(position);
        holder.textViewCategoryTitle.setText(Utils.getString(category.getTitle()));
        if (holder.getAdapterPosition() != check) {
            holder.itemView.setOnClickListener(v -> clickItem(holder));

            if (selectedCategories[0] != null && selectedCategories[0].equals(category)) {
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
        if (selectedCategories != null && !selectedCategories.isEmpty()
                && selectedCategories.get(0) != null) {
            this.selectedCategories[0] = selectedCategories.get(0);
        }
        notifyDataSetChanged();
    }

    @Override
    public void clickItem(ViewHolder holder) {
        Category category = categories.get(holder.getAdapterPosition());
        if (holder.imageViewCheck.getVisibility() == View.GONE) {
            holder.imageViewCheck.setVisibility(View.VISIBLE);
            holder.itemView.setBackgroundColor(Color.LTGRAY);
            selectedCategories[0] = category;
        } else {
            holder.imageViewCheck.setVisibility(View.GONE);
            holder.itemView.setBackgroundColor(Color.WHITE);
            selectedCategories[0] = null;
        }
        filterCallBack.setSelectedCategories(Arrays.asList(selectedCategories.clone()),category.getCategoryOwnerId());
    }
}

package com.kravchenko.apps.gooddeed.screen.adapter.filter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.database.entity.category.Category;
import com.kravchenko.apps.gooddeed.util.Utils;
import com.kravchenko.apps.gooddeed.viewmodel.FilterViewModel;

import java.util.ArrayList;
import java.util.List;

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.ViewHolder> {
    private final Context context;
    private FilterViewModel filterViewModel;
    private List<Category> categories;
    private List<Category> selectedCategories;
    boolean isSelectAll;
    int check = -1;


    public CategoryRecyclerViewAdapter(Context context, FilterViewModel filterViewModel) {
        this.context = context;
        this.filterViewModel = filterViewModel;
        this.categories = new ArrayList<>();
        this.selectedCategories = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.textViewCategoryTitle.setText(Utils.getString(category.getTitle()));
        if (holder.getAdapterPosition() != check) {
            holder.itemView.setOnClickListener(v -> clickItem(holder));
            if (isSelectAll) {
                holder.imageViewCheck.setVisibility(View.VISIBLE);
                holder.itemView.setBackgroundColor(Color.LTGRAY);
            } else {
                holder.imageViewCheck.setVisibility(View.GONE);
                holder.itemView.setBackgroundColor(Color.WHITE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    public void selectAll() {
        if (categories.size() == selectedCategories.size()) {
            isSelectAll = false;
            selectedCategories.forEach(category ->
                    filterViewModel.removeSelectedCategory(category)
            );
            selectedCategories.clear();

        } else {
            isSelectAll = true;
            selectedCategories.clear();
            selectedCategories.addAll(categories);
            selectedCategories.forEach(category ->
                    filterViewModel.addSelectedCategory(category)
            );

        }
        filterViewModel.setSelectedCategories(selectedCategories);
        notifyDataSetChanged();
    }

    private void clickItem(ViewHolder holder) {
        Category category = categories.get(holder.getAdapterPosition());
        if (holder.imageViewCheck.getVisibility() == View.GONE) {
            holder.imageViewCheck.setVisibility(View.VISIBLE);
            holder.itemView.setBackgroundColor(Color.LTGRAY);
            selectedCategories.add(category);
            filterViewModel.addSelectedCategory(category);
        } else {
            holder.imageViewCheck.setVisibility(View.GONE);
            holder.itemView.setBackgroundColor(Color.WHITE);
            selectedCategories.remove(category);
            filterViewModel.removeSelectedCategory(category);
        }
        filterViewModel.setSelectedCategories(selectedCategories);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewCategoryTitle;
        private final ImageView imageViewCheck;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCategoryTitle = itemView.findViewById(R.id.textViewCategoryTitle);
            imageViewCheck = itemView.findViewById(R.id.imageViewCheckBox);
        }
    }

}
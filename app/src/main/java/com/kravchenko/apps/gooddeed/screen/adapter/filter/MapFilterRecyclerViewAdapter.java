package com.kravchenko.apps.gooddeed.screen.adapter.filter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.database.entity.category.Category;
import com.kravchenko.apps.gooddeed.util.Utils;
import com.kravchenko.apps.gooddeed.viewmodel.FilterViewModel;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MapFilterRecyclerViewAdapter extends RecyclerView.Adapter<MapFilterRecyclerViewAdapter.ViewHolder> {
    private final Context context;
    private final FilterViewModel filterViewModel;
    private List<Category> categories;
    private List<Category> selectedCategories;
    private Category category;
    private boolean isSelectAll;
    private final int check;

    public MapFilterRecyclerViewAdapter(Context context, FilterViewModel filterViewModel) {
        this.context = context;
        this.categories = new ArrayList<>();
        this.selectedCategories = new ArrayList<>();
        this.filterViewModel = filterViewModel;
        check = -1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { //TODO take out in base adapter
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_category, parent, false);
        return new MapFilterRecyclerViewAdapter.ViewHolder(view);
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
    public int getItemCount() {
        return categories.size();
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    public void setSelectedCategories(List<Category> selectedCategories) {
        this.selectedCategories = selectedCategories;
        notifyDataSetChanged();
    }


    public void selectAll() {
        if (categories.size() == selectedCategories.size()) {
            isSelectAll = false;
            selectedCategories.clear();

        } else {
            isSelectAll = true;
            selectedCategories.clear();
            selectedCategories.addAll(categories);
        }
        filterViewModel.setMapSelectedCategoriesLiveData(selectedCategories, category.getCategoryOwnerId());
        notifyDataSetChanged();
    }

    private void clickItem(ViewHolder holder) {
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
        filterViewModel.setMapSelectedCategoriesLiveData(selectedCategories, category.getCategoryOwnerId());
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
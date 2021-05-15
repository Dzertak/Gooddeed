package com.kravchenko.apps.gooddeed.screen.adapter.filter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.database.entity.category.Category;
import com.kravchenko.apps.gooddeed.viewmodel.FilterViewModel;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseCategoryRecyclerViewAdapter extends RecyclerView.Adapter<BaseCategoryRecyclerViewAdapter.ViewHolder> {
    protected final Context context;
    protected final FilterViewModel filterViewModel;
    protected List<Category> categories;
    protected final int check;
    protected Category category;

    public BaseCategoryRecyclerViewAdapter(Context context, FilterViewModel filterViewModel) {
        this.context = context;
        this.categories = new ArrayList<>();
        this.filterViewModel = filterViewModel;
        this.check = -1;
    }

    @Override
    public abstract void onBindViewHolder(@NonNull ViewHolder holder, int position);

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_category, parent, false);
        return new BaseCategoryRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    public abstract void setSelectedCategories(List<Category> selectedCategories);

    public abstract void clickItem(ViewHolder holder);

    class ViewHolder extends RecyclerView.ViewHolder {
        protected final TextView textViewCategoryTitle;
        protected final ImageView imageViewCheck;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCategoryTitle = itemView.findViewById(R.id.textViewCategoryTitle);
            imageViewCheck = itemView.findViewById(R.id.imageViewCheckBox);
        }
    }


}

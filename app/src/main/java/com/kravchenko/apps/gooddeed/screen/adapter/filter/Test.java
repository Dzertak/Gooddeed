package com.kravchenko.apps.gooddeed.screen.adapter.filter;

import android.content.Context;

import androidx.annotation.NonNull;

import com.kravchenko.apps.gooddeed.database.entity.category.Category;
import com.kravchenko.apps.gooddeed.viewmodel.FilterViewModel;

import java.util.List;

public class Test extends BaseCategoryRecyclerViewAdapter{

    public Test(Context context, FilterViewModel filterViewModel) {
        super(context, filterViewModel);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public void setSelectedCategories(List<Category> selectedCategories) {

    }

    @Override
    public void clickItem(ViewHolder holder) {

    }


}

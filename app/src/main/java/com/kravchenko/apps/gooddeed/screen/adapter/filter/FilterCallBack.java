package com.kravchenko.apps.gooddeed.screen.adapter.filter;

import com.kravchenko.apps.gooddeed.database.entity.category.Category;

import java.util.List;

public interface FilterCallBack {
    void setSelectedCategories(List<Category> categories, long categoryOwnerId);
}

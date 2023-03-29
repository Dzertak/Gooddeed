package com.kravchenko.apps.gooddeed.screen.profile;

import com.kravchenko.apps.gooddeed.database.entity.category.Category;

import java.util.List;

public interface SubscriptionsCallback {
    void addSubscription();
    void removeSubscription(Category category);
}

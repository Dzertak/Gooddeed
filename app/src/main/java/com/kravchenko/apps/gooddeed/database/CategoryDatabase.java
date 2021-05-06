package com.kravchenko.apps.gooddeed.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.kravchenko.apps.gooddeed.AppInstance;
import com.kravchenko.apps.gooddeed.database.dao.CategoryDao;
import com.kravchenko.apps.gooddeed.database.entity.category.Category;
import com.kravchenko.apps.gooddeed.database.entity.category.CategoryType;


@Database(entities = {
        Category.class,
        CategoryType.class},
        version = 1
)
public abstract class CategoryDatabase extends RoomDatabase {
    private static final String DB_NAME = "category_database";
    private static volatile CategoryDatabase instance;

    public abstract CategoryDao categoryDao();

    public static synchronized CategoryDatabase getInstance() {
        if (instance == null) {
            instance = Room.databaseBuilder(AppInstance.getAppContext(),
                   CategoryDatabase.class, DB_NAME)
                    .build();
        }
        return instance;
    }
}

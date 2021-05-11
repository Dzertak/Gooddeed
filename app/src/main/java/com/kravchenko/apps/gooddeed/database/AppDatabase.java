package com.kravchenko.apps.gooddeed.database;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.kravchenko.apps.gooddeed.AppInstance;
import com.kravchenko.apps.gooddeed.database.dao.CategoryDao;
import com.kravchenko.apps.gooddeed.database.entity.category.Category;
import com.kravchenko.apps.gooddeed.database.entity.category.CategoryType;
import com.kravchenko.apps.gooddeed.database.entity.category.CategoryTypeWithCategories;
import com.kravchenko.apps.gooddeed.util.FillHelper;

import java.util.List;

import static com.kravchenko.apps.gooddeed.repository.CategoryRepository.databaseWriteExecutor;


@Database(entities = {
        Category.class,
        CategoryType.class},
        version = 1
)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DB_NAME = "app_database";
    private static volatile AppDatabase instance;

    public abstract CategoryDao categoryDao();

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static synchronized AppDatabase getInstance() {
        if (instance == null) {
            instance = Room.databaseBuilder(AppInstance.getAppContext(),
                    AppDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .addCallback(sRoomDatabaseCallback).build();
        }
        return instance;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            List<CategoryTypeWithCategories> categoryTypesWithCategories
                    = FillHelper.getCategoryTypeWithCategories();
            categoryTypesWithCategories.forEach(categoryTypeWithCategories ->
                    databaseWriteExecutor.execute(() ->
                            instance.categoryDao()
                                    .insertCategoryTypeWithCategories(categoryTypeWithCategories))

            );
        }


    };
}

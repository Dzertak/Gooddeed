package com.kravchenko.apps.gooddeed.util;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.database.entity.category.Category;
import com.kravchenko.apps.gooddeed.database.entity.category.CategoryType;
import com.kravchenko.apps.gooddeed.database.entity.category.CategoryTypeWithCategories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FillHelper {
    public static int[] categoryTypesTitles = {
            R.string.beauty_and_health_title,
            R.string.charity_title,
            R.string.cleaning_title,
            R.string.delivery_and_transportation_title,
            R.string.photo_and_video_title,
            R.string.repair_and_construction_title,
            R.string.tutoring_and_training_title
    };
    public static int[] categoryTypesDescriptions = {
            R.string.beauty_and_health_desc,
            R.string.charity_desc,
            R.string.cleaning_desc,
            R.string.delivery_and_transportation_desc,
            R.string.photo_and_video_desc,
            R.string.repair_and_construction_desc,
            R.string.tutoring_and_training_desc
    };
    public static int[] tutoring_and_trainingCategoryTitles = {
            R.string.art_title,
            R.string.foreign_languages_title,
            R.string.programming_title,
            R.string.science_title,
            R.string.sport_title
    };
    public static int[] tutoring_and_trainingCategoryDesc = {
            R.string.art_desc,
            R.string.foreign_languages_desc,
            R.string.programming_desc,
            R.string.science_desc,
            R.string.sport_desc
    };

    public static int[] repair_and_constructionCategoryTitles = {
            R.string.building_title,
            R.string.electrical_title,
            R.string.handyman_for_hour_title,
            R.string.plumbing_title,
            R.string.repair_and_construction_title,
            R.string.vehicle_repair_title
    };
    public static int[] repair_and_constructionCategoryDesc = {
            R.string.building_desc,
            R.string.electrical_desc,
            R.string.handyman_for_hour_desc,
            R.string.plumbing_desc,
            R.string.repair_and_construction_desc,
            R.string.vehicle_repair_desc
    };
    public static int[] photo_and_videoTitle = {
            R.string.filming_title,
            R.string.photoshoot_title
    };
    public static int[] photo_and_videoDesc = {
            R.string.filming_desc,
            R.string.photoshoot_desc
    };
    public static int[] delivery_and_transportationTitle = {
            R.string.cargo_transportation_title,
            R.string.courier_title,
            R.string.taxi_title
    };
    public static int[] delivery_and_transportationDesc = {
            R.string.cargo_transportation_desc,
            R.string.courier_desc,
            R.string.taxi_desc
    };
    public static int[] cleaningTitle = {
            R.string.yard_cleaning_title,
            R.string.house_cleaning_title
    };
    public static int[] cleaningDesc = {
            R.string.yard_cleaning_desc,
            R.string.house_cleaning_desc
    };
    public static int[] charityTitle = {
            R.string.donations_title,
            R.string.help_those_in_need_title
    };
    public static int[] charityDesc = {
            R.string.donations_desc,
            R.string.help_those_in_need_desc
    };
    public static int[] beauty_and_healthTitle = {
            R.string.cosmetologist_services_title,
            R.string.hairdressing_title,
            R.string.massage_title,
            R.string.psychologists_title
    };
    public static int[] beauty_and_healthDesc = {
            R.string.cosmetologist_services_desc,
            R.string.hairdressing_desc,
            R.string.massage_desc,
            R.string.psychologists_desc
    };
    public static String[] pathes = {
            "category-types/beauty-and-health/categories",
            "category-types/charity/categories",
            "category-types/cleaning/categories",
            "category-types/delivery-and-transportation/categories",
            "category-types/photo-and-video/categories",
            "category-types/repair-and-construction/categories",
            "category-types/tutoring-and-training/categories",
    };

    public static void fillDB() {
        fillCategoryTypes();
        fillCategories(pathes[0], beauty_and_healthTitle, beauty_and_healthDesc);
        fillCategories(pathes[1], charityTitle, charityDesc);
        fillCategories(pathes[2], cleaningTitle, charityDesc);
        fillCategories(pathes[3], delivery_and_transportationTitle, delivery_and_transportationDesc);
        fillCategories(pathes[4], photo_and_videoTitle, photo_and_videoDesc);
        fillCategories(pathes[5], repair_and_constructionCategoryTitles, repair_and_constructionCategoryDesc);
        fillCategories(pathes[6], tutoring_and_trainingCategoryTitles, tutoring_and_trainingCategoryDesc);
    }

    public static void fillCategories(String path, int[] titles, int[] desc) {
        CollectionReference categoryTypesRef = FirebaseFirestore.getInstance()
                .collection(path);
        categoryTypesRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<DocumentSnapshot> categoryTypesSnapshots = queryDocumentSnapshots.getDocuments();
            for (int i = 0; i < titles.length; i++) {
                DocumentSnapshot documentSnapshot = categoryTypesSnapshots.get(i);
                Map<String, Object> dataToSave = new HashMap<>();
                dataToSave.put("title", titles[i]);
                dataToSave.put("description", desc[i]);
                documentSnapshot.getReference().set(dataToSave);
            }
        });
    }

    public static void fillCategoryTypes() {
        CollectionReference categoryTypesRef = FirebaseFirestore.getInstance()
                .collection("category-types");
        categoryTypesRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<DocumentSnapshot> categoryTypesSnapshots = queryDocumentSnapshots.getDocuments();
            for (int i = 0; i < FillHelper.categoryTypesDescriptions.length; i++) {
                DocumentSnapshot documentSnapshot = categoryTypesSnapshots.get(i);
                Map<String, Object> dataToSave = new HashMap<>();
                dataToSave.put("title", FillHelper.categoryTypesTitles[i]);
                dataToSave.put("description", FillHelper.categoryTypesDescriptions[i]);
                documentSnapshot.getReference().set(dataToSave);
            }
        });
    }

    public static List<CategoryTypeWithCategories> getCategoryTypeWithCategories() {
        List<CategoryTypeWithCategories> categoryTypesWithCategories = new ArrayList<>();
        for (int i = 0; i < categoryTypesTitles.length; i++) {
            CategoryTypeWithCategories categoryTypeWithCategories = new CategoryTypeWithCategories();

            CategoryType categoryType = new CategoryType(categoryTypesTitles[i], categoryTypesDescriptions[i]);
            categoryTypeWithCategories.setCategoryType(categoryType);

            List<Category> categories = new ArrayList<>();
            for (int j = 0; j < categoryValues[i].titles.length; j++) {
                Category category = new Category(categoryValues[i].titles[j], categoryValues[i].descriptions[j]);
                categories.add(category);
            }

            categoryTypeWithCategories.setCategories(categories);
            categoryTypesWithCategories.add(categoryTypeWithCategories);
        }
        return categoryTypesWithCategories;
    }

    static CategoryValues[] categoryValues = {
            new CategoryValues(beauty_and_healthTitle, beauty_and_healthDesc),
            new CategoryValues(charityTitle, charityDesc),
            new CategoryValues(cleaningTitle, charityDesc),
            new CategoryValues(delivery_and_transportationTitle, delivery_and_transportationDesc),
            new CategoryValues(photo_and_videoTitle, photo_and_videoDesc),
            new CategoryValues(repair_and_constructionCategoryTitles, repair_and_constructionCategoryDesc),
            new CategoryValues(tutoring_and_trainingCategoryTitles, tutoring_and_trainingCategoryDesc)
    };

    static class CategoryValues {
        int[] titles;
        int[] descriptions;

        public CategoryValues(int[] titles, int[] descriptions) {
            this.titles = titles;
            this.descriptions = descriptions;
        }
    }
}

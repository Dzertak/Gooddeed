package com.kravchenko.apps.gooddeed.util;

import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.database.entity.category.Category;
import com.kravchenko.apps.gooddeed.database.entity.category.CategoryType;
import com.kravchenko.apps.gooddeed.database.entity.category.CategoryTypeWithCategories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FillHelper {
    public static Map<String, Integer> getStringKeys() {
        Map<String, Integer> stringKeys = new HashMap<>();
        stringKeys.put("beauty_and_health_title", R.string.beauty_and_health_title);
        stringKeys.put("charity_title", R.string.charity_title);
        stringKeys.put("cleaning_title", R.string.cleaning_title);
        stringKeys.put("delivery_and_transportation_title", R.string.delivery_and_transportation_title);
        stringKeys.put("photo_and_video_title", R.string.photo_and_video_title);
        stringKeys.put("repair_and_construction_title", R.string.repair_and_construction_title);
        stringKeys.put("tutoring_and_training_title", R.string.tutoring_and_training_title);
        stringKeys.put("beauty_and_health_desc", R.string.beauty_and_health_desc);
        stringKeys.put("charity_desc", R.string.charity_desc);
        stringKeys.put("cleaning_desc", R.string.cleaning_desc);
        stringKeys.put("delivery_and_transportation_desc,", R.string.delivery_and_transportation_desc);
        stringKeys.put("photo_and_video_desc", R.string.photo_and_video_desc);
        stringKeys.put("repair_and_construction_desc", R.string.repair_and_construction_desc);
        stringKeys.put("tutoring_and_training_desc", R.string.tutoring_and_training_desc);
        stringKeys.put("cosmetologist_services_title", R.string.cosmetologist_services_title);
        stringKeys.put("hairdressing_title", R.string.hairdressing_title);
        stringKeys.put("massage_title", R.string.massage_title);
        stringKeys.put("psychologists_title", R.string.psychologists_title);
        stringKeys.put("cosmetologist_services_desc", R.string.cosmetologist_services_desc);
        stringKeys.put("hairdressing_desc", R.string.hairdressing_desc);
        stringKeys.put("massage_desc", R.string.massage_desc);
        stringKeys.put("psychologists_desc", R.string.psychologists_desc);
        stringKeys.put("donations_title", R.string.donations_title);
        stringKeys.put("help_those_in_need_title", R.string.help_those_in_need_title);
        stringKeys.put("donations_desc", R.string.donations_desc);
        stringKeys.put("help_those_in_need_desc", R.string.help_those_in_need_desc);
        stringKeys.put("yard_cleaning_title", R.string.yard_cleaning_title);
        stringKeys.put("house_cleaning_title", R.string.house_cleaning_title);
        stringKeys.put("yard_cleaning_desc", R.string.yard_cleaning_desc);
        stringKeys.put("house_cleaning_desc", R.string.house_cleaning_desc);
        stringKeys.put("cargo_transportation_title", R.string.cargo_transportation_title);
        stringKeys.put("courier_title", R.string.courier_title);
        stringKeys.put("taxi_title", R.string.taxi_title);
        stringKeys.put("cargo_transportation_desc", R.string.cargo_transportation_desc);
        stringKeys.put("courier_desc", R.string.courier_desc);
        stringKeys.put("taxi_desc", R.string.taxi_desc);
        stringKeys.put("filming_title", R.string.filming_title);
        stringKeys.put("photoshoot_title", R.string.photoshoot_title);
        stringKeys.put("filming_desc", R.string.filming_desc);
        stringKeys.put("photoshoot_desc", R.string.photoshoot_desc);
        stringKeys.put("building_title", R.string.building_title);
        stringKeys.put("electrical_title", R.string.electrical_title);
        stringKeys.put("handyman_for_hour_title", R.string.handyman_for_hour_title);
        stringKeys.put("plumbing_title", R.string.plumbing_title);
        stringKeys.put("vehicle_repair_title", R.string.vehicle_repair_title);
        stringKeys.put("building_desc", R.string.building_desc);
        stringKeys.put("electrical_desc", R.string.electrical_desc);
        stringKeys.put("handyman_for_hour_desc", R.string.handyman_for_hour_desc);
        stringKeys.put("plumbing_desc", R.string.plumbing_desc);
        stringKeys.put("vehicle_repair_desc", R.string.vehicle_repair_desc);
        stringKeys.put("art_title", R.string.art_title);
        stringKeys.put("foreign_languages_title", R.string.foreign_languages_title);
        stringKeys.put("programming_title", R.string.programming_title);
        stringKeys.put("science_title", R.string.science_title);
        stringKeys.put("sport_title", R.string.sport_title);
        stringKeys.put("art_desc", R.string.art_desc);
        stringKeys.put("foreign_languages_desc", R.string.foreign_languages_desc);
        stringKeys.put("programming_desc", R.string.programming_desc);
        stringKeys.put("science_desc", R.string.science_desc);
        stringKeys.put("sport_desc", R.string.sport_desc);
        return stringKeys;
    }

    public static String[] categoryKeys = {
            //Category types
            // Category types titles
            "beauty_and_health_title",
            "charity_title",
            "cleaning_title",
            "delivery_and_transportation_title",
            "photo_and_video_title,",
            "repair_and_construction_title,",
            "tutoring_and_training_title,",

            //Category types descriptions
            "beauty_and_health_desc,",
            "charity_desc,",
            "cleaning_desc,",
            "delivery_and_transportation_desc,",
            "photo_and_video_desc,",
            "repair_and_construction_desc,",
            "tutoring_and_training_desc",

            //Categories
// 1 Beauty and health
            "cosmetologist_services_title,",
            "hairdressing_title,",
            "massage_title,",
            "psychologists_title",

            "cosmetologist_services_desc,",
            "hairdressing_desc,",
            "massage_desc,",
            "psychologists_desc",
// 2   Charity
            "donations_title,",
            "help_those_in_need_title",

            "donations_desc,",
            "help_those_in_need_desc",

// 3  Cleaning
            "yard_cleaning_title,",
            "house_cleaning_title",

            "yard_cleaning_desc,",
            "house_cleaning_desc",

// 4  Delivery and transportation
            "cargo_transportation_title,",
            "courier_title,",
            "taxi_title",

            "cargo_transportation_desc,",
            "courier_desc,",
            "taxi_desc",

// 5   Photo and video
            "filming_title,",
            "photoshoot_title",

            "filming_desc,",
            "photoshoot_desc",

// 6  Repair and construction
            "building_title,",
            "electrical_title,",
            "handyman_for_hour_title,",
            "plumbing_title,",
            "repair_and_construction_title,",
            "vehicle_repair_title",

            "building_desc,",
            "electrical_desc,",
            "handyman_for_hour_desc,",
            "plumbing_desc,",
            "repair_and_construction_desc,",
            "vehicle_repair_desc",

// 7  Tutoring and training
            "art_title,",
            "foreign_languages_title,",
            "programming_title,",
            "science_title,",
            "sport_title",

            "art_desc,",
            "foreign_languages_desc,",
            "programming_desc,",
            "science_desc,",
            "sport_desc ",
    };


    public static String[] categoryTypesTitles = {
            "beauty_and_health_title",
            "charity_title",
            "cleaning_title",
            "delivery_and_transportation_title",
            "photo_and_video_title",
            "repair_and_construction_title",
            "tutoring_and_training_title",
    };


    public static String[] categoryTypesDescriptions = {
            "beauty_and_health_desc",
            "charity_desc",
            "cleaning_desc",
            "delivery_and_transportation_desc",
            "photo_and_video_desc",
            "repair_and_construction_desc",
            "tutoring_and_training_desc",
    };


    public static String[] tutoring_and_trainingCategoryTitles = {
            "art_title",
            "foreign_languages_title",
            "programming_title",
            "science_title",
            "sport_title",
    };

    public static String[] tutoring_and_trainingCategoryDesc = {
            "art_desc",
            "foreign_languages_desc",
            "programming_desc",
            "science_desc",
            "sport_desc",
    };

    public static String[] repair_and_constructionCategoryTitles = {
            "building_title",
            "electrical_title",
            "handyman_for_hour_title",
            "plumbing_title",
            "repair_and_construction_title",
            "vehicle_repair_title",
    };
    public static String[] repair_and_constructionCategoryDesc = {
            "building_desc",
            "electrical_desc",
            "handyman_for_hour_desc",
            "plumbing_desc",
            "repair_and_construction_desc",
            "vehicle_repair_desc",
    };
    public static String[] photo_and_videoTitle = {
            "filming_title",
            "photoshoot_title",
    };
    public static String[] photo_and_videoDesc = {
            "filming_desc",
            "photoshoot_desc",
    };
    public static String[] delivery_and_transportationTitle = {
            "cargo_transportation_title",
            "courier_title",
            "taxi_title",
    };
    public static String[] delivery_and_transportationDesc = {
            "cargo_transportation_desc",
            "courier_desc",
            "taxi_desc",
    };
    public static String[] cleaningTitle = {
            "yard_cleaning_title",
            "house_cleaning_title",
    };
    public static String[] cleaningDesc = {
            "yard_cleaning_desc",
            "house_cleaning_desc",
    };
    public static String[] charityTitle = {
            "donations_title",
            "help_those_in_need_title",
    };
    public static String[] charityDesc = {
            "donations_desc",
            "help_those_in_need_desc",
    };
    public static String[] beauty_and_healthTitle = {
            "cosmetologist_services_title",
            "hairdressing_title",
            "massage_title",
            "psychologists_title",
    };
    public static String[] beauty_and_healthDesc = {
            "cosmetologist_services_desc",
            "hairdressing_desc",
            "massage_desc",
            "psychologists_desc",
    };


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
            new CategoryValues(cleaningTitle, cleaningDesc),
            new CategoryValues(delivery_and_transportationTitle, delivery_and_transportationDesc),
            new CategoryValues(photo_and_videoTitle, photo_and_videoDesc),
            new CategoryValues(repair_and_constructionCategoryTitles, repair_and_constructionCategoryDesc),
            new CategoryValues(tutoring_and_trainingCategoryTitles, tutoring_and_trainingCategoryDesc)
    };

    static class CategoryValues {
        String[] titles;
        String[] descriptions;

        public CategoryValues(String[] titles, String[] descriptions) {
            this.titles = titles;
            this.descriptions = descriptions;
        }
//        int[] titles;
//        int[] descriptions;
//
//        public CategoryValues(int[] titles, int[] descriptions) {
//            this.titles = titles;
//            this.descriptions = descriptions;
//        }
    }
}


//    public static String[] pathes = {
//            "category-types/beauty-and-health/categories",
//            "category-types/charity/categories",
//            "category-types/cleaning/categories",
//            "category-types/delivery-and-transportation/categories",
//            "category-types/photo-and-video/categories",
//            "category-types/repair-and-construction/categories",
//            "category-types/tutoring-and-training/categories",
//    };

//    public static void fillDB() {
//        fillCategoryTypes();
//        fillCategories(pathes[0], beauty_and_healthTitle, beauty_and_healthDesc);
//        fillCategories(pathes[1], charityTitle, charityDesc);
//        fillCategories(pathes[2], cleaningTitle, charityDesc);
//        fillCategories(pathes[3], delivery_and_transportationTitle, delivery_and_transportationDesc);
//        fillCategories(pathes[4], photo_and_videoTitle, photo_and_videoDesc);
//        fillCategories(pathes[5], repair_and_constructionCategoryTitles, repair_and_constructionCategoryDesc);
//        fillCategories(pathes[6], tutoring_and_trainingCategoryTitles, tutoring_and_trainingCategoryDesc);
//    }

//    public static void fillCategories(String path, int[] titles, int[] desc) {
//        CollectionReference categoryTypesRef = FirebaseFirestore.getInstance()
//                .collection(path);
//        categoryTypesRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
//            List<DocumentSnapshot> categoryTypesSnapshots = queryDocumentSnapshots.getDocuments();
//            for (int i = 0; i < titles.length; i++) {
//                DocumentSnapshot documentSnapshot = categoryTypesSnapshots.get(i);
//                Map<String, Object> dataToSave = new HashMap<>();
//                dataToSave.put("title", titles[i]);
//                dataToSave.put("description", desc[i]);
//                documentSnapshot.getReference().set(dataToSave);
//            }
//        });
//    }


//    public static void fillCategoryTypes() {
//        CollectionReference categoryTypesRef = FirebaseFirestore.getInstance()
//                .collection("category-types");
//        categoryTypesRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
//            List<DocumentSnapshot> categoryTypesSnapshots = queryDocumentSnapshots.getDocuments();
//            for (int i = 0; i < FillHelper.categoryTypesDescriptions.length; i++) {
//                DocumentSnapshot documentSnapshot = categoryTypesSnapshots.get(i);
//                Map<String, Object> dataToSave = new HashMap<>();
//                dataToSave.put("title", FillHelper.categoryTypesTitles[i]);
//                dataToSave.put("description", FillHelper.categoryTypesDescriptions[i]);
//                documentSnapshot.getReference().set(dataToSave);
//            }
//        });
//    }


//    public static int[] categoryTypesTitles = {
//            R.string.beauty_and_health_title,
//            R.string.charity_title,
//            R.string.cleaning_title,
//            R.string.delivery_and_transportation_title,
//            R.string.photo_and_video_title,
//            R.string.repair_and_construction_title,
//            R.string.tutoring_and_training_title
//    };
//    public static int[] categoryTypesDescriptions = {
//            R.string.beauty_and_health_desc,
//            R.string.charity_desc,
//            R.string.cleaning_desc,
//            R.string.delivery_and_transportation_desc,
//            R.string.photo_and_video_desc,
//            R.string.repair_and_construction_desc,
//            R.string.tutoring_and_training_desc
//    };
//    public static int[] tutoring_and_trainingCategoryTitles = {
//            R.string.art_title,
//            R.string.foreign_languages_title,
//            R.string.programming_title,
//            R.string.science_title,
//            R.string.sport_title
//    };
//    public static int[] tutoring_and_trainingCategoryDesc = {
//            R.string.art_desc,
//            R.string.foreign_languages_desc,
//            R.string.programming_desc,
//            R.string.science_desc,
//            R.string.sport_desc
//    };
//public static int[] repair_and_constructionCategoryTitles = {
//            R.string.building_title,
//            R.string.electrical_title,
//            R.string.handyman_for_hour_title,
//            R.string.plumbing_title,
//            R.string.repair_and_construction_title,
//            R.string.vehicle_repair_title
//    };
//    public static int[] repair_and_constructionCategoryDesc = {
//            R.string.building_desc,
//            R.string.electrical_desc,
//            R.string.handyman_for_hour_desc,
//            R.string.plumbing_desc,
//            R.string.repair_and_construction_desc,
//            R.string.vehicle_repair_desc
//    };
//    public static int[] photo_and_videoTitle = {
//            R.string.filming_title,
//            R.string.photoshoot_title
//    };
//    public static int[] photo_and_videoDesc = {
//            R.string.filming_desc,
//            R.string.photoshoot_desc
//    };
//    public static int[] delivery_and_transportationTitle = {
//            R.string.cargo_transportation_title,
//            R.string.courier_title,
//            R.string.taxi_title
//    };
//    public static int[] delivery_and_transportationDesc = {
//            R.string.cargo_transportation_desc,
//            R.string.courier_desc,
//            R.string.taxi_desc
//    };
//    public static int[] cleaningTitle = {
//            R.string.yard_cleaning_title,
//            R.string.house_cleaning_title
//    };
//    public static int[] cleaningDesc = {
//            R.string.yard_cleaning_desc,
//            R.string.house_cleaning_desc
//    };
//    public static int[] charityTitle = {
//            R.string.donations_title,
//            R.string.help_those_in_need_title
//    };
//    public static int[] charityDesc = {
//            R.string.donations_desc,
//            R.string.help_those_in_need_desc
//    };
//    public static int[] beauty_and_healthTitle = {
//            R.string.cosmetologist_services_title,
//            R.string.hairdressing_title,
//            R.string.massage_title,
//            R.string.psychologists_title
//    };
//    public static int[] beauty_and_healthDesc = {
//            R.string.cosmetologist_services_desc,
//            R.string.hairdressing_desc,
//            R.string.massage_desc,
//            R.string.psychologists_desc
//    };
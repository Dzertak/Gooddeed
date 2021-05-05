package com.kravchenko.apps.gooddeed.screen.filterwindow;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.database.entity.category.CategoryType;
import com.kravchenko.apps.gooddeed.database.entity.category.CategoryTypesWithCategories;
import com.kravchenko.apps.gooddeed.databinding.FragmentCategoryTypeFilterBinding;
import com.kravchenko.apps.gooddeed.screen.BaseFragment;
import com.kravchenko.apps.gooddeed.screen.adapter.filter.CategoryTypeRecyclerViewAdapter;
import com.kravchenko.apps.gooddeed.util.Utils;
import com.kravchenko.apps.gooddeed.viewmodel.AuthViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CategoryTypeFilterFragment extends BaseFragment {
    private FragmentCategoryTypeFilterBinding binding;
    private CategoryTypeRecyclerViewAdapter adapter;
    private AuthViewModel authViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCategoryTypeFilterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        NavigationUI.setupWithNavController(binding.toolbar, getNavController());
        initRecyclerView();
        authViewModel.getCategoryTypes()
                .observe(getViewLifecycleOwner(), categoryTypes -> {
                    System.out.println("******************************");
                    System.out.println("******************************");
                    System.out.println("******************************");
                    categoryTypes.forEach(categoryType -> System.out.println(categoryType.toString()));
                    adapter.setCategoryTypes(categoryTypes);
                });
        authViewModel.getCategoryTypesWithCategoriesLiveData()
                .observe(getViewLifecycleOwner(), categoryTypesWithCategories -> {
                    for (CategoryTypesWithCategories categories : categoryTypesWithCategories) {
                        Log.i("dev", categories.getCategoryType().getCategoryTypeId() + " " + categories.getCategoryType().toString());
                    }
                });

        DocumentReference documentReference = FirebaseFirestore.getInstance()
                .collection("category-types").document("beauty-and-health");
        Map<String, Object> dataToSave = new HashMap<>();
        dataToSave.put("title", R.string.beauty_and_health_title);
        dataToSave.put("description", R.string.beauty_and_health_desc);
       // documentReference.set(dataToSave);

        CollectionReference categoryTypesRef = FirebaseFirestore.getInstance().collection("category-types");
        categoryTypesRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<DocumentSnapshot> categoryTypesSnapshots = queryDocumentSnapshots.getDocuments();
            for (DocumentSnapshot documentSnapshot : categoryTypesSnapshots) {
                CategoryType categoryType = documentSnapshot.toObject(CategoryType.class);
                categoryType.setCategoryTypeId(documentSnapshot.getId());
                Log.i("dev", categoryType.toString());
            }
        });
    }

    private void initRecyclerView() {
        adapter = new CategoryTypeRecyclerViewAdapter(getContext());
        adapter.setOnItemClickListener(categoryType -> {
            Toast.makeText(getContext(), Utils.getString((int) categoryType.getTitle()), Toast.LENGTH_SHORT).show();
            //TODO change args
           NavDirections action = CategoryTypeFilterFragmentDirections.actionCategoryTypeFilterFragmentToCategoryFilterFragment3(categoryType.getCategoryTypeId());
            getNavController().navigate(action);
        });

        binding.recyclerViewCategoryTypes.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewCategoryTypes.setAdapter(adapter);


//        List<CategoryType> categoryTypes = new ArrayList<>();
//        categoryTypes.add(new CategoryType(R.string.cargo_transportation_title, R.string.description));
//        categoryTypes.add(new CategoryType(R.string.cargo_transportation_title, R.string.description));
//        categoryTypes.add(new CategoryType(R.string.cargo_transportation_title, R.string.description));
//        categoryTypes.add(new CategoryType(R.string.cargo_transportation_title, R.string.description));
//        adapter.setCategoryTypes(categoryTypes);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
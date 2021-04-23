package com.kravchenko.apps.gooddeed.screen.filterwindow;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.adapter.MainFilterRecyclerViewAdapter;
import com.kravchenko.apps.gooddeed.database.entity.Filter;
import com.kravchenko.apps.gooddeed.databinding.FragmentFilterMainBinding;
import com.kravchenko.apps.gooddeed.util.annotation.FilterName;

import java.util.ArrayList;
import java.util.List;

public class FilterFragmentMain extends Fragment {
    private FragmentFilterMainBinding binding;
    private NavController navController;
    private MainFilterRecyclerViewAdapter filterAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFilterMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbarFilterFragment);
        NavigationUI.setupWithNavController(binding.toolbarFilterFragment, navController);
        initRecyclerView();
    }

    private void initRecyclerView() {
        filterAdapter = new MainFilterRecyclerViewAdapter(getContext());
        filterAdapter.setOnItemClickListener(filter -> {
                    switch (filter.getFilterName()) {
                        case FilterName.CATEGORY:
                            // Toast.makeText(getContext(), filter.getName(), Toast.LENGTH_SHORT).show();
                            navController.navigate(R.id.action_filterFragment_to_categoryFilterFragment);
                            break;
                        case FilterName.PERFORMER_INITIATIVE:
                            break;
                        case FilterName.PERIOD_REALIZATION:
                            break;
                        case FilterName.RADIUS:
                            break;
                    }

                }
        );
        binding.recyclerViewFilterFragment.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewFilterFragment.setAdapter(filterAdapter);
        //TODO
        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter(FilterName.CATEGORY, "default"));
        filters.add(new Filter(FilterName.PERFORMER_INITIATIVE, "default"));
        filters.add(new Filter(FilterName.PERIOD_REALIZATION, "default"));
        filters.add(new Filter(FilterName.RADIUS, "default"));
        filterAdapter.setFilters(filters);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        Log.i("dev","onCreateOptionsMen");
        inflater.inflate(R.menu.filter_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
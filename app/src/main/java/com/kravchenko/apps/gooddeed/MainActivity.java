package com.kravchenko.apps.gooddeed;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.kravchenko.apps.gooddeed.viewmodel.AuthViewModel;

public class MainActivity extends AppCompatActivity {

    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // for check
        // NavController navController = Navigation.findNavController(this, R.id.fragment);
        // navController.navigate(R.id.action_loginFragment_to_mainFragment);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        if (authViewModel.getIsAuth().getValue()) {
            adjustBackStack();
        }
    }

    private void adjustBackStack() {
        NavController navController = Navigation.findNavController(this, R.id.fragment);
        NavGraph navGraph = navController.getGraph();
        navGraph.setStartDestination(R.id.mainFragment);
        NavOptions navOptions = new NavOptions.Builder()
                .setPopUpTo(R.id.loginFragment, true)
                .build();
        if (!(navController.getCurrentDestination().getId() == R.id.mainFragment)) {
            navController.navigate(R.id.action_loginFragment_to_mainFragment, null, navOptions);
        }
    }
}
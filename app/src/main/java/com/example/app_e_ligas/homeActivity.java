package com.example.app_e_ligas;

import android.os.Bundle;

import com.example.namespace.databinding.ActivityHomeBinding;

public class homeActivity extends DrawerBasedActivity {

    ActivityHomeBinding activityHomeBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHomeBinding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(activityHomeBinding.getRoot());
        allocateActivityTitle("Home");
    }
}
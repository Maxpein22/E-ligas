package com.example.app_e_ligas;

import android.os.Bundle;

import com.example.namespace.databinding.ActivityDashboardBinding;

public class DashboardActivity extends DrawerBasedActivity {

    ActivityDashboardBinding activityDashboardBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDashboardBinding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(activityDashboardBinding.getRoot());
        allocateActivityTitle("Dashboard");

    }
}

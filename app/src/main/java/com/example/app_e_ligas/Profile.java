package com.example.app_e_ligas;

import android.os.Bundle;

import com.example.namespace.databinding.ActivityProfileBinding;

public class Profile extends DrawerBasedActivity {
    ActivityProfileBinding activityProfileBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityProfileBinding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(activityProfileBinding.getRoot());
        allocateActivityTitle("Profile");


    }
}
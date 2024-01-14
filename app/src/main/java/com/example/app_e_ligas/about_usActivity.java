package com.example.app_e_ligas;

import android.os.Bundle;

import com.example.namespace.databinding.ActivityAboutUsBinding;

public class about_usActivity extends DrawerBasedActivity {
    ActivityAboutUsBinding activityAboutUsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAboutUsBinding = ActivityAboutUsBinding.inflate(getLayoutInflater());
        setContentView(activityAboutUsBinding.getRoot());
        allocateActivityTitle("About Us");





    }
}
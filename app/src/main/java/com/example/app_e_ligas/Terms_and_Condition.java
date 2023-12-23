package com.example.app_e_ligas;

import android.os.Bundle;

import com.example.namespace.databinding.ActivityTermsAndConditionBinding;

public class Terms_and_Condition extends DrawerBasedActivity {

    ActivityTermsAndConditionBinding activityTermsAndConditionBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTermsAndConditionBinding = ActivityTermsAndConditionBinding.inflate(getLayoutInflater());
        setContentView(activityTermsAndConditionBinding.getRoot());
        allocateActivityTitle("Terms and Condition");
    }
}
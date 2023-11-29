package com.example.app_e_ligas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.namespace.R;
import com.example.namespace.databinding.ActivityBarangayEventsBinding;

public class barangay_eventsActivity extends DrawerBasedActivity {
    ActivityBarangayEventsBinding activityBarangayEventsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBarangayEventsBinding = ActivityBarangayEventsBinding.inflate(getLayoutInflater());
        setContentView(activityBarangayEventsBinding.getRoot());
        allocateActivityTitle("Barangay Events");
    }
}
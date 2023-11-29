package com.example.app_e_ligas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.namespace.R;
import com.example.namespace.databinding.ActivityBarangayEmergencyBinding;

public class barangay_emergencyActivity extends DrawerBasedActivity {
    ActivityBarangayEmergencyBinding activityBarangayEmergencyBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBarangayEmergencyBinding = ActivityBarangayEmergencyBinding.inflate(getLayoutInflater());
        setContentView(activityBarangayEmergencyBinding.getRoot());
        allocateActivityTitle("Barangay Emergency");
    }
}
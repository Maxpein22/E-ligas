package com.example.app_e_ligas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.namespace.R;
import com.example.namespace.databinding.ActivityDamMonitoringBinding;

public class dam_monitoringActivity extends DrawerBasedActivity {
    ActivityDamMonitoringBinding activityDamMonitoringBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDamMonitoringBinding = ActivityDamMonitoringBinding.inflate(getLayoutInflater());
        setContentView(activityDamMonitoringBinding.getRoot());
        allocateActivityTitle("Barangay Dam Monitoring");
    }
}
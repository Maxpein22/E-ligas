package com.example.app_e_ligas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import com.example.namespace.databinding.ActivityDashboardBinding;

public class DashboardActivity extends DrawerBasedActivity {

    ActivityDashboardBinding activityDashboardBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDashboardBinding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(activityDashboardBinding.getRoot());
        allocateActivityTitle("Dashboard");

        setupCardClickListeners();
    }

    private void setupCardClickListeners() {
        CardView profile = activityDashboardBinding.profile;
        CardView barangayEventsCard = activityDashboardBinding.barangayEvents;
        CardView barangayServicesCard = activityDashboardBinding.barangayServices;
        CardView barangayEmergencyCard = activityDashboardBinding.barangayEmergency;
        CardView damMonitoringCard = activityDashboardBinding.damMonitoring;
        CardView aboutCard = activityDashboardBinding.about;
        CardView termsCard = activityDashboardBinding.terms;
        CardView logout = activityDashboardBinding.logout;

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToLogut();
            }
        });


        termsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToTerms();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToProfile();
            }
        });

        barangayEventsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToBarangayEvents();
            }
        });

        barangayServicesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToBarangayServices();
            }
        });

        barangayEmergencyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToBarangayEmergency();
            }
        });

        damMonitoringCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToDamMonitoring();
            }
        });

        aboutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToAbout();
            }
        });
    }

    private void navigateToLogut() {
        // Handle click for the Dashboard CardView
        Intent intent = new Intent(this, WecolmeAcvtivity.class);
        Toast.makeText(this, "Logout Successfully!", Toast.LENGTH_SHORT).show();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void navigateToProfile() {
        // Handle click for the Dashboard CardView
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }

    private void navigateToBarangayEvents() {
        // Handle click for the Barangay Events CardView
        Intent intent = new Intent(this, barangay_eventsActivity.class);
        startActivity(intent);
    }

    private void navigateToBarangayServices() {
        // Handle click for the Barangay Services CardView
        Intent intent = new Intent(this, barangay_servicesActivity.class);
        startActivity(intent);

    }

    private void navigateToBarangayEmergency() {
        // Handle click for the Barangay Emergency CardView
        Intent intent = new Intent(this, barangay_emergencyActivity.class);
        startActivity(intent);
    }

    private void navigateToDamMonitoring() {
        // Handle click for the Dam Monitoring CardView
        Intent intent = new Intent(this, dam_monitoringActivity.class);
        startActivity(intent);
    }

    private void navigateToAbout() {
        // Handle click for the About CardView
        Intent intent = new Intent(this, about_usActivity.class);
        startActivity(intent);
    }
    private void navigateToTerms() {
        // Handle click for the About CardView
        Intent intent = new Intent(this, Terms_and_Condition.class);
        startActivity(intent);
    }
}

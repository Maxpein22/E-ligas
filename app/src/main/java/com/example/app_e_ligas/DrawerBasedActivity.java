package com.example.app_e_ligas;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.namespace.R;
import com.google.android.material.navigation.NavigationView;

public class DrawerBasedActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;

    @Override
    public void setContentView(View view) {
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer_based, null);
        FrameLayout container = drawerLayout.findViewById(R.id.activityContainer);
        container.addView(view);
        super.setContentView(drawerLayout);

        Toolbar toolbar = drawerLayout.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = drawerLayout.findViewById(R.id.nav_views);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.menu_drawer_open, R.string.menu_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);

        int itemId = item.getItemId();

        if (itemId == R.id.nav_events) {
            startActivity(new Intent(this, barangay_eventsActivity.class));
            overridePendingTransition(0, 0);

        }
        else if (itemId == R.id.nav_services) {
            startActivity(new Intent(this, barangay_servicesActivity.class));
            overridePendingTransition(0, 0);

        }
        else if (itemId == R.id.nav_profile) {
            startActivity(new Intent(this, Profile.class));
            overridePendingTransition(0, 0);

        }
        else if (itemId == R.id.nav_emergency) {
            startActivity(new Intent(this, barangay_emergencyActivity.class));
            overridePendingTransition(0, 0);

        }
        else if (itemId == R.id.nav_dam) {
            startActivity(new Intent(this, dam_monitoringActivity.class));
            overridePendingTransition(0, 0);
        }
        else if (itemId == R.id.nav_about) {
            startActivity(new Intent(this, about_usActivity.class));
            overridePendingTransition(0, 0);

        }
        else if (itemId == R.id.nav_dashboard) {
            startActivity(new Intent(this, DashboardActivity.class));
            overridePendingTransition(0, 0);

        }
        else if (itemId == R.id.nav_Terms_Condition) {
            startActivity(new Intent(this, Terms_and_Condition.class));
            overridePendingTransition(0, 0);

        }
        else if (itemId == R.id.nav_logout) {
            Toast.makeText(this, "Logout Successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, WecolmeAcvtivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        return false;
    }


    protected void allocateActivityTitle(String titleString) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(titleString);
        }
    }
}

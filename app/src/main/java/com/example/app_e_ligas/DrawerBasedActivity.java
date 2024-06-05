package com.example.app_e_ligas;

import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DrawerBasedActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;


    @Override
    public void setContentView(View view) {
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer_based, null);
        FrameLayout container = drawerLayout.findViewById(R.id.activityContainer);
        container.addView(view);
        super.setContentView(drawerLayout);

        Toolbar toolbar = drawerLayout.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = drawerLayout.findViewById(R.id.nav_views);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.menu_drawer_open, R.string.menu_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        toggle.getDrawerArrowDrawable().setColor(Color.parseColor("#FFFFFF")); // Set to your desired color
        toggle.syncState();
        // Retrieve user's validation status and update the navigation menu
        updateNavigationMenuBasedOnValidation();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);

        int itemId = item.getItemId();

        if (itemId == R.id.nav_events) {
            startActivity(new Intent(this, barangay_eventsActivity.class));
            overridePendingTransition(0, 0);

        } else if (itemId == R.id.nav_officials) {
            startActivity(new Intent(this, BarangayOfficialsActivity.class));
            overridePendingTransition(0, 0);

        } else if (itemId == R.id.nav_services) {
            // Retrieve the current user
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                // If user is logged in, retrieve their validation status from Firebase
                String uid = currentUser.getUid();
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(uid);
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Boolean isDataSubmitted = snapshot.child("dataSubmitted").getValue(Boolean.class);
                        if (isDataSubmitted == null || !isDataSubmitted) {
                            // If data is not submitted, show toast message
                            Toast.makeText(DrawerBasedActivity.this, "Not available, please fill up census first", Toast.LENGTH_SHORT).show();
                        } else {
                            // If data is submitted, go to "Services" activity
                            startActivity(new Intent(DrawerBasedActivity.this, barangay_servicesActivity.class));
                            overridePendingTransition(0, 0);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle database error
                        Toast.makeText(DrawerBasedActivity.this, "Failed to retrieve user data. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                // Handle the case where the user is not logged in
                // This should not happen if the "Services" tab is only available for logged-in users
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            }

        } else if (itemId == R.id.nav_profile) {
            startActivity(new Intent(this, Profile.class));
            overridePendingTransition(0, 0);

        } else if (itemId == R.id.nav_emergency) {

            // Retrieve the current user
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                // If user is logged in, retrieve their validation status from Firebase
                String uid = currentUser.getUid();
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(uid);
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Boolean isDataSubmitted = snapshot.child("dataSubmitted").getValue(Boolean.class);
                        if (isDataSubmitted == null || !isDataSubmitted) {
                            // If data is not submitted, show toast message
                            Toast.makeText(DrawerBasedActivity.this, "Not available, please fill up census first", Toast.LENGTH_SHORT).show();
                        } else {
                            // If data is submitted, go to "Services" activity
                            startActivity(new Intent(DrawerBasedActivity.this, SubmitReport.class));
                            overridePendingTransition(0, 0);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle database error
                        Toast.makeText(DrawerBasedActivity.this, "Failed to retrieve user data. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                // Handle the case where the user is not logged in
                // This should not happen if the "Services" tab is only available for logged-in users
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            }

        } else if (itemId == R.id.nav_dam) {
            startActivity(new Intent(this, dam_monitoringActivity.class));
            overridePendingTransition(0, 0);

        } else if (itemId == R.id.nav_about) {
            startActivity(new Intent(this, about_usActivity.class));
            overridePendingTransition(0, 0);

        } else if (itemId == R.id.nav_dashboard) {
            startActivity(new Intent(this, DashboardActivity.class));
            overridePendingTransition(0, 0);

        }else if (itemId == R.id.nav_census) {
            startActivity(new Intent(this, barangay_cencus.class));
            overridePendingTransition(0, 0);

        } else if (itemId == R.id.nav_Terms_Condition) {
            startActivity(new Intent(this, Terms_and_Condition.class));
            overridePendingTransition(0, 0);



        }
       else if (itemId == R.id.nav_logout) {
            Toast.makeText(this, "Logout Successfully!", Toast.LENGTH_SHORT).show();

            // Clear user data
            FirebaseAuth.getInstance().signOut();
            User.clearUserData(); // You need to implement this method in your User class

            Intent intent = new Intent(this, SignInActivity.class);
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

    private void updateNavigationMenuBasedOnValidation() {
        // Retrieve the current user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            // If user is logged in, retrieve their validation status from Firebase
            String uid = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(uid);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        boolean isValidated = user.isValidated();
                        Boolean isDataSubmitted = snapshot.child("dataSubmitted").getValue(Boolean.class);

                        // Access the navigation view's menu
                        Menu menu = navigationView.getMenu();

                        // Hide specific menu items if the user is not validated
                        if (!isValidated) {
                            menu.findItem(R.id.nav_dashboard).setVisible(false);
                            menu.findItem(R.id.nav_services).setVisible(false);
                            menu.findItem(R.id.nav_emergency).setVisible(false);
                            menu.findItem(R.id.nav_events).setVisible(false);
                            menu.findItem(R.id.nav_census).setVisible(false);
                            menu.findItem(R.id.nav_about).setVisible(false);
                            menu.findItem(R.id.nav_Terms_Condition).setVisible(false);
                            menu.findItem(R.id.nav_officials).setVisible(false);
                            // Hide other menu items as needed
                        } else {
                            // Show or hide "Services" based on data submission status
                            MenuItem emergencyItem = menu.findItem(R.id.nav_emergency);
                            MenuItem servicesItem = menu.findItem(R.id.nav_services);
                            if (isDataSubmitted == null || !isDataSubmitted) {
                                // If data is not submitted, show "Services" with "Not Available" text in red color
                                SpannableString spannable = new SpannableString("Services (Not Available)");
                                spannable.setSpan(new ForegroundColorSpan(Color.RED), 9, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                servicesItem.setTitle(spannable);

                                SpannableString spannables = new SpannableString("Emergency (Not Available)");
                                spannables.setSpan(new ForegroundColorSpan(Color.RED), 10, spannables.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                emergencyItem.setTitle(spannables);
                            }
                        }
                    } else {
                        // Handle the case where the user data could not be retrieved
                        Toast.makeText(DrawerBasedActivity.this, "Failed to retrieve user data. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle database error
                    Toast.makeText(DrawerBasedActivity.this, "Failed to retrieve user data. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // If user is not logged in, hide all menu items except for the "dam" item
            Menu menu = navigationView.getMenu();
            menu.findItem(R.id.nav_dashboard).setVisible(false);
            menu.findItem(R.id.nav_services).setVisible(false);
            menu.findItem(R.id.nav_emergency).setVisible(false);
            menu.findItem(R.id.nav_events).setVisible(false);
            menu.findItem(R.id.nav_census).setVisible(false);
            menu.findItem(R.id.nav_about).setVisible(false);
            menu.findItem(R.id.nav_Terms_Condition).setVisible(false);
            menu.findItem(R.id.nav_officials).setVisible(false);
            menu.findItem(R.id.nav_profile).setVisible(false);
            menu.findItem(R.id.nav_logout).setVisible(false);
            // Hide other menu items as needed
        }
    }

}

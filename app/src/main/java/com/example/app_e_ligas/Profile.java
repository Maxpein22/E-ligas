package com.example.app_e_ligas;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.namespace.databinding.ActivityProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends DrawerBasedActivity {
    private static final String TAG = "ProfileActivity";
    ActivityProfileBinding activityProfileBinding;

    // Firebase variables
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityProfileBinding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(activityProfileBinding.getRoot());
        allocateActivityTitle("Profile");

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Initialize Firebase Realtime Database
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        // Check if user is logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is logged in, retrieve user data
            String userId = currentUser.getUid();
            retrieveUserData(userId);
        } else {
            // User is not logged in, handle accordingly (e.g., redirect to login screen)
            Log.d(TAG, "User is not logged in");
        }
    }

    private void retrieveUserData(String userId) {
        mDatabase.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Retrieve user data from dataSnapshot
                if (dataSnapshot.exists()) {
                    // User data found, populate UI with retrieved data
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        // Populate UI elements with user data
                        activityProfileBinding.fullNameTextView.setText(user.getFullName());
                        activityProfileBinding.phoneNumberTextView.setText(user.getUserPhoneNumber());
                        activityProfileBinding.emailTextView.setText(user.getUserEmail());
                        activityProfileBinding.addressTextView.setText(user.getAddress());
                        // You can populate other UI elements as needed
                    }
                } else {
                    // User data not found
                    Log.d(TAG, "User data not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                Log.d(TAG, "Database error: " + databaseError.getMessage());
            }
        });
    }
}

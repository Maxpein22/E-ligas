package com.example.app_e_ligas;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.namespace.R;
import com.example.namespace.databinding.ActivityDamMonitoringBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class dam_monitoringActivity extends DrawerBasedActivity {

    ActivityDamMonitoringBinding activityDamMonitoringBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDamMonitoringBinding = ActivityDamMonitoringBinding.inflate(getLayoutInflater());
        setContentView(activityDamMonitoringBinding.getRoot());
        allocateActivityTitle("Barangay Dam Monitoring");

        // Retrieve waterDistance value from Firebase Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("waterDistance");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Assuming waterDistance is stored as a String
                    String waterDistance = dataSnapshot.getValue(String.class);

                    // Display waterDistance value in a TextView
                    TextView waterDistanceTextView = findViewById(R.id.textView5);
                    waterDistanceTextView.setText(waterDistance + " Centimeters");

                    // Display an image based on waterDistance
                    ImageView waterLevelImageView = findViewById(R.id.waterImage);
                    int waterDistanceValue = Integer.parseInt(waterDistance);

// Choose images based on water level (you should have these images in your res/drawable folder)
                    if (waterDistanceValue >= 100) {
                        waterLevelImageView.setImageResource(R.drawable.i0);
                    } else if (waterDistanceValue >= 90) {
                        waterLevelImageView.setImageResource(R.drawable.i10);
                    } else if (waterDistanceValue >= 80) {
                        waterLevelImageView.setImageResource(R.drawable.i20);
                    } else if (waterDistanceValue >= 70) {
                        waterLevelImageView.setImageResource(R.drawable.i30);
                    } else if (waterDistanceValue >= 60) {
                        waterLevelImageView.setImageResource(R.drawable.i40);
                    } else if (waterDistanceValue >= 55) {
                        waterLevelImageView.setImageResource(R.drawable.i50);
                    } else if (waterDistanceValue >= 40) {
                        waterLevelImageView.setImageResource(R.drawable.i60);
                    } else if (waterDistanceValue >= 30) {
                        waterLevelImageView.setImageResource(R.drawable.i70);
                    } else if (waterDistanceValue >= 20) {
                        waterLevelImageView.setImageResource(R.drawable.i80);
                    } else if (waterDistanceValue >= 15) {
                        waterLevelImageView.setImageResource(R.drawable.i90);
                    } else if (waterDistanceValue >= 0) {
                        waterLevelImageView.setImageResource(R.drawable.i100);
                    }


                    // Handle the status of Water Level
                    TextView waterStatusTextView = findViewById(R.id.textView9);
                    if (waterDistanceValue >= 100) {
                        waterStatusTextView.setText("is Low!");
                    } else if (waterDistanceValue >= 50 && waterDistanceValue < 100) {
                        waterStatusTextView.setText("is Medium!");
                    } else if (waterDistanceValue < 50) {
                        waterStatusTextView.setText("is High!");
                    }


                } else {
                    // Handle the case when the data doesn't exist
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors, if any
            }
        });
    }
}

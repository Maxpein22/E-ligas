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
                    String waterDistanceString = dataSnapshot.getValue(String.class);

// Convert waterDistance to meters with error checking
                    double meter;
                    try {
                        int waterDistance = Integer.parseInt(waterDistanceString);
                        meter = waterDistance / 1.524;
                        meter = meter - 10.00;
                        meter = -1 * meter;
                    } catch (NumberFormatException e) {
                        // Handle the case when waterDistance is not a valid integer string
                        e.printStackTrace(); // Log the error or handle it as needed
                        meter = 0.0; // Default value or appropriate fallback
                    }

// Display waterDistance value in meters in a TextView
                    TextView waterDistanceTextView = findViewById(R.id.textView5);

                    waterDistanceTextView.setText(String.format("%.2f", meter) + " Meters");

// Display an image based on the original waterDistance value
                    ImageView waterLevelImageView = findViewById(R.id.waterImage);
                    int waterDistanceValue;
                    try {
                        waterDistanceValue = Integer.parseInt(waterDistanceString);
                    } catch (NumberFormatException e) {
                        // Handle the case when waterDistance is not a valid integer string
                        e.printStackTrace(); // Log the error or handle it as needed
                        waterDistanceValue = 0; // Default value or appropriate fallback
                    }

// Choose images based on the exact water level (you should have these images in your res/drawable folder)
                    int imageResource;
                    switch (waterDistanceValue) {
                        case 15:
                            imageResource = R.drawable.i15;
                            break;
                        case 14:
                            imageResource = R.drawable.i14;
                            break;
                        case 13:
                            imageResource = R.drawable.i13;
                            break;
                        case 12:
                            imageResource = R.drawable.i12;
                            break;
                        case 11:
                            imageResource = R.drawable.i11;
                            break;
                        case 10:
                            imageResource = R.drawable.i10;
                            break;
                        case 9:
                            imageResource = R.drawable.i9;
                            break;
                        case 8:
                            imageResource = R.drawable.i8;
                            break;
                        case 7:
                            imageResource = R.drawable.i7;
                            break;
                        case 6:
                            imageResource = R.drawable.i6;
                            break;
                        case 5:
                            imageResource = R.drawable.i5;
                            break;
                        case 4:
                            imageResource = R.drawable.i4;
                            break;
                        case 3:
                            imageResource = R.drawable.i3;
                            break;
                        case 2:
                            imageResource = R.drawable.i2;
                            break;
                        case 1:
                            imageResource = R.drawable.i1;
                            break;
                        case 0:
                            imageResource = R.drawable.i0;
                            break;
                        // Add cases for other values as needed
                        default:
                            // Default image when the exact value doesn't match any specific case
                            imageResource = R.drawable.i0; // Replace with your default image
                            break;
                    }

                    waterLevelImageView.setImageResource(imageResource);

// No changes to the status handling
                    TextView waterStatusTextView = findViewById(R.id.textView9);
                    if (waterDistanceValue == 0) {
                        waterStatusTextView.setText("Alert Water Level!");
                    } else if (waterDistanceValue == 1) {
                        waterStatusTextView.setText("Alert Water Level!");
                    }
                    else if (waterDistanceValue == 2) {
                        waterStatusTextView.setText("Alert Water Level!");
                    }
                    else if (waterDistanceValue == 3) {
                        waterStatusTextView.setText("Alert Water Level!");
                    }else if (waterDistanceValue == 4) {
                        waterStatusTextView.setText("High Water Level!");
                    }else if (waterDistanceValue == 5) {
                        waterStatusTextView.setText("High Water Level!");
                    }else if (waterDistanceValue == 6) {
                        waterStatusTextView.setText("High Water Level!");
                    }else if (waterDistanceValue == 7) {
                        waterStatusTextView.setText("High Water Level!");
                    }else if (waterDistanceValue == 8) {
                        waterStatusTextView.setText("Increasing Water Level!");
                    }else if (waterDistanceValue == 9) {
                        waterStatusTextView.setText("Increasing Water Level!");
                    }else if (waterDistanceValue == 10) {
                        waterStatusTextView.setText("Increasing Water Level!");
                    }else if (waterDistanceValue == 11) {
                        waterStatusTextView.setText("Increasing Water Level!");
                    }else if (waterDistanceValue == 12) {
                        waterStatusTextView.setText("Calm Water Level!");
                    }else if (waterDistanceValue == 13) {
                        waterStatusTextView.setText("Calm Water Level!");
                    }else if (waterDistanceValue == 14) {
                        waterStatusTextView.setText("Calm Water Level!");
                    }else if (waterDistanceValue == 15) {
                        waterStatusTextView.setText("Calm Water Level!");
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

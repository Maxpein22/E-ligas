package com.example.app_e_ligas;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
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

        // Initialize the buttons
        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);

        // Set click listeners for each button
        button1.setOnClickListener(v -> showPopupWindow1());
        button2.setOnClickListener(v -> showPopupWindow2());
        button3.setOnClickListener(v -> showPopupWindow3());


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
                        meter = meter - 13.20;
                        meter = -1 * meter;
                        if (meter > 10) meter = 10;
                        if (meter < 0) meter = 0;
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
                        case 21:
                            imageResource = R.drawable.i15;
                            break;
                        case 20:
                            imageResource = R.drawable.i15;
                            break;
                        case 19:
                            imageResource = R.drawable.i15;
                            break;
                        case 18:
                            imageResource = R.drawable.i14;
                            break;
                        case 17:
                            imageResource = R.drawable.i12;
                            break;
                        case 16:
                            imageResource = R.drawable.i11;
                            break;
                        case 15:
                            imageResource = R.drawable.i10;
                            break;
                        case 14:
                            imageResource = R.drawable.i9;
                            break;
                        case 13:
                            imageResource = R.drawable.i8;
                            break;
                        case 12:
                            imageResource = R.drawable.i7;
                            break;
                        case 11:
                            imageResource = R.drawable.i6;
                            break;
                        case 10:
                            imageResource = R.drawable.i5;
                            break;
                        case 9:
                            imageResource = R.drawable.i4;
                            break;
                        case 8:
                            imageResource = R.drawable.i3;
                            break;
                        case 7:
                            imageResource = R.drawable.i2;
                            break;
                        case 6:
                            imageResource = R.drawable.i1;
                            break;
                        case 5:
                            imageResource = R.drawable.i1;
                            break;
                        case 4:
                            imageResource = R.drawable.i0;
                            break;
                        case 3:
                            imageResource = R.drawable.i0;
                            break;
                        case 2:
                            imageResource = R.drawable.i0;
                            break;
                        case 1:
                            imageResource = R.drawable.i0;
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

                    // Set the image resource for the water level ImageView
                    waterLevelImageView.setImageResource(imageResource);

                    // Change the text in the TextView based on water level
                    TextView waterLevelLegendTextView = findViewById(R.id.newTextViews);
                    // Define the text for each water level
                    if (meter <= 2.5) {
                        waterLevelLegendTextView.setText("Stay informed and monitor weather updates.");
                    } else if (meter <= 5.0) {
                        waterLevelLegendTextView.setText("Prepare for potential changes, check supplies, and be ready to act if necessary.");
                    } else if (meter <= 7.5) {
                        waterLevelLegendTextView.setText("Be ready to evacuate if needed. Secure property and move valuables up.");
                    } else {
                        waterLevelLegendTextView.setText("Evacuate immediately. Follow local orders and take essential items with you.");
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


    private void showPopupWindow1() {
        // Inflate the popup layout for button1
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_layout_button1, null);

        // Create the PopupWindow with appropriate width and height
        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        // Get the root view of the activity
        View rootView = activityDamMonitoringBinding.getRoot();

        // Show the popup window at the center of the screen
        popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);

    }

    private void showPopupWindow2() {
        // Inflate the popup layout for button2
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_layout_button2, null);

        // Create the PopupWindow with appropriate width and height
        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        // Get the root view of the activity
        View rootView = activityDamMonitoringBinding.getRoot();

        // Show the popup window at the center of the screen
        popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);

    }

    private void showPopupWindow3() {
        // Inflate the popup layout for button3
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_layout_button3, null);

        // Create the PopupWindow with appropriate width and height
        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        // Get the root view of the activity
        View rootView = activityDamMonitoringBinding.getRoot();

        // Show the popup window at the center of the screen
        popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);

    }
}



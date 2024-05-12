package com.example.app_e_ligas;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.namespace.R;
import com.example.namespace.databinding.ActivityDashboardBinding;

public class DashboardActivity extends DrawerBasedActivity {


    ActivityDashboardBinding activityDashboardBinding;
    String originalDescriptionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDashboardBinding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(activityDashboardBinding.getRoot());
        allocateActivityTitle("Dashboard");

        // Store the original text of the description
        originalDescriptionText = activityDashboardBinding.anotherDescription.getText().toString();

        // Setting OnClickListener for the "Mission" button
        Button missionButton = findViewById(R.id.button1);
        missionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Revert the text to the original description
                activityDashboardBinding.anotherDescription.setText(originalDescriptionText);
                // Change button colors
                changeButtonColor(R.id.button1);
            }
        });

        // Setting OnClickListener for the "Vision" button
        Button visionButton = findViewById(R.id.button2);
        visionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change the text displayed in the "anotherDescription" TextView for Vision
                activityDashboardBinding.anotherDescription.setText("We exercise transparency, integrity, professionalism, efficiency and most of all we conduct free services because as Public Servants. We are accountable to the residents of Barangay Ligas 1.");
                // Change button colors
                changeButtonColor(R.id.button2);
            }
        });

        // Setting OnClickListener for the "Goal" button
        Button goalButton = findViewById(R.id.button3);
        goalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change the text displayed in the "anotherDescription" TextView for Goal
                activityDashboardBinding.anotherDescription.setText("Barangay Ligas 1 aims to be efficient in serving the public because Public Office is a Public Trust and must at all times be accountable to the people, serve them with utmost responsibility, loyalty and efficiency, act with patriotism and justice and lead modest lives. Thus, Barangay Ligas 1 is determined to address everything that hinder its way to be the best.");
                // Change button colors
                changeButtonColor(R.id.button3);
            }
        });

        Button learnMoreButton = findViewById(R.id.learnMoreButton);
        learnMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(DashboardActivity.this, about_usActivity.class));
            }
        });

        // Setting OnClickListener for the "Contact Us" button
        Button contactUsButton = findViewById(R.id.actionButton);
        contactUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open Facebook page in browser when "Contact Us" button is clicked
                String facebookUrl = "https://www.facebook.com/pamunuanngligas.uno"; // Replace "yourpage" with your Facebook page name or URL
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl));
                startActivity(intent);
            }
        });

        // Set default clicked state to "Mission"
        changeButtonColor(R.id.button1);
    }

    // Method to change the color of the clicked button and reset others
    private void changeButtonColor(int buttonId) {
        Button missionButton = findViewById(R.id.button1);
        Button visionButton = findViewById(R.id.button2);
        Button goalButton = findViewById(R.id.button3);

        // Reset all button colors to default (white)
        missionButton.setBackgroundColor(getResources().getColor(R.color.splash_blue));
        visionButton.setBackgroundColor(getResources().getColor(R.color.splash_blue));
        goalButton.setBackgroundColor(getResources().getColor(R.color.splash_blue));

        // Set the color of the clicked button to white
        Button clickedButton = findViewById(buttonId);
        clickedButton.setBackgroundColor(getResources().getColor(R.color.grey));
    }
}

package com.example.app_e_ligas;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.namespace.databinding.ActivityAboutUsBinding;

public class about_usActivity extends DrawerBasedActivity {
    ActivityAboutUsBinding activityAboutUsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAboutUsBinding = ActivityAboutUsBinding.inflate(getLayoutInflater());
        setContentView(activityAboutUsBinding.getRoot());
        allocateActivityTitle("About Us");

        // Set click listeners for social media icons
        activityAboutUsBinding.imageFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open Facebook page
                openFacebookPage();
            }
        });

        activityAboutUsBinding.imageGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Send email via Gmail
                sendEmail();
            }
        });

        activityAboutUsBinding.imageHotline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call or message hotline number
                callOrMessageHotline();
            }
        });
    }

    // Method to open the Facebook page
    private void openFacebookPage() {
        String facebookUrl = "https://www.facebook.com/pamunuanngligas.uno"; // Replace "YourPageName" with your actual page name
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl));
        startActivity(intent);
    }

    // Method to send email via Gmail
    private void sendEmail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:ligasuno@gmail.com")); // Replace "example@gmail.com" with the Gmail address
        startActivity(intent);
    }

    // Method to call or message the hotline number
    private void callOrMessageHotline() {
        String hotlineNumber = "09391142215"; // Replace with the actual hotline number
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + hotlineNumber));
        startActivity(intent);
    }
}

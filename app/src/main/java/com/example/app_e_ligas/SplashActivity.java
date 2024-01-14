package com.example.app_e_ligas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import com.example.namespace.R;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                // Find the TextView elements
                TextView textView = findViewById(R.id.textSplashScreen);
                TextView textView1 = findViewById(R.id.text2SplashScreen);

        // Define the animation parameters
                float fromScale = 0.0f; // Start scale
                float toScale = 1.0f; // End scale
                int duration = 1000; // Animation duration in milliseconds

        // Create a ScaleAnimation for textView
                Animation popInAnimation = new ScaleAnimation(
                        fromScale, toScale, fromScale, toScale,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
                );
                popInAnimation.setDuration(duration);
                popInAnimation.setFillAfter(true);

        // Apply the animation to textView
                textView.startAnimation(popInAnimation);

        // Create a ScaleAnimation for textView1
                Animation popInAnimation1 = new ScaleAnimation(
                        fromScale, toScale, fromScale, toScale,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
                );
                popInAnimation1.setDuration(duration);
                popInAnimation1.setFillAfter(true);

        // Apply the animation to textView1
                textView1.startAnimation(popInAnimation1);


        Thread thread = new Thread() {
            public void run() {
                try {
                    // Sleep for 4 seconds (4000 milliseconds)
                    sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    // Create an Intent to start the MainActivity
                    //Intent intent = new Intent(SplashActivity.this, WecolmeAcvtivity.class);
                    Intent intent = new Intent(SplashActivity.this, barangay_emergencyActivity.class);
                    startActivity(intent);
                    // Finish the current activity
                    finish();
                }
            }
        };

// Start the thread
        thread.start();


    }
}
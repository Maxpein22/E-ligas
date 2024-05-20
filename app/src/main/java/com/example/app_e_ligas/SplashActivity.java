package com.example.app_e_ligas;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.namespace.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        TextView textView = findViewById(R.id.textSplashScreen);
        TextView textView1 = findViewById(R.id.text2SplashScreen);

        float fromScale = 0.0f;
        float toScale = 1.0f;
        int duration = 1000;

        Animation popInAnimation = new ScaleAnimation(
                fromScale, toScale, fromScale, toScale,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        );
        popInAnimation.setDuration(duration);
        popInAnimation.setFillAfter(true);
        textView.startAnimation(popInAnimation);

        Animation popInAnimation1 = new ScaleAnimation(
                fromScale, toScale, fromScale, toScale,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        );
        popInAnimation1.setDuration(duration);
        popInAnimation1.setFillAfter(true);
        textView1.startAnimation(popInAnimation1);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    // User is signed in
                    Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
                    startActivity(intent);
                } else {
                    // User is not signed in
                    Intent intent = new Intent(SplashActivity.this, WecolmeAcvtivity.class);
                    startActivity(intent);
                }
                finish();
            }
        }, 1000); // Delay for 1 second before checking authentication status
    }
}

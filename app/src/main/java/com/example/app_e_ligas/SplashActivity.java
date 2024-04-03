package com.example.app_e_ligas;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.namespace.R;

public class SplashActivity extends AppCompatActivity {

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

        Thread thread = new Thread() {
            public void run() {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent intent = new Intent(SplashActivity.this, WecolmeAcvtivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        thread.start();
    }
}

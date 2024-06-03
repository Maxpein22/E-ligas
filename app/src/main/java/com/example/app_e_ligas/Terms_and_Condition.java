package com.example.app_e_ligas;

import android.os.Bundle;
import android.webkit.WebView;

import com.example.namespace.R;
import com.example.namespace.databinding.ActivityTermsAndConditionBinding;

public class Terms_and_Condition extends DrawerBasedActivity {

    ActivityTermsAndConditionBinding activityTermsAndConditionBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTermsAndConditionBinding = ActivityTermsAndConditionBinding.inflate(getLayoutInflater());
        setContentView(activityTermsAndConditionBinding.getRoot());
        allocateActivityTitle("Terms and Condition");

        WebView signUpTermWebView = findViewById(R.id.signUpTermWebView);
        signUpTermWebView.getSettings().setJavaScriptEnabled(true); // Enable JavaScript if needed
        signUpTermWebView.loadUrl("https://e-ligas.netlify.app/documenttemplate/verifier?templateID=-NzIvRvikPhsCR6f-pd1"); // Load the URL you want to display

    }
}
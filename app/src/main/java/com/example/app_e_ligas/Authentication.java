package com.example.app_e_ligas;


import android.app.Activity;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//Source: https://github.com/firebase/snippets-android/blob/bc74fe8e59253db2b712eb0d1e362e990b7d69fe/auth/app/src/main/java/com/google/firebase/quickstart/auth/GoogleSignInActivity.java#L67-L68

public class Authentication {
    Activity activity;


    // [START auth_fui_create_launcher]
    // See: https://developer.android.com/training/basics/intents/result
    private final ActivityResultLauncher<Intent> signInLauncher;
    // [END auth_fui_create_launcher]

    public Authentication(Activity activity, ActivityResultLauncher<Intent> signInLauncher) {
        this.activity = activity;

        this.signInLauncher = signInLauncher;
    }


    public void navigateToSignInActivity(){
        activity.finish();
        Intent accountSetupActivity = new Intent(activity.getApplicationContext(), MainActivity.class);
        activity.startActivity(accountSetupActivity);
    }

    public void navigateToActivity(Class navigateClass){
        activity.finish();
        Intent accountSetupActivity = new Intent(activity.getApplicationContext(), navigateClass);
        activity.startActivity(accountSetupActivity);
    }







    public FirebaseUser getUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }



    public static  String toValidFirebaseEmail(String email){
        return email.toString().replace('.','|');
    }

    public static  String toNormalEmailFromFirebase(String email){
        return email.toString().replace('|','.');
    }



    public void navigateToActivity(Activity currentActivity,Class<?> toNavigateClass) {
        currentActivity.finish();
        Intent newActivity = new Intent(currentActivity.getApplicationContext(), toNavigateClass);
        currentActivity.startActivity(newActivity);
    }


}
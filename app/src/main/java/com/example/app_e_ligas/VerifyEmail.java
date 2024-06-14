package com.example.app_e_ligas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.namespace.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VerifyEmail extends AppCompatActivity {
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);

        TextView messageMail = findViewById(R.id.messageMail);

        Button resendEmailBtn = findViewById(R.id.resendEmailBtn);
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() == null){
            Intent i = new Intent(this, SignInActivity.class);
            startActivity(i);
            finish();
        }


        if(mAuth.getCurrentUser().isEmailVerified()){
            Intent i = new Intent(this, DashboardActivity.class);
            startActivity(i);
            finish();
        }else{
            Toast.makeText(this, "Your email is not yet verified", Toast.LENGTH_LONG).show();
        }

        messageMail.setText("We sent a verification email to " + mAuth.getCurrentUser().getEmail() + "\n \n Open your mail box and verify your email first" );

        resendEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.getCurrentUser().sendEmailVerification()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // Email sent successfully
                                    Toast.makeText(VerifyEmail.this, "Verification email, open your mail box ", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Failed to send verification email
                                    Toast.makeText(VerifyEmail.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        Button logOutBtn = findViewById(R.id.logOutBtn);
        logOutBtn.setElevation(0);
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VerifyEmail.this, "Logout Successfully!", Toast.LENGTH_SHORT).show();

                // Clear user data
                FirebaseAuth.getInstance().signOut();
                User.clearUserData(); // You need to implement this method in your User class

                Intent intent = new Intent(VerifyEmail.this, SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        Button verifyBtn = findViewById(R.id.verifyBtn);
        verifyBtn.setElevation(0);
        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.getCurrentUser().reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser updatedUser = mAuth.getCurrentUser();
                            if (updatedUser != null && updatedUser.isEmailVerified()) {
                                // Email is verified
                                Toast.makeText(VerifyEmail.this, "Email is verified", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(VerifyEmail.this, DashboardActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                // Email is not verified
                                Toast.makeText(VerifyEmail.this, "Email is not verified.", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            // Handle the error
                            System.out.println("Error reloading user.");
                        }
                    }
                });
            }
        });

    }

}
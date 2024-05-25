package com.example.app_e_ligas;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.namespace.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText editTextEmail;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        editTextEmail = findViewById(R.id.editTextForgotPasswordEmail);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.forgotPasswordActivity);
    }

    public void forgotPasswordResetBtnPressed(View v) {
        resetPassword();
    }

    private void resetPassword() {
        String txtEmail = editTextEmail.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail).matches()) {
            editTextEmail.setError("Please Enter a Valid Email");
            editTextEmail.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.sendPasswordResetEmail(txtEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Toast.makeText(ForgotPasswordActivity.this, "Please Check Your Email to Reset Password", Toast.LENGTH_SHORT).show();

                    // Delay transition to the next activity by 3 seconds to allow user to see the message
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(ForgotPasswordActivity.this, SignInActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }, 3000); // 3000 milliseconds = 3 seconds

                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Failed to Reset Password. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

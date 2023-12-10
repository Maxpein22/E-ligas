package com.example.app_e_ligas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.namespace.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {
    EditText editTextEmail, editTextPassword;
    TextView textViewForgotPassword, textViewRegister;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        editTextEmail = findViewById(R.id.editTextSignInUserName);
        editTextPassword = findViewById(R.id.editTextSignInUserPassword);

        textViewForgotPassword = findViewById(R.id.txtSignInForgotPassword);
        textViewRegister = findViewById(R.id.txtSignInRegister);

        progressBar = findViewById(R.id.progressBar2);

        mAuth = FirebaseAuth.getInstance();
    }

    public void txtSignInForgotPasswordClicked(View v) {
        Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    public void txtSignInRegisterClicked(View v) {
        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    public void buttonSignInScrSignInClicked(View v) {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            // Add a check to ensure email and password are not empty
            Toast.makeText(SignInActivity.this, "Email and Password cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please Enter a Valid Email");
            editTextEmail.requestFocus();
            return;
        }

        // Disable UI elements during authentication
        setUIEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);

                // Re-enable UI elements after authentication completes
                setUIEnabled(true);

                if (task.isSuccessful()) {
                    Toast.makeText(SignInActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(SignInActivity.this, homeActivity.class));
                    finish();
                } else {
                    // Show a generic error message
                    Toast.makeText(SignInActivity.this, "Login Failed. Please check your credentials and try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Method to enable or disable UI elements based on the parameter
    private void setUIEnabled(boolean isEnabled) {
        editTextEmail.setEnabled(isEnabled);
        editTextPassword.setEnabled(isEnabled);
        textViewForgotPassword.setEnabled(isEnabled);
        textViewRegister.setEnabled(isEnabled);
        // Disable or enable any other UI elements as needed
    }
}

package com.example.app_e_ligas;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {
    EditText editTextEmail, editTextPassword;
    TextView textViewForgotPassword, textViewRegister;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    ImageButton imageButtonShowPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        editTextEmail = findViewById(R.id.editTextSignInUserName);
        editTextPassword = findViewById(R.id.editTextPassword);
        imageButtonShowPassword = findViewById(R.id.imageButtonShowPassword); // Change the initialization to ImageButton

        textViewForgotPassword = findViewById(R.id.txtSignInForgotPassword);
        textViewRegister = findViewById(R.id.txtSignInRegister);

        progressBar = findViewById(R.id.progressBar2);

        mAuth = FirebaseAuth.getInstance();

        // Set OnClickListener to the CheckBox to toggle password visibility
        // Set OnClickListener to the ImageButton to toggle password visibility
        imageButtonShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save the current cursor position
                int cursorPosition = editTextPassword.getSelectionStart();

                // Toggle password visibility
                if (editTextPassword.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
                    // Password is currently hidden, so show it
                    editTextPassword.setTransformationMethod(null);
                    imageButtonShowPassword.setImageResource(R.drawable.baseline_visibility_off_24); // Set hide password icon
                } else {
                    // Password is currently visible, so hide it
                    editTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imageButtonShowPassword.setImageResource(R.drawable.baseline_remove_red_eye_24); // Set show password icon
                }

                // Restore cursor position
                editTextPassword.setSelection(cursorPosition);
            }
        });



    }

    public void txtSignInForgotPasswordClicked(View v) {
        Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    public void txtSignInRegisterClicked(View v) {
        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
        startActivity(intent);
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

                // Within the mAuth.signInWithEmailAndPassword() method
// When authentication completes, check whether the sign-in was successful
                if (task.isSuccessful()) {
                    String uid = mAuth.getCurrentUser().getUid();
                    FirebaseDatabase.getInstance().getReference("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // Retrieve the user object from the database
                            User user = snapshot.getValue(User.class);
                            if (user != null) {
                                // Save the user data for later use
                                User currentUser = user;

                                // Pass the validation status as an extra to the next activity
                                boolean isValidated = user.isValidated();
                                Intent intent = new Intent(SignInActivity.this, DashboardActivity.class);
                                intent.putExtra("isValidated", isValidated);
                                startActivity(intent);
                                finish();

                                // Show a message based on the user's validation status
                                if (isValidated) {
                                    Toast.makeText(SignInActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(SignInActivity.this, "Login Successfully, but your account is not validated yet. Certain features may be restricted.", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            } else {
                                // Handle the case where the user data could not be retrieved
                                Toast.makeText(SignInActivity.this, "Failed to retrieve user data. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle database error
                            Toast.makeText(SignInActivity.this, "Failed to retrieve user data. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
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
        imageButtonShowPassword.setEnabled(isEnabled);
        // Disable or enable any other UI elements as needed
    }
}

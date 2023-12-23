package com.example.app_e_ligas;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.namespace.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SignUpActivity extends AppCompatActivity {
    EditText editTextLastName;
    EditText editTextFirstName;
    EditText editTextMiddleName;
    EditText editTextPhoneNumber;
    EditText editTextEmail;
    EditText editTextPassword;
    EditText editTextConfirmPassword;
    EditText editTextCivilStatus;
    EditText editTextAge;
    EditText editTextBirthday;
    EditText editTextBirthplace;
    EditText editTextAddress;
    EditText editTextEmergencyContact;

    Button buttonRegister;
    ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTextLastName = findViewById(R.id.editTextLastName);
        editTextMiddleName = findViewById(R.id.editTextMiddleName);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        editTextCivilStatus = findViewById(R.id.editTextCivilStatus);
        editTextAge = findViewById(R.id.editTextAge);
        editTextBirthday = findViewById(R.id.editTextBirthday);
        editTextBirthplace = findViewById(R.id.editTextBirthplace);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextEmergencyContact = findViewById(R.id.editTextEmergencyContact);

        buttonRegister = findViewById(R.id.button3);
        progressBar = findViewById(R.id.progressBar);

        // birth date
        editTextBirthday = findViewById(R.id.editTextBirthday);

        // Get the current date
        final Calendar calendar = Calendar.getInstance();

        // Set up DatePickerDialog
        final DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Update the EditText with the selected date
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy", Locale.US);
                        calendar.set(year, month, dayOfMonth);
                        String formattedDate = dateFormat.format(calendar.getTime());
                        editTextBirthday.setText(formattedDate);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        // Show DatePickerDialog when the EditText is clicked
        editTextBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupButtonClicked();
            }
        });

        //fillTestData();

    }
    private void fillTestData() {
        editTextLastName.setText("Doe");
        editTextMiddleName.setText("John");
        editTextFirstName.setText("Jane");
        editTextPhoneNumber.setText("09398322222");
        editTextEmail.setText("tester@gmail.com");
        editTextPassword.setText("tester");
        editTextConfirmPassword.setText("tester");
        editTextCivilStatus.setText("Single");
        editTextAge.setText("25");
        editTextBirthday.setText("Dec 24 1998");
        editTextEmergencyContact.setText("Juan Dela Cruz");
        editTextBirthplace.setText("Palo Leyte");
        editTextAddress.setText("Bacoor Cavite");

        // You can call the validation function here to simulate a button click
    }
    public void signupButtonClicked() {
        String txtLastName = editTextLastName.getText().toString().trim();
        String txtFirstName = editTextFirstName.getText().toString().trim();
        String txtMiddleName = editTextMiddleName.getText().toString().trim();
        String txtPhoneNumber = editTextPhoneNumber.getText().toString().trim();
        String txtEmail = editTextEmail.getText().toString().trim();
        String txtPassword = editTextPassword.getText().toString().trim();
        String txtConfirmPassword = editTextConfirmPassword.getText().toString().trim();

        // Additional fields
        String txtCivilStatus = editTextCivilStatus.getText().toString().trim();
        String txtAge = editTextAge.getText().toString().trim();
        String txtBirthday = editTextBirthday.getText().toString().trim();
        String txtBirthplace = editTextBirthplace.getText().toString().trim();
        String txtAddress = editTextAddress.getText().toString().trim();
        String txtEmergencyContact = editTextEmergencyContact.getText().toString().trim();



        if (txtLastName.isEmpty()) {
            editTextLastName.setError("Please Enter Your Last Name");
            editTextLastName.requestFocus();
            return;
        }
        if (txtFirstName.isEmpty()) {
            editTextFirstName.setError("Please Enter Your First Name");
            editTextFirstName.requestFocus();
            return;
        }
        if (txtMiddleName.isEmpty()) {
            editTextMiddleName.setError("Please Enter Your Middle Name");
            editTextMiddleName.requestFocus();
            return;
        }
        if (txtPhoneNumber.isEmpty() || txtPhoneNumber.length() < 11) {
            editTextPhoneNumber.setError("Please Enter a Valid Phone Number with at least 11 digits");
            editTextPhoneNumber.requestFocus();
            return;
        }
        if (txtEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(txtEmail).matches()) {
            editTextEmail.setError("Please Enter a Valid Email");
            editTextEmail.requestFocus();
            return;
        }
        if (txtPassword.isEmpty() || txtPassword.length() < 6) {
            editTextPassword.setError("Please Enter Your Password Containing At Least Six Characters");
            editTextPassword.requestFocus();
            return;
        }
        boolean b = txtConfirmPassword.isEmpty() || !txtConfirmPassword.equals(txtPassword);
        if (b) {
            editTextConfirmPassword.setError("Password does not Match!");
            editTextConfirmPassword.requestFocus();
            return;
        }
        // Validation for phone number
        if (txtPhoneNumber.isEmpty() || txtPhoneNumber.length() < 11) {
            editTextPhoneNumber.setError("Please enter a valid phone number with at least 11 digits");
            editTextPhoneNumber.requestFocus();
            return;
        }

        // Additional field validations
        if (txtCivilStatus.isEmpty()) {
            editTextCivilStatus.setError("Please enter your civil status");
            editTextCivilStatus.requestFocus();
            return;
        }

        // Validate age is not empty and within a reasonable range (you can adjust the range as needed)
        if (txtAge.isEmpty() || Integer.parseInt(txtAge) <= 0 || Integer.parseInt(txtAge) > 150) {
            editTextAge.setError("Please enter a valid age");
            editTextAge.requestFocus();
            return;
        }

        // You can add similar validations for other fields...

        // Validation for email
        if (txtEmail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(txtEmail).matches()) {
            editTextEmail.setError("Please enter a valid email address");
            editTextEmail.requestFocus();
            return;
        }

        // Validation for password
        if (txtPassword.isEmpty() || txtPassword.length() < 6) {
            editTextPassword.setError("Please enter a password with at least 6 characters");
            editTextPassword.requestFocus();
            return;
        }

        // Validation for confirming password
        if (txtConfirmPassword.isEmpty() || !txtConfirmPassword.equals(txtPassword)) {
            editTextConfirmPassword.setError("Passwords do not match");
            editTextConfirmPassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(txtEmail, txtPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("Firebase", "User created successfully");

                            //User user = new User(txtLastName, txtMiddleName, txtFirstName, txtPhoneNumber, txtEmail, txtPassword);
                            User user = new User(txtLastName, txtMiddleName, txtFirstName, txtPhoneNumber, txtEmail, txtPassword, txtConfirmPassword, txtCivilStatus, txtAge, txtBirthday, txtEmergencyContact, txtBirthplace, txtAddress);
                            FirebaseDatabase.getInstance().getReference("users")  // Change to your desired database node
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("Firebase", "User data written to the database successfully");

                                                Toast.makeText(SignUpActivity.this, "Registered Successfully", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Log.e("Firebase", "Failed to write user data to the database", task.getException());

                                                Toast.makeText(SignUpActivity.this, "Register Failed", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                        } else {
                            Log.e("Firebase", "User creation failed", task.getException());

                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                editTextEmail.setError("Email is already registered");
                                editTextEmail.requestFocus();
                            } else {
                                Toast.makeText(SignUpActivity.this, "Registration failed", Toast.LENGTH_LONG).show();
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}

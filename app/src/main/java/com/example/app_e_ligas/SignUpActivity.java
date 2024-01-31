package com.example.app_e_ligas;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.namespace.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
    EditText editTextAge;
    EditText editTextBirthday;
    EditText editTextBirthplace;
    EditText editTextAddress;
    EditText editTextEmergencyContact;
    Spinner spinnerCivilStatus;
    Button buttonRegister;
    Button buttonUploadID;
    ProgressBar progressBar;
    Uri validIDUri;
    ImageView imageViewValidID;
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
        editTextAge = findViewById(R.id.editTextAge);
        editTextBirthday = findViewById(R.id.editTextBirthday);
        editTextBirthplace = findViewById(R.id.editTextBirthplace);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextEmergencyContact = findViewById(R.id.editTextEmergencyContact);
        spinnerCivilStatus = findViewById(R.id.spinnerCivilStatus);
        buttonRegister = findViewById(R.id.button3);
        buttonUploadID = findViewById(R.id.buttonUploadID);
        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();


        imageViewValidID = findViewById(R.id.imageViewValidID);


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

        // Set up civil status spinner
        ArrayAdapter<CharSequence> civilStatusAdapter = ArrayAdapter.createFromResource(this, R.array.civil_status_choices_with_hint, android.R.layout.simple_spinner_item);
        civilStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCivilStatus.setAdapter(civilStatusAdapter);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupButtonClicked();
            }
        });

        buttonUploadID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {
                validIDUri = data.getData();
                imageViewValidID.setImageURI(validIDUri); // Update imageViewValidID with the selected image
            }
        }
    }

    private void signupButtonClicked() {
        String txtLastName = editTextLastName.getText().toString().trim();
        String txtFirstName = editTextFirstName.getText().toString().trim();
        String txtMiddleName = editTextMiddleName.getText().toString().trim();
        String txtPhoneNumber = editTextPhoneNumber.getText().toString().trim();
        String txtEmail = editTextEmail.getText().toString().trim();
        String txtPassword = editTextPassword.getText().toString().trim();
        String txtConfirmPassword = editTextConfirmPassword.getText().toString().trim();

        // Additional fields
        String txtCivilStatus = spinnerCivilStatus.getSelectedItem().toString();
        String txtAge = editTextAge.getText().toString().trim();
        String txtBirthday = editTextBirthday.getText().toString().trim();
        String txtBirthplace = editTextBirthplace.getText().toString().trim();
        String txtAddress = editTextAddress.getText().toString().trim();
        String txtEmergencyContact = editTextEmergencyContact.getText().toString().trim();

        // Validate fields
        // Validation for first name
        if (txtFirstName.isEmpty()) {
            editTextFirstName.setError("Please Enter Your First Name");
            editTextFirstName.requestFocus();
            return;
        }
        // Validation for last name
        if (txtLastName.isEmpty()) {
            editTextLastName.setError("Please Enter Your Last Name");
            editTextLastName.requestFocus();
            return;
        }

        // Validation for phone number
        if (txtPhoneNumber.isEmpty() || txtPhoneNumber.length() < 11) {
            editTextPhoneNumber.setError("Please Enter a Valid Phone Number with at least 11 digits");
            editTextPhoneNumber.requestFocus();
            return;
        }
        // Validation for email
        if (txtEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(txtEmail).matches()) {
            editTextEmail.setError("Please Enter a Valid Email");
            editTextEmail.requestFocus();
            return;
        }
        // Validation for password
        if (txtPassword.isEmpty() || txtPassword.length() < 6) {
            editTextPassword.setError("Please Enter Your Password Containing At Least Six Characters");
            editTextPassword.requestFocus();
            return;
        }
        // Validation for confirming password
        if (!txtConfirmPassword.equals(txtPassword)) {
            editTextConfirmPassword.setError("Passwords do not Match!");
            editTextConfirmPassword.requestFocus();
            return;
        }
        // Validation for civil status
        if (txtCivilStatus.isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Please select your civil status", Toast.LENGTH_SHORT).show();
            return;
        }
        // Validation for age
        if (txtAge.isEmpty() || Integer.parseInt(txtAge) <= 0 || Integer.parseInt(txtAge) > 150) {
            editTextAge.setError("Please enter a valid age");
            editTextAge.requestFocus();
            return;
        }
        // Validation for email
        if (txtEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(txtEmail).matches()) {
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
        if (!txtConfirmPassword.equals(txtPassword)) {
            editTextConfirmPassword.setError("Passwords do not match");
            editTextConfirmPassword.requestFocus();
            return;
        }
        // Start the registration process
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(txtEmail, txtPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("Firebase", "User created successfully");

                            // Upload the valid ID image and save user data
                            uploadValidIDAndSaveUserData(txtLastName, txtMiddleName, txtFirstName, txtPhoneNumber, txtEmail, txtPassword, txtCivilStatus, txtAge, txtBirthday, txtBirthplace, txtAddress, txtEmergencyContact);
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

    private void uploadValidIDAndSaveUserData(String lastName, String middleName, String firstName, String phoneNumber, String email, String password, String civilStatus, String age, String birthday, String birthplace, String address, String emergencyContact) {
        if (validIDUri != null) {
            final StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("valid_ids/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
            storageRef.putFile(validIDUri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String validIDUrl = uri.toString();
                                        saveUserData(lastName, middleName, firstName, phoneNumber, email, password, civilStatus, age, birthday, birthplace, address, emergencyContact, validIDUrl);
                                    }
                                });
                            } else {
                                Toast.makeText(SignUpActivity.this, "Failed to upload valid ID image", Toast.LENGTH_SHORT).show();
                                saveUserData(lastName, middleName, firstName, phoneNumber, email, password, civilStatus, age, birthday, birthplace, address, emergencyContact, null);
                            }
                        }
                    });
        } else {
            saveUserData(lastName, middleName, firstName, phoneNumber, email, password, civilStatus, age, birthday, birthplace, address, emergencyContact, null);
        }
    }

    private void saveUserData(String lastName, String middleName, String firstName, String phoneNumber, String email, String password, String civilStatus, String age, String birthday, String birthplace, String address, String emergencyContact, String validIDUrl) {
        User user = new User(lastName, middleName, firstName, phoneNumber, email, password, civilStatus, age, birthday, emergencyContact, birthplace, address, validIDUrl);
        FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
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
    }
}

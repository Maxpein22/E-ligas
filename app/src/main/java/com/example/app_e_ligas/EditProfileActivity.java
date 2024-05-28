package com.example.app_e_ligas;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.namespace.R;
import com.example.namespace.databinding.ActivityEditProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends DrawerBasedActivity {
    ActivityEditProfileBinding activityEditProfileBinding;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private EditText firstNameEditText, birthPlaceEditText, middleNameEditText, lastNameEditText,
            dateofBirth, phoneNumberEditText, emailEditText;

    private Spinner civilStatusSpinner, genderSpinner;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityEditProfileBinding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(activityEditProfileBinding.getRoot());
        allocateActivityTitle("Profile Update");

        firstNameEditText = findViewById(R.id.etFirstName);
        middleNameEditText = findViewById(R.id.etMiddleName);
        lastNameEditText = findViewById(R.id.etLastName);
        dateofBirth = findViewById(R.id.editTextBirthday);
        civilStatusSpinner = findViewById(R.id.spinnerCivilStatus);
        genderSpinner = findViewById(R.id.spinnerGender);
        birthPlaceEditText = findViewById(R.id.etBirthPlace);
        phoneNumberEditText = findViewById(R.id.etPhoneNumber);
        emailEditText = findViewById(R.id.etEmail);
        btnSave = findViewById(R.id.btnSave);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Initialize Firebase Realtime Database
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        // Load user data
        loadUserData();

        // Set onClickListener for save button
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });
    }

    private void loadUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            mDatabase.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Map<String, Object> userData = (Map<String, Object>) dataSnapshot.getValue();
                        if (userData != null) {
                            // Populate EditTexts with user data
                            firstNameEditText.setText((String) userData.get("userFirstName"));
                            middleNameEditText.setText((String) userData.get("userMiddleName"));
                            lastNameEditText.setText((String) userData.get("userLastName"));
                            dateofBirth.setText((String) userData.get("birthday"));
                            birthPlaceEditText.setText((String) userData.get("birthPlace"));
                            phoneNumberEditText.setText((String) userData.get("userPhoneNumber"));
                            emailEditText.setText((String) userData.get("userEmail"));

                            // Set spinner selections
                            setSpinnerSelection(civilStatusSpinner, (String) userData.get("civilStatus"));
                            setSpinnerSelection(genderSpinner, (String) userData.get("gender"));
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(EditProfileActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setSpinnerSelection(Spinner spinner, String value) {
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        if (adapter != null) {
            int position = adapter.getPosition(value);
            if (position != -1) {
                spinner.setSelection(position);
            }
        }
    }

    private void saveProfile() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Retrieve entered profile information
            String firstName = firstNameEditText.getText().toString();
            String middleName = middleNameEditText.getText().toString();
            String lastName = lastNameEditText.getText().toString();
            String birthday = dateofBirth.getText().toString();
            String birthPlace = birthPlaceEditText.getText().toString();
            String contactNumber = phoneNumberEditText.getText().toString();
            String civilStatus = civilStatusSpinner.getSelectedItem().toString();

            // Create a Map to store the user data
            Map<String, Object> userData = new HashMap<>();
            userData.put("userFirstName", firstName);
            userData.put("userMiddleName", middleName);
            userData.put("userLastName", lastName);
            userData.put("birthday", birthday);
            userData.put("birthPlace", birthPlace);
            userData.put("userPhoneNumber", contactNumber);
            userData.put("civilStatus", civilStatus);

            // Update the user data in the database
            mDatabase.child(userId).updateChildren(userData)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(EditProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                                // Set result to indicate profile update success
                                setResult(RESULT_OK);
                                finish(); // Finish the activity
                            } else {
                                Toast.makeText(EditProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(EditProfileActivity.this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }
}

package com.example.app_e_ligas;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
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

        dateofBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
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
            String gender = genderSpinner.getSelectedItem().toString();

            int age = calculateAge(birthday);
            String ageString = String.valueOf(age);



            // Create a Map to store the user data
            Map<String, Object> userData = new HashMap<>();
            userData.put("userFirstName", firstName);
            userData.put("userMiddleName", middleName);
            userData.put("userLastName", lastName);
            userData.put("birthday", birthday);
            userData.put("birthPlace", birthPlace);
            userData.put("userPhoneNumber", contactNumber);
            userData.put("civilStatus", civilStatus);
            userData.put("age", ageString);
            userData.put("gender", gender);


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

    public void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Format selected date as "MMMM dd, yyyy"
                        String selectedDate = String.format(Locale.getDefault(), "%s %02d, %04d",
                                new DateFormatSymbols().getMonths()[monthOfYear], dayOfMonth, year);

                        // Update the EditText field with selected date
                        dateofBirth.setText(selectedDate);
                    }
                }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private int calculateAge(String birthday) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        try {
            Date birthDate = sdf.parse(birthday);
            Calendar birthCalendar = Calendar.getInstance();
            birthCalendar.setTime(birthDate);

            Calendar today = Calendar.getInstance();

            int age = today.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR);

            if (today.get(Calendar.DAY_OF_YEAR) < birthCalendar.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }

            return age;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0; // Return 0 if parsing fails
        }
    }

}
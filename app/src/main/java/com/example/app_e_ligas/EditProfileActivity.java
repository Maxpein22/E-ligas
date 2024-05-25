package com.example.app_e_ligas;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.namespace.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditProfileActivity extends AppCompatActivity {

    private EditText firstNameEditText, middleNameEditText, lastNameEditText,
            ageEditText, birthPlaceEditText,
            contactNumberEditText, emailEditText, locationEditText;
    private TextView birthdayTextView; // Changed from EditText to TextView
    private Spinner civilStatusSpinner;
    private Button saveButton;

    private Spinner locationSpinner; // Added for location
    private EditText blockEditText; // Added for block
    private EditText lotEditText; // Added for lot

    // Firebase variables
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private DatePickerDialog.OnDateSetListener dateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Initialize Firebase Realtime Database
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        // Bind views
        firstNameEditText = findViewById(R.id.firstNameEditText);
        middleNameEditText = findViewById(R.id.middleNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        birthdayTextView = findViewById(R.id.birthdayEditText); // Updated ID
        birthPlaceEditText = findViewById(R.id.birthPlaceEditText);
        contactNumberEditText = findViewById(R.id.contactNumberEditText);
        civilStatusSpinner = findViewById(R.id.spinnerCivilStatus);
        locationSpinner = findViewById(R.id.spinnerLocation); // Added for location
        blockEditText = findViewById(R.id.editTextBlock); // Added for block
        lotEditText = findViewById(R.id.editTextLot); // Added for lot
        saveButton = findViewById(R.id.button8);

        // Populate civil status spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.civil_status_choices_with_hint, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        civilStatusSpinner.setAdapter(adapter);

        // Set date picker for birthday
        setUpDatePicker();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });

        // Load user data
        loadUserData();
    }

    private void setUpDatePicker() {
        // Get current date
        final Calendar calendar = Calendar.getInstance();

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);

                // Check if the selected date is today's date
                if (selectedDate.after(calendar)) {
                    // If it's a future date, show a message and return
                    Toast.makeText(EditProfileActivity.this, "Please select a valid date", Toast.LENGTH_SHORT).show();
                    return;
                }

                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
                birthdayTextView.setText(sdf.format(selectedDate.getTime())); // Update TextView
            }
        };

        birthdayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditProfileActivity.this, dateSetListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
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
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            firstNameEditText.setText(user.getUserFirstName());
                            middleNameEditText.setText(user.getUserMiddleName());
                            lastNameEditText.setText(user.getUserLastName());
                            birthdayTextView.setText(user.getBirthday());

                            birthPlaceEditText.setText(user.getBirthPlace());
                            contactNumberEditText.setText(user.getUserPhoneNumber());

                            // Set the spinner selection
                            ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) civilStatusSpinner.getAdapter();
                            if (user.getCivilStatus() != null) {
                                int spinnerPosition = adapter.getPosition(user.getCivilStatus());
                                civilStatusSpinner.setSelection(spinnerPosition);
                            }


                            // Set the location spinner selection
                            ArrayAdapter<CharSequence> locationAdapter = (ArrayAdapter<CharSequence>) locationSpinner.getAdapter();
                            if (user.getLocation() != null) {
                                int spinnerPosition = locationAdapter.getPosition(user.getLocation());
                                locationSpinner.setSelection(spinnerPosition);
                            }

                            // Set block and lot
                            blockEditText.setText(user.getBlock());
                            lotEditText.setText(user.getLot());
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

    private void saveProfile() {
        // Retrieve values from EditText fields
        String firstName = firstNameEditText.getText().toString();
        String middleName = middleNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String civilStatus = civilStatusSpinner.getSelectedItem().toString();
        String birthday = birthdayTextView.getText().toString(); // Use TextView instead of EditText
        String birthPlace = birthPlaceEditText.getText().toString();
        String contactNumber = contactNumberEditText.getText().toString();
        String location = locationSpinner.getSelectedItem().toString(); // Retrieve selected location from spinner
        String block = blockEditText.getText().toString(); // Retrieve block
        String lot = lotEditText.getText().toString(); // Retrieve lot

        String address = location + " blk " + block + " lot " + lot;

        String age = String.valueOf(calculateAge(birthday));




        // Get current user
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Retrieve user ID
            String userId = currentUser.getUid();



            // Retrieve the current user data from the database
            mDatabase.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Get the current user object
                        User user = dataSnapshot.getValue(User.class);

                        // Update only the fields that have been modified
                        if (!firstName.isEmpty()) {
                            user.setUserFirstName(firstName);
                        }
                        if (!middleName.isEmpty()) {
                            user.setUserMiddleName(middleName);
                        }
                        if (!lastName.isEmpty()) {
                            user.setUserLastName(lastName);
                        }
                        if (!civilStatus.isEmpty()) {
                            user.setCivilStatus(civilStatus);
                        }
                        if (!birthday.isEmpty()) {
                            user.setBirthday(birthday);
                        }

                        if (!birthPlace.isEmpty()) {
                            user.setBirthPlace(birthPlace);
                        }
                        if (!contactNumber.isEmpty()) {
                            user.setUserPhoneNumber(contactNumber);
                        }
                        if (!location.isEmpty()) {
                            user.setLocation(location);
                        }
                        if (!block.isEmpty()) {
                            user.setBlock(block);
                        }
                        if (!lot.isEmpty()) {
                            user.setLot(lot);
                        }

                        user.setAddress(address);

                        user.setAge(age);



                        // Update the user data in the database
                        mDatabase.child(userId).setValue(user)
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
                        Toast.makeText(EditProfileActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(EditProfileActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(EditProfileActivity.this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private int calculateAge(String birthday) {
        // Parse the birthday string to get the year, month, and day
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
        Calendar birthDate = Calendar.getInstance();
        try {
            birthDate.setTime(sdf.parse(birthday));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Get current date
        Calendar currentDate = Calendar.getInstance();

        // Calculate age
        int age = currentDate.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
        if (currentDate.get(Calendar.MONTH) < birthDate.get(Calendar.MONTH)
                || (currentDate.get(Calendar.MONTH) == birthDate.get(Calendar.MONTH)
                && currentDate.get(Calendar.DAY_OF_MONTH) < birthDate.get(Calendar.DAY_OF_MONTH))) {
            age--;
        }
        return age;
    }
}
package com.example.app_e_ligas;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.namespace.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
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

    EditText editTextBirthday;
    EditText editTextBirthplace;
    EditText editTextEmergencyContact;
    EditText editTextEmergencyContactNo;
    Spinner spinnerCivilStatus;
    Spinner spinnerGender;

    Button buttonRegister;

    ProgressBar progressBar;
    Uri validIDUri;
    ImageView imageViewValidID;
    Button buttonUploadID;

    Spinner spinnerLocation;
    EditText editTextBlock;
    EditText editTextLot;

    EditText editTextStreet;

    EditText editTextAlias;

    Spinner spinnerVoterStauts;
    Spinner spinnerOccupation;

    Spinner spinnerResidentialStatus;
    private FirebaseAuth mAuth;

    private TextView textFileSize;

    String fcmToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
            if (!TextUtils.isEmpty(token)) {
                Log.d(TAG, "retrieve token successful : " + token);
            } else{
                Log.w(TAG, "token should not be null...");
            }
        }).addOnFailureListener(e -> {
            //handle e
        }).addOnCanceledListener(() -> {
            //handle cancel
        }).addOnCompleteListener((task) -> fcmToken = task.getResult());

        editTextLastName = findViewById(R.id.editTextLastName);
        editTextMiddleName = findViewById(R.id.editTextMiddleName);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);

        editTextBirthday = findViewById(R.id.editTextBirthday);
        editTextBirthplace = findViewById(R.id.editTextBirthplace);
        editTextEmergencyContact = findViewById(R.id.editTextEmergencyContact);
        editTextEmergencyContactNo = findViewById(R.id.editTextEmergencyContactNo);
        spinnerCivilStatus = findViewById(R.id.spinnerCivilStatus);
        spinnerGender = findViewById(R.id.spinnerGender);
        buttonRegister = findViewById(R.id.button3);
        buttonUploadID = findViewById(R.id.buttonUploadID);
        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();

        imageViewValidID = findViewById(R.id.imageViewValidID);

        textFileSize = findViewById(R.id.textFileSize);

        spinnerLocation = findViewById(R.id.spinnerLocation);
        editTextBlock = findViewById(R.id.editTextBlock);
        editTextLot = findViewById(R.id.editTextLot);
        editTextStreet = findViewById(R.id.editTextStreet);
        editTextBirthday = findViewById(R.id.editTextBirthday);
        editTextAlias = findViewById(R.id.editTextAlias);
        spinnerVoterStauts = findViewById(R.id.spinnerVoterStauts);
        spinnerOccupation = findViewById(R.id.spinnerOccupation);
        spinnerResidentialStatus = findViewById(R.id.spinnerResidentialStatus);



        // Password visibility toggling
        final ImageView showPasswordImageView = findViewById(R.id.imageButtonShowPassword);
        final ImageView showConfirmPasswordImageView = findViewById(R.id.imageViewShowConfirmPassword);
        final EditText passwordEditText = findViewById(R.id.editTextPassword);
        final EditText confirmPasswordEditText = findViewById(R.id.editTextConfirmPassword);
        showPasswordImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility(passwordEditText, showPasswordImageView);
            }
        });

        showConfirmPasswordImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility(confirmPasswordEditText, showConfirmPasswordImageView);
            }
        });

        // Setup text views
        setupTextView(findViewById(R.id.textF), "First Name*", "This field is required");
        setupTextView(findViewById(R.id.textL), "Last Name*", "This field is required");
        setupTextView(findViewById(R.id.textP), "Phone Number*", "This field is required");
        setupTextView(findViewById(R.id.textCS), "Select Civil Status*", "This field is required");
        setupTextView(findViewById(R.id.gender), "Gender*", "This field is required");
        setupTextView(findViewById(R.id.textAdd), "Address*", "This field is required");
        setupTextView(findViewById(R.id.textBd), "Birthday*", "This field is required");
        setupTextView(findViewById(R.id.textBp), "Birth Place*", "This field is required");
        setupTextView(findViewById(R.id.textAlias), "Alias*", "This field is required");
        setupTextView(findViewById(R.id.textVoter), "Voter Status*", "This field is required");
        setupTextView(findViewById(R.id.textOccupation), "Occupation*", "This field is required");
        setupTextView(findViewById(R.id.textResidentialStatus), "Residential Status*", "This field is required");

        setupTextView(findViewById(R.id.textECP), "Emergency Contact Person*", "This field is required");
        setupTextView(findViewById(R.id.textECN), "Emergency Contact Number*", "This field is required");
        setupTextView(findViewById(R.id.textEL), "Email*", "This field is required");
        setupTextView(findViewById(R.id.textPass), "Password*", "This field is required");
        setupTextView(findViewById(R.id.textCpass), "Confirm Password*", "This field is required");
        setupTextView(findViewById(R.id.checkBoxTerms), "I agree to the Terms and Conditions*", "This field is required");


        // birth date


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
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, month, dayOfMonth);

                        // Check if selected date is today or in the future
                        if (selectedDate.after(Calendar.getInstance())) {
                            Toast.makeText(SignUpActivity.this, "Please select a valid date", Toast.LENGTH_SHORT).show();
                        } else {
                            String formattedDate = dateFormat.format(selectedDate.getTime());
                            editTextBirthday.setText(formattedDate);
                        }
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        // Set the maximum date to yesterday
        final Calendar maxCalendar = Calendar.getInstance();
        maxCalendar.add(Calendar.DAY_OF_MONTH, -1);
        datePickerDialog.getDatePicker().setMaxDate(maxCalendar.getTimeInMillis());

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

        ArrayAdapter<CharSequence> GenderAdapter = ArrayAdapter.createFromResource(this, R.array.sex_choices, android.R.layout.simple_spinner_item);
        GenderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(GenderAdapter);

        ArrayAdapter<CharSequence> VoterStutsAdapter = ArrayAdapter.createFromResource(this, R.array.voter_status, android.R.layout.simple_spinner_item);
        VoterStutsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVoterStauts.setAdapter(VoterStutsAdapter);

        ArrayAdapter<CharSequence> OccupationAdapter = ArrayAdapter.createFromResource(this, R.array.occupation, android.R.layout.simple_spinner_item);
        OccupationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOccupation.setAdapter(OccupationAdapter);

        ArrayAdapter<CharSequence> ResidentialStatusAdapter = ArrayAdapter.createFromResource(this, R.array.resident_status, android.R.layout.simple_spinner_item);
        ResidentialStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerResidentialStatus.setAdapter(ResidentialStatusAdapter);



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

        // Checkbox for terms and conditions
        CheckBox checkBoxTerms = findViewById(R.id.checkBoxTerms);
        checkBoxTerms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Show terms and conditions dialog
                    showTermsAndConditionsDialog();
                }
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

                // Calculate file size and display it
                Cursor cursor = getContentResolver().query(validIDUri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                    long fileSize = cursor.getLong(sizeIndex);
                    cursor.close();

                    double fileSizeInMB = (double) fileSize / (1024 * 1024);
                    if (fileSizeInMB > 15) {
                        // File size exceeds 15 MB, show toast message
                        Toast.makeText(getApplicationContext(), "File size exceeds 5 MB. Please select a smaller image.", Toast.LENGTH_SHORT).show();
                        imageViewValidID.setImageResource(0); // Clear the image view
                    } else {
                        String formattedSize = String.format("%.2f", fileSizeInMB);
                        textFileSize.setText("File Size: " + formattedSize + " MB");
                    }
                }
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

        String txtBirthday = editTextBirthday.getText().toString().trim();
        String txtBirthplace = editTextBirthplace.getText().toString().trim();
        String txtEmergencyContact = editTextEmergencyContact.getText().toString().trim();
        String txtEmergencyContactNo = editTextEmergencyContactNo.getText().toString().trim();

        String location = spinnerLocation.getSelectedItem().toString();
        String block = editTextBlock.getText().toString().trim();
        String lot = editTextLot.getText().toString().trim();
        String street = editTextStreet.getText().toString().trim();
        String alias = editTextAlias.getText().toString().trim();

        String voters = spinnerVoterStauts.getSelectedItem().toString().trim();
        String occupation = spinnerOccupation.getSelectedItem().toString().trim();





        if (alias.isEmpty()) {
            editTextAlias.setError("Please Enter Your Alias");
            editTextAlias.requestFocus();
            return;
        }
        if (street.isEmpty()) {
            editTextStreet.setError("Please Enter Your Street");
            editTextStreet.requestFocus();
            return;
        }
        if (block.isEmpty()) {
            editTextBlock.setError("Please Enter Your Blk");
            editTextBlock.requestFocus();
            return;
        }
        if (lot.isEmpty()) {
            editTextLot.setError("Please Enter Your Blk");
            editTextLot.requestFocus();
            return;
        }

        //  validation code...
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
        // Validate if the checkbox is checked
        CheckBox checkBoxTerms = findViewById(R.id.checkBoxTerms);
        if (!checkBoxTerms.isChecked()) {
            Toast.makeText(SignUpActivity.this, "Please accept the terms and conditions", Toast.LENGTH_SHORT).show();
            return;
        }


        // Validate if the selected date is not in the future
        Calendar selectedDate = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.US);
        try {
            selectedDate.setTime(sdf.parse(txtBirthday));
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(SignUpActivity.this, "Please enter a valid birthday", Toast.LENGTH_SHORT).show();
            return;
        }
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        if (selectedDate.after(today)) {
            Toast.makeText(SignUpActivity.this, "Please select a valid date", Toast.LENGTH_SHORT).show();
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
                            uploadValidIDAndSaveUserData(txtLastName, txtMiddleName, txtFirstName, txtPhoneNumber, txtEmail, txtPassword, txtCivilStatus, txtBirthday, txtBirthplace, txtEmergencyContact, txtEmergencyContactNo);
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

    private void showTermsAndConditionsDialog() {
        // Create a dialog
        Dialog dialog = new Dialog(this
        );
        dialog.setContentView(R.layout.floating_terms_layout);
        dialog.setCancelable(true);
        // Find views in the dialog layout
        Button closeButton = dialog.findViewById(R.id.closeButton);
        closeButton.setVisibility(View.GONE); // Initially hide the close button

        // ScrollView and its listener
        ScrollView scrollView = dialog.findViewById(R.id.termsScrollView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    // Check if the ScrollView has reached the bottom
                    if (scrollView.getChildAt(0).getBottom() <= (scrollView.getHeight() + scrollView.getScrollY())) {
                        // ScrollView is at the bottom, make the close button visible
                        closeButton.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

        // Set click listener for the close button
        closeButton.setOnClickListener(v -> dialog.dismiss());

        // Show the dialog
        dialog.show();
    }


    private void uploadValidIDAndSaveUserData(String lastName, String middleName, String firstName, String phoneNumber, String email, String password, String civilStatus, String birthday, String birthplace, String emergencyContact, String emergencyContactNo) {
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
                                        // Save user data without setting validated to true
                                        saveUserData(lastName, middleName, firstName, phoneNumber, email, password, civilStatus, birthday, birthplace, emergencyContact, emergencyContactNo, validIDUrl, false);
                                    }
                                });
                            } else {
                                Toast.makeText(SignUpActivity.this, "Failed to upload valid ID image", Toast.LENGTH_SHORT).show();
                                // If image upload failed, save user data without a valid ID URL
                                saveUserData(lastName, middleName, firstName, phoneNumber, email, password, civilStatus, birthday, birthplace, emergencyContact, emergencyContactNo, null, false);
                            }
                        }
                    });
        } else {
            // If no valid ID is provided
            saveUserData(lastName, middleName, firstName, phoneNumber, email, password, civilStatus, birthday, birthplace, emergencyContact, emergencyContactNo, null, false);
        }
    }

    private void saveUserData(String lastName, String middleName, String firstName, String phoneNumber, String email, String password, String civilStatus, String birthday, String birthplace, String emergencyContact, String emergencyContactNo, String validIDUrl, boolean validated) {
        // Retrieve additional data from the EditText fields
        String location = spinnerLocation.getSelectedItem().toString();
        String block = editTextBlock.getText().toString().trim();
        String street = editTextStreet.getText().toString().trim();
        String alias = editTextAlias.getText().toString().trim();
        String voters = spinnerVoterStauts.getSelectedItem().toString();
        String occupation = spinnerOccupation.getSelectedItem().toString().trim();
        String resident_status = spinnerResidentialStatus.getSelectedItem().toString().trim();

        String lot = editTextLot.getText().toString().trim();
        String address = location + " blk " + block + " lot " + lot + " street " + street;
        String gender = spinnerGender.getSelectedItem().toString();
        String type_employment = occupation.equalsIgnoreCase("Unemployed") ? "unemployed" : "employed";

        // Calculate age from the birth date
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.US);
        Calendar dob = Calendar.getInstance();
        try {
            dob.setTime(sdf.parse(birthday));
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(SignUpActivity.this, "Please enter a valid birthday", Toast.LENGTH_SHORT).show();
            return;
        }
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        String Age = String.valueOf(age);


        // Create a User object with all the fields, including the validated flag and additional data
        User user = new User(lastName, middleName, firstName, phoneNumber, email, password, civilStatus, birthday, emergencyContact, emergencyContactNo, birthplace, validIDUrl, validated, location, block, lot, address, Age, gender, fcmToken, street, alias,voters,occupation, resident_status, type_employment);

        // Get a reference to the Firebase Database and save the User object
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


    // Helper function to set spannable text and click listener for a TextView
    private void setupTextView(TextView textView, String text, String toastMessage) {
        // Create spannable string and color span
        SpannableString spannableString = new SpannableString(text);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.RED);
        int index = text.indexOf("*");
        if (index != -1) {
            spannableString.setSpan(colorSpan, index, index + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textView.setText(spannableString);

        // Set click listener to show a toast message
        textView.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
        });
    }

    private void togglePasswordVisibility(EditText editText, ImageView imageView) {
        if (editText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)) {
            // Password is currently visible, so hide it
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            imageView.setImageResource(R.drawable.baseline_remove_red_eye_24); // Set hide password icon
        } else {
            // Password is currently hidden, so show it
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            imageView.setImageResource(R.drawable.baseline_visibility_off_24); // Set show password icon
        }
        // Move cursor to the end of the text
        editText.setSelection(editText.getText().length());
    }


}
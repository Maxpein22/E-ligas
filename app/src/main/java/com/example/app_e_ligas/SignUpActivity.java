package com.example.app_e_ligas;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
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
    EditText editTextAge;
    EditText editTextBirthday;
    EditText editTextBirthplace;
    EditText editTextAddress;
    EditText editTextEmergencyContact;
    EditText editTextEmergencyContactNo;
    Spinner spinnerCivilStatus;
    Button buttonRegister;

    ProgressBar progressBar;
    Uri validIDUri;
    ImageView imageViewValidID;
    Button buttonUploadID;
    private FirebaseAuth mAuth;

    private TextView textFileSize;


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
        editTextEmergencyContactNo = findViewById(R.id.editTextEmergencyContactNo);
        spinnerCivilStatus = findViewById(R.id.spinnerCivilStatus);
        buttonRegister = findViewById(R.id.button3);
        buttonUploadID = findViewById(R.id.buttonUploadID);
        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();

        imageViewValidID = findViewById(R.id.imageViewValidID);

        textFileSize = findViewById(R.id.textFileSize);


        // Password visibility toggling
        final CheckBox showPasswordCheckbox = findViewById(R.id.showPasswordCheckbox);
        final CheckBox showConfirmPasswordCheckbox = findViewById(R.id.showConfirmPasswordCheckbox);
        final EditText passwordEditText = findViewById(R.id.editTextPassword);
        final EditText confirmPasswordEditText = findViewById(R.id.editTextConfirmPassword);

        showPasswordCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        showConfirmPasswordCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    confirmPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    confirmPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });


        TextView textView = findViewById(R.id.textF); // Assuming you have assigned an id to the TextView in your XML layout
        String text = "First Name*";
        SpannableString spannableString = new SpannableString(text);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.RED);
        spannableString.setSpan(colorSpan, text.indexOf("*"), text.indexOf("*") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "This field is required", Toast.LENGTH_SHORT).show();
            }
        });

        TextView textView2 = findViewById(R.id.textL); // Assuming you have assigned an id to the TextView in your XML layout
        String text2 = "Last Name*";
        SpannableString spannableString2 = new SpannableString(text2);
        ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(Color.RED);
        spannableString2.setSpan(colorSpan2, text2.indexOf("*"), text2.indexOf("*") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView2.setText(spannableString2);
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "This field is required", Toast.LENGTH_SHORT).show();
            }
        });

        TextView textView3 = findViewById(R.id.textP); // Assuming you have assigned an id to the TextView in your XML layout
        String text3 = "Phone Number*";
        SpannableString spannableString3 = new SpannableString(text3);
        ForegroundColorSpan colorSpan3 = new ForegroundColorSpan(Color.RED);
        spannableString3.setSpan(colorSpan3, text3.indexOf("*"), text3.indexOf("*") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView3.setText(spannableString3);
        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "This field is required", Toast.LENGTH_SHORT).show();
            }
        });

        TextView textView4 = findViewById(R.id.textCS); // Assuming you have assigned an id to the TextView in your XML layout
        String text4 = "Select Civil Status*";
        SpannableString spannableString4 = new SpannableString(text4);
        ForegroundColorSpan colorSpan4 = new ForegroundColorSpan(Color.RED);
        spannableString4.setSpan(colorSpan4, text4.indexOf("*"), text4.indexOf("*") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView4.setText(spannableString4);
        textView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "This field is required", Toast.LENGTH_SHORT).show();
            }
        });

        TextView textView5 = findViewById(R.id.textAg); // Assuming you have assigned an id to the TextView in your XML layout
        String text5 = "Age*";
        SpannableString spannableString5 = new SpannableString(text5);
        ForegroundColorSpan colorSpan5 = new ForegroundColorSpan(Color.RED);
        spannableString5.setSpan(colorSpan5, text5.indexOf("*"), text5.indexOf("*") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView5.setText(spannableString5);
        textView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "This field is required", Toast.LENGTH_SHORT).show();
            }
        });

        TextView textView6 = findViewById(R.id.textBd); // Assuming you have assigned an id to the TextView in your XML layout
        String text6 = "Birthday*";
        SpannableString spannableString6 = new SpannableString(text6);
        ForegroundColorSpan colorSpan6 = new ForegroundColorSpan(Color.RED);
        spannableString6.setSpan(colorSpan6, text6.indexOf("*"), text6.indexOf("*") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView6.setText(spannableString6);
        textView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "This field is required", Toast.LENGTH_SHORT).show();
            }
        });

        TextView textView7 = findViewById(R.id.textBp); // Assuming you have assigned an id to the TextView in your XML layout
        String text7 = "Birth Place*";
        SpannableString spannableString7 = new SpannableString(text7);
        ForegroundColorSpan colorSpan7 = new ForegroundColorSpan(Color.RED);
        spannableString7.setSpan(colorSpan7, text7.indexOf("*"), text7.indexOf("*") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView7.setText(spannableString7);
        textView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "This field is required", Toast.LENGTH_SHORT).show();
            }
        });

        TextView textView8 = findViewById(R.id.textAdd); // Assuming you have assigned an id to the TextView in your XML layout
        String text8 = "Address*";
        SpannableString spannableString8 = new SpannableString(text8);
        ForegroundColorSpan colorSpan8 = new ForegroundColorSpan(Color.RED);
        spannableString8.setSpan(colorSpan8, text8.indexOf("*"), text8.indexOf("*") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView8.setText(spannableString8);
        textView8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "This field is required", Toast.LENGTH_SHORT).show();
            }
        });

        TextView textView9 = findViewById(R.id.textECP); // Assuming you have assigned an id to the TextView in your XML layout
        String text9 = "Emergency Contact Person*";
        SpannableString spannableString9 = new SpannableString(text9);
        ForegroundColorSpan colorSpan9 = new ForegroundColorSpan(Color.RED);
        spannableString9.setSpan(colorSpan9, text9.indexOf("*"), text9.indexOf("*") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView9.setText(spannableString9);
        textView9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "This field is required", Toast.LENGTH_SHORT).show();
            }
        });

        TextView textView10 = findViewById(R.id.textECN); // Assuming you have assigned an id to the TextView in your XML layout
        String text10 = "Emergency Contact Number*";
        SpannableString spannableString10 = new SpannableString(text10);
        ForegroundColorSpan colorSpan10 = new ForegroundColorSpan(Color.RED);
        spannableString10.setSpan(colorSpan10, text10.indexOf("*"), text10.indexOf("*") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView10.setText(spannableString10);
        textView10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "This field is required", Toast.LENGTH_SHORT).show();
            }
        });

        TextView textView11 = findViewById(R.id.textEL); // Assuming you have assigned an id to the TextView in your XML layout
        String text11 = "Email*";
        SpannableString spannableString11 = new SpannableString(text11);
        ForegroundColorSpan colorSpan11 = new ForegroundColorSpan(Color.RED);
        spannableString11.setSpan(colorSpan11, text11.indexOf("*"), text11.indexOf("*") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView11.setText(spannableString11);
        textView11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "This field is required", Toast.LENGTH_SHORT).show();
            }
        });

        TextView textView12 = findViewById(R.id.textPass); // Assuming you have assigned an id to the TextView in your XML layout
        String text12 = "Password*";
        SpannableString spannableString12 = new SpannableString(text12);
        ForegroundColorSpan colorSpan12 = new ForegroundColorSpan(Color.RED);
        spannableString12.setSpan(colorSpan12, text12.indexOf("*"), text12.indexOf("*") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView12.setText(spannableString12);
        textView12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "This field is required", Toast.LENGTH_SHORT).show();
            }
        });

        TextView textView13 = findViewById(R.id.textCpass); // Assuming you have assigned an id to the TextView in your XML layout
        String text13 = "Confirm Password*";
        SpannableString spannableString13 = new SpannableString(text13);
        ForegroundColorSpan colorSpan13 = new ForegroundColorSpan(Color.RED);
        spannableString13.setSpan(colorSpan13, text13.indexOf("*"), text13.indexOf("*") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView13.setText(spannableString13);
        textView13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "This field is required", Toast.LENGTH_SHORT).show();
            }
        });

        TextView textView14 = findViewById(R.id.textUVI); // Assuming you have assigned an id to the TextView in your XML layout
        String text14 = "UPLOAD VALID ID*";
        SpannableString spannableString14 = new SpannableString(text14);
        ForegroundColorSpan colorSpan14 = new ForegroundColorSpan(Color.RED);
        spannableString14.setSpan(colorSpan14, text14.indexOf("*"), text14.indexOf("*") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView14.setText(spannableString14);
        textView14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "This field is required", Toast.LENGTH_SHORT).show();
            }
        });

        TextView textView15 = findViewById(R.id.checkBoxTerms); // Assuming you have assigned an id to the TextView in your XML layout
        String text15 = "I agree to the Terms and Conditions*";
        SpannableString spannableString15 = new SpannableString(text15);
        ForegroundColorSpan colorSpan15 = new ForegroundColorSpan(Color.RED);
        spannableString15.setSpan(colorSpan15, text15.indexOf("*"), text15.indexOf("*") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView15.setText(spannableString15);
        textView15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "This field is required", Toast.LENGTH_SHORT).show();
            }
        });


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
                    if (fileSizeInMB > 5) {
                        // File size exceeds 5 MB, show toast message
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
        String txtAge = editTextAge.getText().toString().trim();
        String txtBirthday = editTextBirthday.getText().toString().trim();
        String txtBirthplace = editTextBirthplace.getText().toString().trim();
        String txtAddress = editTextAddress.getText().toString().trim();
        String txtEmergencyContact = editTextEmergencyContact.getText().toString().trim();
        String txtEmergencyContactNo = editTextEmergencyContactNo.getText().toString().trim();


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
        // Validate if the checkbox is checked
        CheckBox checkBoxTerms = findViewById(R.id.checkBoxTerms);
        if (!checkBoxTerms.isChecked()) {
            Toast.makeText(SignUpActivity.this, "Please accept the terms and conditions", Toast.LENGTH_SHORT).show();
            return;
        }

        if (validIDUri == null) {
            Toast.makeText(SignUpActivity.this, "Please upload a valid ID photo", Toast.LENGTH_SHORT).show();
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
                            uploadValidIDAndSaveUserData(txtLastName, txtMiddleName, txtFirstName, txtPhoneNumber, txtEmail, txtPassword, txtCivilStatus, txtAge, txtBirthday, txtBirthplace, txtAddress, txtEmergencyContact, txtEmergencyContactNo);
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
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.floating_terms_layout);
        dialog.setCancelable(true); // Set if the dialog can be canceled by clicking outside

        // Find views in the dialog layout
        Button closeButton = dialog.findViewById(R.id.closeButton);

        // Set click listener for the close button
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // Dismiss the dialog when the close button is clicked
            }
        });

        // Show the dialog
        dialog.show();
    }

    private void uploadValidIDAndSaveUserData(String lastName, String middleName, String firstName, String phoneNumber, String email, String password, String civilStatus, String age, String birthday, String birthplace, String address, String emergencyContact, String emergencyContactNo) {
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
                                        saveUserData(lastName, middleName, firstName, phoneNumber, email, password, civilStatus, age, birthday, birthplace, address, emergencyContact, emergencyContactNo, validIDUrl);
                                    }
                                });
                            } else {
                                Toast.makeText(SignUpActivity.this, "Failed to upload valid ID image", Toast.LENGTH_SHORT).show();
                                saveUserData(lastName, middleName, firstName, phoneNumber, email, password, civilStatus, age, birthday, birthplace, address, emergencyContact, emergencyContactNo, null);
                            }
                        }
                    });
        } else {
            saveUserData(lastName, middleName, firstName, phoneNumber, email, password, civilStatus, age, birthday, birthplace, address, emergencyContact, emergencyContactNo, null);
        }
    }

    private void saveUserData(String lastName, String middleName, String firstName, String phoneNumber, String email, String password, String civilStatus, String age, String birthday, String birthplace, String address, String emergencyContact, String emergencyContactNo, String validIDUrl) {
        // You need to add the userProfileImage field here
        String userProfileImage = null; // Initially set to null, update it if you have a profile image URL

        // Create a User object with all the fields
        User user = new User(lastName, middleName, firstName, phoneNumber, email, password, civilStatus, age, birthday, emergencyContact, emergencyContactNo, birthplace, address, validIDUrl, userProfileImage);

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

}

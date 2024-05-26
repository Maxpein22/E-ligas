package com.example.app_e_ligas;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.namespace.R;
import com.example.namespace.databinding.ActivityBarangayCencusBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class barangay_cencus extends DrawerBasedActivity {
    private ActivityBarangayCencusBinding activityBarangayCencusBinding;
    private EditText editTextBirthday, etFirstName, etMiddleName, etLastName, House_blk_lot, St_Purok_Sitio_Subd,
            barangay, City_Municipality, province, place_birth, Height, Weight, Nationality, company_name,
            duration, address_company, elem_school_name, elem_school_address, high_school_name, high_school_address,
            voc_school_name, voc_school_address, college_school_name, college_school_address;
    private Spinner spinnerGender, spinnerCivilStatus, spinner_vacine, spinnerPWD, House_type, Solo_Parent;
    Button btnNext1, btnNext2, btnSubmit, btnBack1,btnBack2;
    private DatabaseReference usersRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBarangayCencusBinding = ActivityBarangayCencusBinding.inflate(getLayoutInflater());
        setContentView(activityBarangayCencusBinding.getRoot());
        allocateActivityTitle("Barangay Census");

        btnNext1 = findViewById(R.id.btnNext1);
        btnNext2 = findViewById(R.id.btnNext2);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnBack1 = findViewById(R.id.btnBack1);
        btnBack2 = findViewById(R.id.btnBack2);


        spinnerGender = findViewById(R.id.spinnerGender);
        spinnerCivilStatus = findViewById(R.id.spinnerCivilStatus);
        spinner_vacine = findViewById(R.id.spinner_vacine);
        spinnerPWD = findViewById(R.id.spinnerPWD);
        House_type = findViewById(R.id.House_type);
        Solo_Parent = findViewById(R.id.Solo_Parent);
        voc_school_name = findViewById(R.id.voc_school_name);
        voc_school_address = findViewById(R.id.voc_school_address);
        college_school_name = findViewById(R.id.college_school_name);
        college_school_address = findViewById(R.id.college_school_address);
        high_school_address = findViewById(R.id.high_school_address);
        high_school_name = findViewById(R.id.high_school_name);
        elem_school_address = findViewById(R.id.elem_school_address);
        elem_school_name = findViewById(R.id.elem_school_name);
        address_company = findViewById(R.id.address_company);
        duration = findViewById(R.id.duration);
        company_name = findViewById(R.id.company_name);
        Nationality = findViewById(R.id.Nationality);
        Weight = findViewById(R.id.Weight);
        Height = findViewById(R.id.Height);
        place_birth = findViewById(R.id.place_birth);
        province = findViewById(R.id.province);
        City_Municipality = findViewById(R.id.City_Municipality);
        barangay = findViewById(R.id.barangay);
        St_Purok_Sitio_Subd = findViewById(R.id.St_Purok_Sitio_Subd);
        House_blk_lot = findViewById(R.id.House_blk_lot);
        etLastName = findViewById(R.id.etLastName);
        etMiddleName = findViewById(R.id.etMiddleName);
        etFirstName = findViewById(R.id.etFirstName);
        editTextBirthday = findViewById(R.id.editTextBirthday);


        setupTextView(findViewById(R.id.firstname), "First Name*", "This field is required");
        setupTextView(findViewById(R.id.lastname), "Last Name*", "This field is required");
        setupTextView(findViewById(R.id.House), "(House/Block/Lot)*", "This field is required");
        setupTextView(findViewById(R.id.St), "(St/Purok/Sitio/Subd)*", "This field is required");
        setupTextView(findViewById(R.id.bar), "Barangay*", "This field is required");
        setupTextView(findViewById(R.id.city), "City/Municipality*", "This field is required");
        setupTextView(findViewById(R.id.prov), "Province*", "This field is required");
        setupTextView(findViewById(R.id.bday), "Date of Birth*", "This field is required");
        setupTextView(findViewById(R.id.bdayplace), "Place of Birth*", "This field is required");
        setupTextView(findViewById(R.id.gender), "Gender*", "This field is required");
        setupTextView(findViewById(R.id.civil), "Civil Status*", "This field is required");
        setupTextView(findViewById(R.id.height), "Height*", "This field is required");
        setupTextView(findViewById(R.id.weight), "Weight*", "This field is required");
        setupTextView(findViewById(R.id.house_type), "House Type*", "This field is required");
        setupTextView(findViewById(R.id.nationality), "Nationality*", "This field is required");
        setupTextView(findViewById(R.id.solo_par), "Solo Parent*", "This field is required");
        setupTextView(findViewById(R.id.Vstatus), "Vacinne Status*", "This field is required");
        setupTextView(findViewById(R.id.PWd), "Person with Disability*", "This field is required");




        // Initialize Firebase components
        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        // Button click listeners
        btnNext1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the next layout, hide the current layout
                findViewById(R.id.scrollView1).setVisibility(View.GONE);
                findViewById(R.id.scrollView2).setVisibility(View.VISIBLE);
            }
        });

        btnBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the previous layout, hide the current layout
                findViewById(R.id.scrollView1).setVisibility(View.VISIBLE);
                findViewById(R.id.scrollView2).setVisibility(View.GONE);
            }
        });

        btnNext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the next layout, hide the current layout
                findViewById(R.id.scrollView2).setVisibility(View.GONE);
                findViewById(R.id.scrollView3).setVisibility(View.VISIBLE);
            }
        });

        btnBack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the previous layout, hide the current layout
                findViewById(R.id.scrollView2).setVisibility(View.VISIBLE);
                findViewById(R.id.scrollView3).setVisibility(View.GONE);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gather all data from fields and perform necessary actions
                submitData();
            }
        });

        // Set click listener for birthday EditText
        editTextBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    // Method to show DatePickerDialog
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(barangay_cencus.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Set the selected date to the EditText
                editTextBirthday.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            }
        }, year, month, day);

        // Show DatePickerDialog
        datePickerDialog.show();
    }
    private void submitData() {
        String firstName = etFirstName.getText().toString();
        String middleName = etMiddleName.getText().toString();
        String lastName = etLastName.getText().toString();
        String houseBlockLot = House_blk_lot.getText().toString();
        String stPurokSitioSubd = St_Purok_Sitio_Subd.getText().toString();
        String barangayValue = barangay.getText().toString();
        String cityMunicipality = City_Municipality.getText().toString();
        String provinceValue = province.getText().toString();
        String placeOfBirth = place_birth.getText().toString();
        String height = Height.getText().toString();
        String weight = Weight.getText().toString();
        String nationality = Nationality.getText().toString();
        String companyName = company_name.getText().toString();
        String durationOfEmployment = duration.getText().toString();
        String companyAddress = address_company.getText().toString();
        String elemSchoolName = elem_school_name.getText().toString();
        String elemSchoolAddress = elem_school_address.getText().toString();
        String highSchoolName = high_school_name.getText().toString();
        String highSchoolAddress = high_school_address.getText().toString();
        String vocSchoolName = voc_school_name.getText().toString();
        String vocSchoolAddress = voc_school_address.getText().toString();
        String collegeSchoolName = college_school_name.getText().toString();
        String collegeSchoolAddress = college_school_address.getText().toString();
        String birthday = editTextBirthday.getText().toString();

        // Get selected values from Spinners
        String gender = spinnerGender.getSelectedItem().toString();
        String civilStatus = spinnerCivilStatus.getSelectedItem().toString();
        String vaccineStatus = spinner_vacine.getSelectedItem().toString();
        String pwdType = spinnerPWD.getSelectedItem().toString();
        String houseType = House_type.getSelectedItem().toString();
        String soloParent = Solo_Parent.getSelectedItem().toString();

        // Validate required fields
        if (firstName.isEmpty() || lastName.isEmpty() || houseBlockLot.isEmpty() ||
                stPurokSitioSubd.isEmpty() || barangayValue.isEmpty() || cityMunicipality.isEmpty() ||
                provinceValue.isEmpty() || placeOfBirth.isEmpty() || height.isEmpty() || weight.isEmpty() ||
                nationality.isEmpty() || vaccineStatus.isEmpty() || pwdType.isEmpty() ) {
            // Display error message for missing required fields
            Toast.makeText(barangay_cencus.this, "All fields are required except for Educational Attainment and Employment Record", Toast.LENGTH_SHORT).show();
            return; // Exit method if required fields are missing
        }

        int age = calculateAge(birthday);
        // Get current user ID
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Create a reference to the census data under the current user's ID
            DatabaseReference userCensusRef = usersRef.child(userId).child("census");

            // Create a HashMap to store the census data
            HashMap<String, Object> censusData = new HashMap<>();
            censusData.put("firstName", firstName);
            censusData.put("middleName", middleName);
            censusData.put("lastName", lastName);
            censusData.put("houseBlockLot", houseBlockLot);
            censusData.put("stPurokSitioSubd", stPurokSitioSubd);
            censusData.put("barangayValue", barangayValue);
            censusData.put("cityMunicipality", cityMunicipality);
            censusData.put("provinceValue", provinceValue);
            censusData.put("placeOfBirth", placeOfBirth);
            censusData.put("birthday", birthday);
            censusData.put("age", age);
            censusData.put("height", height);
            censusData.put("weight", weight);
            censusData.put("nationality", nationality);
            censusData.put("companyName", companyName);
            censusData.put("durationOfEmployment", durationOfEmployment);
            censusData.put("companyAddress", companyAddress);
            censusData.put("elemSchoolName", elemSchoolName);
            censusData.put("elemSchoolAddress", elemSchoolAddress);
            censusData.put("highSchoolName", highSchoolName);
            censusData.put("highSchoolAddress", highSchoolAddress);
            censusData.put("vocSchoolName", vocSchoolName);
            censusData.put("vocSchoolAddress", vocSchoolAddress);
            censusData.put("collegeSchoolName", collegeSchoolName);
            censusData.put("collegeSchoolAddress", collegeSchoolAddress);
            censusData.put("gender", gender);
            censusData.put("civilStatus", civilStatus);
            censusData.put("vaccineStatus", vaccineStatus);
            censusData.put("pwdType", pwdType);
            censusData.put("houseType", houseType);
            censusData.put("soloParent", soloParent);
            censusData.put("soloParent", soloParent);

            // Save the census data to Firebase
            userCensusRef.setValue(censusData)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Census data saved successfully
                            Toast.makeText(barangay_cencus.this, "Census data submitted successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            // Failed to save census data
                            Toast.makeText(barangay_cencus.this, "Failed to submit census data", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // User is not logged in
            Toast.makeText(barangay_cencus.this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

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


    // Method to calculate age from birthday
    private int calculateAge(String birthday) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
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

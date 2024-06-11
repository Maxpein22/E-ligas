package com.example.app_e_ligas;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.namespace.R;
import com.example.namespace.databinding.ActivityBarangayCencusBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class barangay_cencus extends DrawerBasedActivity {
    private ActivityBarangayCencusBinding activityBarangayCencusBinding;
    public static EditText editTextBirthday;
    public static EditText etFirstName;
    public static EditText etMiddleName;
    public static EditText etLastName;
    public static EditText House_blk_lot;
    public static EditText St_Purok_Sitio_Subd;
    public static EditText barangay;
    public static EditText City_Municipality;
    public static EditText province;
    public static EditText place_birth;
    public static EditText Height;
    public static EditText Weight;
    public static EditText Nationality;
    public static EditText company_name;
    public static EditText duration;
    public static EditText address_company;
    public static EditText elem_school_name;
    public static EditText elem_school_address;
    public static EditText high_school_name;
    public static EditText high_school_address;
    public static EditText voc_school_name;
    public static EditText voc_school_address;
    public static EditText college_school_name;
    public static EditText college_school_address;
    public static EditText etAlias;
    public static Spinner spinnerGender;
    public static Spinner spinnerCivilStatus;
    public static Spinner spinner_vacine;
    public static Spinner spinnerPWD;
    public static Spinner House_type;
    public static Spinner Solo_Parent;
    public static Spinner voter_status_spinner;
    public static Spinner residential_status_spinner;
    public static Spinner occup;
    public static Spinner spinnerReligion;
    public static Spinner spinnerLearningSystem;
    public static Spinner spinner_fourps;
    public static Button btnNext1, btnNext2, btnSubmit, btnBack1, btnBack2, btnAddResponse, btnSaveforAnotherResponse;
    private DatabaseReference usersRef;
    private FirebaseAuth mAuth;
    private boolean isDataSubmitted = false;
    private Map<String, Object> userData;
    static String censusEditingKey = "";

    ListView listView;

    UsersRecViewAdapter usersRecViewAdapter;
    RecyclerView promoRecViewList;
    ArrayList<PromoModel> promos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBarangayCencusBinding = ActivityBarangayCencusBinding.inflate(getLayoutInflater());
        setContentView(activityBarangayCencusBinding.getRoot());
        allocateActivityTitle("Barangay Census");

        // Initialize Firebase components
        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        fetchUserData();

        // Initialize views
        initView();

        // Button click listeners
        initButtonClickListeners();

        promoRecViewList = findViewById(R.id.promoRecViewList);


        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String currentUserEmail = currentUser.getEmail();
            populateListView(currentUserEmail);
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }


        // Set click listener for birthday EditText
        editTextBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        String currentUserEmail = currentUser.getEmail();
        populateListView(currentUserEmail);
    }

    // Assuming this method is inside an Activity class (barangay_cencus)
    private void populateListView(String currentUserEmail) {
        ArrayList<UserCensus> userList = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

        usersRecViewAdapter = new UsersRecViewAdapter(barangay_cencus.this);
        promoRecViewList.setAdapter(usersRecViewAdapter);
        promoRecViewList.setLayoutManager(new LinearLayoutManager(this));

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    UserCensus user = userSnapshot.getValue(UserCensus.class);
                    if (user != null && currentUserEmail.equals(user.getEmail())) {
                        user.setUserID(userSnapshot.getKey());
                        userList.add(user);
                    }else if(user.getUserEmail() != null && currentUserEmail.equals(user.getUserEmail())){
                        userList.add(user);
                    }
                }
                usersRecViewAdapter.setUsers(userList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(barangay_cencus.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();
            }
        });
    }




    private void initView() {
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
        etAlias = findViewById(R.id.etAlias);
        voter_status_spinner = findViewById(R.id.voter_status_spinner);
        residential_status_spinner = findViewById(R.id.residential_status_spinner);
        occup = findViewById(R.id.occup);

        spinnerReligion = findViewById(R.id.spinnerReligion);
        spinnerLearningSystem = findViewById(R.id.spinnerLearningSystem);
        spinner_fourps = findViewById(R.id.spinner_fourps);


        btnAddResponse = findViewById(R.id.btnAddResponse);



        setupTextView(findViewById(R.id.fourps), "4p's Member*", "This field is required");
        setupTextView(findViewById(R.id.religion), "Religion*", "This field is required");
        setupTextView(findViewById(R.id.occupation), "Occupation*", "This field is required");
        setupTextView(findViewById(R.id.Residential_status), "Residential Status*", "This field is required");
        setupTextView(findViewById(R.id.voter_status), "Voter Status*", "This field is required");
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

    }

    private void initButtonClickListeners() {
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

        btnAddResponse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the first ScrollView and hide others
                findViewById(R.id.scrollView1).setVisibility(View.VISIBLE);
                findViewById(R.id.scrollView2).setVisibility(View.GONE);
                findViewById(R.id.scrollView3).setVisibility(View.GONE);
                findViewById(R.id.scrollView4).setVisibility(View.GONE);
                // Clear all fields
                clearAllFields();
                Map<String, Object> innerMap = new HashMap<>();
                innerMap.put("test", "test");

                Map<String, Object> defaultValues = new HashMap<>();
                defaultValues.put("test", innerMap);
                populateFields(defaultValues);
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
                // Convert the selected date to the desired format
                SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
                calendar.set(year, monthOfYear, dayOfMonth);
                String formattedDate = sdf.format(calendar.getTime());

                // Set the formatted date to the EditText
                editTextBirthday.setText(formattedDate);
            }
        }, year, month, day);

        // Show DatePickerDialog
        datePickerDialog.show();
    }

    private void fetchUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            String userEmail = currentUser.getEmail();
            usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Boolean dataSubmitted = snapshot.child("dataSubmitted").getValue(Boolean.class);
                        if (dataSubmitted != null && dataSubmitted) {
                            // If data is submitted, show the fourth ScrollView
                            findViewById(R.id.scrollView4).setVisibility(View.VISIBLE);
                            findViewById(R.id.scrollView1).setVisibility(View.GONE);
                        } else {
                            // If data is not submitted or null, show the first ScrollView
                            findViewById(R.id.scrollView1).setVisibility(View.VISIBLE);
                        }
                        // Store user data for later use
                        userData = (Map<String, Object>) snapshot.getValue();
                        populateFields(userData);
                    } else {
                        // User data doesn't exist
                        // Show the first ScrollView
                        findViewById(R.id.scrollView1).setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(barangay_cencus.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @SuppressLint("ResourceAsColor")
    public static void populateFields(Map<String, Object> userData) {
        etFirstName.setText(getStringValue(userData, "userFirstName"));
        etMiddleName.setText(getStringValue(userData, "userMiddleName"));
        etLastName.setText(getStringValue(userData, "userLastName"));
        editTextBirthday.setText(getStringValue(userData, "birthday"));
        etAlias.setText(getStringValue(userData, "alias"));
        House_blk_lot.setText(getStringValue(userData, "houseBlockLot"));
        St_Purok_Sitio_Subd.setText(getStringValue(userData, "stPurokSitioSubd"));
        //barangay.setText(getStringValue(userData, "barangayValue"));
        barangay.setText("Ligas 1");

        //City_Municipality.setText(getStringValue(userData, "cityMunicipality"));
        City_Municipality.setText("Bacoor city");

        province.setText(getStringValue(userData, "provinceValue"));
        province.setText("Cavite");

        place_birth.setText(getStringValue(userData, "birthPlace"));
        Height.setText(getStringValue(userData, "height"));
        Weight.setText(getStringValue(userData, "weight"));

        Nationality.setText(getStringValue(userData, "nationality"));
        Nationality.setText("Filipino");

        company_name.setText(getStringValue(userData, "companyName"));
        duration.setText(getStringValue(userData, "durationOfEmployment"));
        address_company.setText(getStringValue(userData, "companyAddress"));
        elem_school_name.setText(getStringValue(userData, "elemSchoolName"));
        elem_school_address.setText(getStringValue(userData, "elemSchoolAddress"));
        high_school_name.setText(getStringValue(userData, "highSchoolName"));
        high_school_address.setText(getStringValue(userData, "highSchoolAddress"));
        voc_school_name.setText(getStringValue(userData, "vocSchoolName"));
        voc_school_address.setText(getStringValue(userData, "vocSchoolAddress"));
        college_school_name.setText(getStringValue(userData, "collegeSchoolName"));
        college_school_address.setText(getStringValue(userData, "collegeSchoolAddress"));

        // Set spinner values
        setSpinnerValue(spinnerGender, getStringValue(userData, "gender"));
        setSpinnerValue(spinnerCivilStatus, getStringValue(userData, "civilStatus"));
        setSpinnerValue(spinner_vacine, getStringValue(userData, "vaccineStatus"));
        setSpinnerValue(spinnerPWD, getStringValue(userData, "pwdType"));
        setSpinnerValue(House_type, getStringValue(userData, "houseType"));
        setSpinnerValue(Solo_Parent, getStringValue(userData, "soloParent"));
        setSpinnerValue(voter_status_spinner, getStringValue(userData, "voters"));
        setSpinnerValue(residential_status_spinner, getStringValue(userData, "resident_status"));
        setSpinnerValue(occup, getStringValue(userData, "occupation"));
        setSpinnerValue(spinnerReligion, getStringValue(userData, "religion"));
        setSpinnerValue(spinnerLearningSystem, getStringValue(userData, "learning_system"));
        setSpinnerValue(spinner_fourps, getStringValue(userData, "fourPs"));


    }

    private static String getStringValue(Map<String, Object> data, String key) {
        Object value = data.get(key);
        return value != null ? value.toString() : "";
    }

    private static void setSpinnerValue(Spinner spinner, String value) {
        // Assuming the spinner adapter contains the value
        for (int i = 0; i < spinner.getAdapter().getCount(); i++) {
            if (spinner.getAdapter().getItem(i).toString().equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    // Method to calculate age from birthday
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

    private void submitData() {
        // Get all data from the fields
        String userFirstName = etFirstName.getText().toString();
        String userMiddleName = etMiddleName.getText().toString();
        String userLastName = etLastName.getText().toString();
        String houseBlockLot = House_blk_lot.getText().toString();
        String stPurokSitioSubd = St_Purok_Sitio_Subd.getText().toString();
        String barangayValue = barangay.getText().toString();
        String cityMunicipality = City_Municipality.getText().toString();
        String provinceValue = province.getText().toString();
        String birthPlace = place_birth.getText().toString();
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
        String alias = etAlias.getText().toString();

        // Get selected values from Spinners
        String gender = spinnerGender.getSelectedItem().toString();
        String civilStatus = spinnerCivilStatus.getSelectedItem().toString();
        String vaccineStatus = spinner_vacine.getSelectedItem().toString();
        String pwdType = spinnerPWD.getSelectedItem().toString();
        String houseType = House_type.getSelectedItem().toString();
        String soloParent = Solo_Parent.getSelectedItem().toString();
        String voters = voter_status_spinner.getSelectedItem().toString();
        String resident_status = residential_status_spinner.getSelectedItem().toString();
        String occupation = occup.getSelectedItem().toString();
        String religion = spinnerReligion.getSelectedItem().toString();
        String learning_system = spinnerLearningSystem.getSelectedItem().toString();
        String fourPs = spinner_fourps.getSelectedItem().toString();

        String type_employment = occupation.equalsIgnoreCase("Unemployed") ? "Unemployed" : "Employed";

        // Validate required fields
        if (userFirstName.isEmpty() || userLastName.isEmpty() || houseBlockLot.isEmpty() ||
                stPurokSitioSubd.isEmpty() || barangayValue.isEmpty() || cityMunicipality.isEmpty() ||
                provinceValue.isEmpty() || birthPlace.isEmpty() || height.isEmpty() || weight.isEmpty() ||
                nationality.isEmpty() || vaccineStatus.isEmpty() || pwdType.isEmpty() || religion.isEmpty()) {
            // Display error message for missing required fields
            Toast.makeText(barangay_cencus.this, "All fields are required except for Educational Attainment and Employment Record", Toast.LENGTH_SHORT).show();
            return;
        }

        String age = String.valueOf(calculateAge(birthday)); // Get age as a string

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            String userEmail = currentUser.getEmail();
            usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Boolean dataSubmitted = snapshot.child("dataSubmitted").getValue(Boolean.class);
                        if (dataSubmitted != null) {
                            if (dataSubmitted) {

                                Map<String, Object> censusData = new HashMap<>();

                                censusData.put("email", userEmail);
                                censusData.put("userFirstName", userFirstName);
                                censusData.put("userMiddleName", userMiddleName);
                                censusData.put("userLastName", userLastName);
                                censusData.put("houseBlockLot", houseBlockLot);
                                censusData.put("stPurokSitioSubd", stPurokSitioSubd);
                                censusData.put("barangayValue", barangayValue);
                                censusData.put("cityMunicipality", cityMunicipality);
                                censusData.put("provinceValue", provinceValue);
                                censusData.put("birthPlace", birthPlace);
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
                                censusData.put("birthday", birthday);
                                censusData.put("age", age);
                                censusData.put("alias", alias);
                                censusData.put("gender", gender);
                                censusData.put("civilStatus", civilStatus);
                                censusData.put("vaccineStatus", vaccineStatus);
                                censusData.put("pwdType", pwdType);
                                censusData.put("houseType", houseType);
                                censusData.put("soloParent", soloParent);
                                censusData.put("voters", voters);
                                censusData.put("resident_status", resident_status);
                                censusData.put("occupation", occupation);
                                censusData.put("religion", religion);
                                censusData.put("learning_system", learning_system);
                                censusData.put("type_employment", type_employment);
                                censusData.put("fourPs", fourPs);
                                censusData.put("dataSubmitted", true);
                                censusData.put("address", houseBlockLot + " " + stPurokSitioSubd + " " + barangayValue + " " + cityMunicipality + " " + provinceValue);



                                DatabaseReference newUserRef = usersRef.push(); // Generate a new unique ID\
                               if(!censusEditingKey.isEmpty()){
                                    newUserRef = usersRef.child(censusEditingKey);
                                }
                                newUserRef.setValue(censusData) // Set the value of the new user's node to the census data
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                censusEditingKey = "";
                                                Toast.makeText(barangay_cencus.this, "Update successfully", Toast.LENGTH_SHORT).show();

                                                // Hide all ScrollView elements
                                                findViewById(R.id.scrollView1).setVisibility(View.GONE);
                                                findViewById(R.id.scrollView2).setVisibility(View.GONE);
                                                findViewById(R.id.scrollView3).setVisibility(View.GONE);

                                                // Show the fourth ScrollView
                                                findViewById(R.id.scrollView4).setVisibility(View.VISIBLE);

                                                // Set dataSubmitted to true for the current user
                                                usersRef.child(userId).child("dataSubmitted").setValue(true);
                                                clearAllFields();

                                            } else {
                                                // Failed to create new user
                                                Toast.makeText(barangay_cencus.this, "Failed to create new user", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                // Save the data to the current user
                                DatabaseReference currentUserRef = usersRef.child(userId);
                                currentUserRef.child("userLastName").setValue(userLastName);
                                currentUserRef.child("userMiddleName").setValue(userMiddleName);
                                currentUserRef.child("userFirstName").setValue(userFirstName);
                                // Save other fields similarly
                                currentUserRef.child("houseBlockLot").setValue(houseBlockLot);
                                currentUserRef.child("stPurokSitioSubd").setValue(stPurokSitioSubd);
                                currentUserRef.child("barangayValue").setValue(barangayValue);
                                currentUserRef.child("cityMunicipality").setValue(cityMunicipality);
                                currentUserRef.child("provinceValue").setValue(provinceValue);
                                currentUserRef.child("birthPlace").setValue(birthPlace);
                                currentUserRef.child("height").setValue(height);
                                currentUserRef.child("weight").setValue(weight);
                                currentUserRef.child("nationality").setValue(nationality);
                                currentUserRef.child("companyName").setValue(companyName);
                                currentUserRef.child("durationOfEmployment").setValue(durationOfEmployment);
                                currentUserRef.child("companyAddress").setValue(companyAddress);
                                currentUserRef.child("elemSchoolName").setValue(elemSchoolName);
                                currentUserRef.child("elemSchoolAddress").setValue(elemSchoolAddress);
                                currentUserRef.child("highSchoolName").setValue(highSchoolName);
                                currentUserRef.child("highSchoolAddress").setValue(highSchoolAddress);
                                currentUserRef.child("vocSchoolName").setValue(vocSchoolName);
                                currentUserRef.child("vocSchoolAddress").setValue(vocSchoolAddress);
                                currentUserRef.child("collegeSchoolName").setValue(collegeSchoolName);
                                currentUserRef.child("collegeSchoolAddress").setValue(collegeSchoolAddress);
                                currentUserRef.child("birthday").setValue(birthday);
                                currentUserRef.child("alias").setValue(alias);
                                currentUserRef.child("fourPs").setValue(fourPs);
                                currentUserRef.child("age").setValue(age);
                                currentUserRef.child("gender").setValue(gender);
                                currentUserRef.child("civilStatus").setValue(civilStatus);
                                currentUserRef.child("religion").setValue(religion);
                                currentUserRef.child("houseType").setValue(houseType);
                                currentUserRef.child("type_employment").setValue(type_employment);
                                currentUserRef.child("vaccineStatus").setValue(vaccineStatus);
                                currentUserRef.child("pwdType").setValue(pwdType);
                                currentUserRef.child("soloParent").setValue(soloParent);
                                currentUserRef.child("resident_status").setValue(resident_status);
                                currentUserRef.child("voters").setValue(voters);
                                currentUserRef.child("occupation").setValue(occupation);
                                currentUserRef.child("learning_system").setValue(learning_system);


                                // Save other fields similarly
                                currentUserRef.child("dataSubmitted").setValue(true);
                                String address = houseBlockLot + " " + stPurokSitioSubd + " " + barangayValue + " " + cityMunicipality + " " + provinceValue;
                                currentUserRef.child("address").setValue(address);

                                // Display a success message
                                Toast.makeText(barangay_cencus.this, "Data submitted successfully", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(getIntent());
                            }
                        } else {
                            // Data has not been submitted yet, save it to the current user
                            DatabaseReference currentUserRef = usersRef.child(userId);
                            currentUserRef.child("userLastName").setValue(userLastName);
                            currentUserRef.child("userMiddleName").setValue(userMiddleName);
                            currentUserRef.child("userFirstName").setValue(userFirstName);
                            // Save other fields similarly
                            currentUserRef.child("houseBlockLot").setValue(houseBlockLot);
                            currentUserRef.child("stPurokSitioSubd").setValue(stPurokSitioSubd);
                            currentUserRef.child("barangayValue").setValue(barangayValue);
                            currentUserRef.child("cityMunicipality").setValue(cityMunicipality);
                            currentUserRef.child("provinceValue").setValue(provinceValue);
                            currentUserRef.child("birthPlace").setValue(birthPlace);
                            currentUserRef.child("height").setValue(height);
                            currentUserRef.child("weight").setValue(weight);
                            currentUserRef.child("nationality").setValue(nationality);
                            currentUserRef.child("companyName").setValue(companyName);
                            currentUserRef.child("durationOfEmployment").setValue(durationOfEmployment);
                            currentUserRef.child("companyAddress").setValue(companyAddress);
                            currentUserRef.child("elemSchoolName").setValue(elemSchoolName);
                            currentUserRef.child("elemSchoolAddress").setValue(elemSchoolAddress);
                            currentUserRef.child("highSchoolName").setValue(highSchoolName);
                            currentUserRef.child("highSchoolAddress").setValue(highSchoolAddress);
                            currentUserRef.child("vocSchoolName").setValue(vocSchoolName);
                            currentUserRef.child("vocSchoolAddress").setValue(vocSchoolAddress);
                            currentUserRef.child("collegeSchoolName").setValue(collegeSchoolName);
                            currentUserRef.child("collegeSchoolAddress").setValue(collegeSchoolAddress);
                            currentUserRef.child("birthday").setValue(birthday);
                            currentUserRef.child("alias").setValue(alias);
                            currentUserRef.child("fourPs").setValue(fourPs);
                            currentUserRef.child("houseType").setValue(houseType);
                            currentUserRef.child("age").setValue(age);
                            currentUserRef.child("gender").setValue(gender);
                            currentUserRef.child("civilStatus").setValue(civilStatus);
                            currentUserRef.child("type_employment").setValue(type_employment);
                            currentUserRef.child("vaccineStatus").setValue(vaccineStatus);
                            currentUserRef.child("pwdType").setValue(pwdType);
                            currentUserRef.child("soloParent").setValue(soloParent);
                            currentUserRef.child("resident_status").setValue(resident_status);
                            currentUserRef.child("voters").setValue(voters);
                            currentUserRef.child("religion").setValue(religion);
                            currentUserRef.child("occupation").setValue(occupation);
                            currentUserRef.child("learning_system").setValue(learning_system);


                            // Save other fields similarly
                            currentUserRef.child("dataSubmitted").setValue(true);
                            String address = houseBlockLot + " " + stPurokSitioSubd + " " + barangayValue + " " + cityMunicipality + " " + provinceValue;
                            currentUserRef.child("address").setValue(address);

                            // Display a success message
                            Toast.makeText(barangay_cencus.this, "Data submitted successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(barangay_cencus.this, "Failed to check user data", Toast.LENGTH_SHORT).show();
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

    private void clearAllFields() {
        etFirstName.setText("");
        etMiddleName.setText("");
        etLastName.setText("");
        editTextBirthday.setText("");
        etAlias.setText("");
        House_blk_lot.setText("");
        St_Purok_Sitio_Subd.setText("");
        barangay.setText("");
        City_Municipality.setText("");
        province.setText("");
        place_birth.setText("");
        Height.setText("");
        Weight.setText("");
        Nationality.setText("");
        company_name.setText("");
        duration.setText("");
        address_company.setText("");
        elem_school_name.setText("");
        elem_school_address.setText("");
        high_school_name.setText("");
        high_school_address.setText("");
        voc_school_name.setText("");
        voc_school_address.setText("");
        college_school_name.setText("");
        college_school_address.setText("");

        // Set spinners to default selection
        spinnerGender.setSelection(0);
        spinnerCivilStatus.setSelection(0);
        spinner_vacine.setSelection(0);
        spinnerPWD.setSelection(0);
        House_type.setSelection(0);
        Solo_Parent.setSelection(0);
        voter_status_spinner.setSelection(0);
        residential_status_spinner.setSelection(0);
        occup.setSelection(0);
        spinnerReligion.setSelection(0);
        spinnerLearningSystem.setSelection(0);
        spinner_fourps.setSelection(0);
    }
}

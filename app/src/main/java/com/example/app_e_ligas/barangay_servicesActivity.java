package com.example.app_e_ligas;



import static java.security.AccessController.getContext;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import com.example.namespace.R;
import com.example.namespace.databinding.ActivityBarangayServicesBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class barangay_servicesActivity extends DrawerBasedActivity implements View.OnClickListener {
    ActivityBarangayServicesBinding activityBarangayServicesBinding;

    private Button btnBrgyCertification;
    private Button btnBrgyIndegency;
    private Button btnBrgyID;
    private Button btnBusinessClearance;
    private Button btnCedula;
    // Create a RecyclerView and set its layout manager and adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBarangayServicesBinding = ActivityBarangayServicesBinding.inflate(getLayoutInflater());
        setContentView(activityBarangayServicesBinding.getRoot());
        allocateActivityTitle("Barangay Services");

        // Find buttons by ID
        btnBrgyCertification = findViewById(R.id.btn_brgy_certification);
        btnBrgyID = findViewById(R.id.btn_brgy_id);
        btnBusinessClearance = findViewById(R.id.btn_business_clearance);
        btnCedula = findViewById(R.id.btn_cedula);
        btnBrgyIndegency = findViewById(R.id.btn_brgy_indigency);

        // Set click listeners for buttons
        btnBrgyCertification.setOnClickListener(this);
        btnBrgyID.setOnClickListener(this);
        btnBusinessClearance.setOnClickListener(this);
        btnCedula.setOnClickListener(this);
        btnBrgyIndegency.setOnClickListener(this);


        // history
        getRequestHistory();

    }

    private void getRequestHistory() {
        Log.i("RequestFetch", "Fetching Request...");
        List<Request> requestList = new ArrayList<>();
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("servicesRequests/" + userID);

        databaseReference
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        requestList.clear();
                        // Iterate through dataSnapshot to get the data
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            // Corrected this line to use the 'snapshot' variable
                            Request request = snapshot.getValue(Request.class);
                            requestList.add(request);
                            Log.i("RequestFetch", request.getType());
                        }
                        RecyclerView recyclerView = findViewById(R.id.historyRecyclerView);
                        recyclerView.setLayoutManager(new LinearLayoutManager(barangay_servicesActivity.this));
                        RequestAdapter adapter = new RequestAdapter(requestList, barangay_servicesActivity.this); // Create an adapter for your data
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle errors
                    }
                });
    }


    private void showBottomSheetDialog(String type) {
        ProgressDialog progressDialog = new ProgressDialog(barangay_servicesActivity.this);
        progressDialog.setTitle("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("users").child(userID);
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                // Check if the dataSnapshot has a value
                if (dataSnapshot.exists()) {

                    User currentUser = dataSnapshot.getValue(User.class);

                    // Inflate the bottom sheet layout
                    View bottomSheetView = getLayoutInflater().inflate(R.layout.activity_bottom_sheet_layout, null);

                    // Create a BottomSheetDialog
                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(barangay_servicesActivity.this);
                    bottomSheetDialog.setContentView(bottomSheetView);

                    TextView requestTitle = bottomSheetView.findViewById(R.id.requestTitle);
                    requestTitle.setText("Request for " + type);

                    // Find and set up form elements
                    EditText editTextSurname = bottomSheetView.findViewById(R.id.editTextSurname);
                    editTextSurname.setText(currentUser.getUserLastName());

                    EditText editTextFirstName = bottomSheetView.findViewById(R.id.editTextFirstName);
                    editTextFirstName.setText(currentUser.getUserFirstName());

                    EditText editTextMiddleName = bottomSheetView.findViewById(R.id.editTextMiddleName);
                    editTextMiddleName.setText(currentUser.getUserMiddleName());

                    EditText editTextPhoneNumber = bottomSheetView.findViewById(R.id.editTextContact);
                    editTextPhoneNumber.setText(currentUser.getUserPhoneNumber());

                    EditText editTextEmail = bottomSheetView.findViewById(R.id.editTextEmail);
                    editTextEmail.setText(currentUser.getUserEmail());

                    // Set up the spinner with certificate types
                    EditText requestType = bottomSheetView.findViewById(R.id.requestType);
                    requestType.setText(type);

                    // Handle the "Send" button click inside the BottomSheetDialog
                    Button submitButton = bottomSheetView.findViewById(R.id.submitButton);

                    EditText editTextPurpose = bottomSheetView.findViewById(R.id.editTextPurpose);

                    // civil status
                    EditText editTextCivilStatus = bottomSheetView.findViewById(R.id.editTextCivilStatus);

                    // age
                    EditText editTextAge = bottomSheetView.findViewById(R.id.editTextAge);

                    // birthday
                    EditText editTextBirthday = bottomSheetView.findViewById(R.id.editTextBirthday);

                    // birthplace
                    EditText editTextBirthplace = bottomSheetView.findViewById(R.id.editTextBirthplace);

                    // address
                    EditText editTextAddress = bottomSheetView.findViewById(R.id.editTextAddress);

                    // emergency contact person
                    EditText editTextEmergencyContact = bottomSheetView.findViewById(R.id.editTextEmergencyContact);


                    submitButton.setOnClickListener(new View.OnClickListener() {



                        // Get the current date
                        Date currentDate = new Date();
                        // Create a SimpleDateFormat object to format the date
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        // Convert the date to a string
                        String dateString = dateFormat.format(currentDate);
                        @Override
                        public void onClick(View view) {
                            String purpose = String.valueOf(editTextPurpose.getText());
                            currentUser.setCivilStatus(String.valueOf(editTextCivilStatus.getText()));
                            currentUser.setAge(String.valueOf(editTextAge.getText()));
                            currentUser.setBirthday(String.valueOf(editTextBirthday.getText()));
                            currentUser.setBirthPlace(String.valueOf(editTextBirthplace.getText()));
                            currentUser.setAddress(String.valueOf(editTextAddress.getText()));
                            currentUser.setEmergencyContactPerson(String.valueOf(editTextEmergencyContact.getText()));
                            progressDialog.show();
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("servicesRequests").child(userID);
                            String newRequestID = databaseReference.push().getKey();
                            Request request = new Request(currentUser,purpose ,type, "on-going",dateString, "Request is under review");
                            FirebaseDatabase.getInstance().getReference("servicesRequests")  // Change to your desired database node
                                    .child(userID)
                                    .child(newRequestID)
                                    .setValue(request)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            progressDialog.dismiss();
                                            if (task.isSuccessful()) {
                                                Toast.makeText(barangay_servicesActivity.this, "Successfully Requested", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(barangay_servicesActivity.this, "Not able to make request, please try again later", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                            bottomSheetDialog.dismiss();
                        }
                    });

                    // Show the BottomSheetDialog
                    bottomSheetDialog.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_brgy_indigency:
                // Handle click for Barangay Indigency button
                showBottomSheetDialog("Barangay Indigency");
                break;
            case R.id.btn_brgy_certification:
                // Handle click for Barangay Certification button
                showBottomSheetDialog("Barangay Certification");
                break;

            case R.id.btn_brgy_id:
                // Handle click for Barangay ID button
                showBottomSheetDialog("Barangay ID");
                break;

            case R.id.btn_business_clearance:
                // Handle click for Business Clearance button
                showBottomSheetDialog("Business Clearance");
                break;

            case R.id.btn_cedula:
                // Handle click for Cedula button
                showBottomSheetDialog("Cedula");
                break;
        }
    }





    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
package com.example.app_e_ligas;



import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


import com.google.android.material.bottomsheet.BottomSheetDialog;

import com.example.namespace.R;
import com.example.namespace.databinding.ActivityBarangayServicesBinding;

public class barangay_servicesActivity extends DrawerBasedActivity {
    ActivityBarangayServicesBinding activityBarangayServicesBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBarangayServicesBinding = ActivityBarangayServicesBinding.inflate(getLayoutInflater());
        setContentView(activityBarangayServicesBinding.getRoot());
        allocateActivityTitle("Barangay Services");

        Button requestButton = findViewById(R.id.button6);
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialog();
            }
        });
    } private void showBottomSheetDialog() {
        // Inflate the bottom sheet layout
        View bottomSheetView = getLayoutInflater().inflate(R.layout.activity_bottom_sheet_layout, null);

        // Create a BottomSheetDialog
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(bottomSheetView);

        // Find and set up form elements
        EditText editTextSurname = bottomSheetView.findViewById(R.id.editTextSurname);
        // ... (other form elements)

        // Set up the spinner with certificate types
        Spinner spinnerCertificates = bottomSheetView.findViewById(R.id.spinnerCertificates);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.certificate_types,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCertificates.setAdapter(adapter);

        // Handle the "Send" button click inside the BottomSheetDialog
        Button submitButton = bottomSheetView.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve data from form elements
                String surname = editTextSurname.getText().toString();
                // ... (retrieve other form data)

                // Handle the data as needed (e.g., send to server, store in database)

                // Close the BottomSheetDialog after submission
                bottomSheetDialog.dismiss();
            }
        });

        // Show the BottomSheetDialog
        bottomSheetDialog.show();
    }
}
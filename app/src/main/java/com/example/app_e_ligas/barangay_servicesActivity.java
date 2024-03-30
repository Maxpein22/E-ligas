package com.example.app_e_ligas;



import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static java.security.AccessController.getContext;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_e_ligas.Services.SelectServicesRecViewAdapter;
import com.example.app_e_ligas.Services.ServiceModel;
import com.google.android.flexbox.FlexboxLayout;
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

    private CardView btnBrgyCertification;
    private CardView btnBrgyIndegency;
    private CardView btnBrgyID;
    private CardView btnBusinessClearance;
    private CardView btnCedula;
    public static View bottomSheetView;
    // Create a RecyclerView and set its layout manager and adapter

    private static SelectServicesRecViewAdapter servicesRecViewAdapter;

    static RecyclerView servicesRecViewList;
    public static ArrayList<ServiceModel> selectedServices = new ArrayList<>();


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
                            requestList.add(0,request);
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


    private void showBottomSheetDialog(String type, String imageFileName) {
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
                    bottomSheetView = getLayoutInflater().inflate(R.layout.activity_bottom_sheet_layout, null);

                    // Create a BottomSheetDialog
                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(barangay_servicesActivity.this);
                    bottomSheetDialog.setContentView(bottomSheetView);
                    servicesRecViewList = bottomSheetView.findViewById(R.id.servicesRecViewList);
                    final TextView infoUser = bottomSheetView.findViewById(R.id.infoUser);
                    final FlexboxLayout businessDetailsContainer = bottomSheetDialog.findViewById(R.id.businessDetailsContainer);
                    final FlexboxLayout purposeContainer = bottomSheetDialog.findViewById(R.id.purposeContainer);
                    final EditText kindOfBusiness = bottomSheetDialog.findViewById(R.id.kindOfBusiness);
                    final EditText addressOfBusiness = bottomSheetDialog.findViewById(R.id.addressOfBusiness);

                    if(type.equals("Business Clearance")){
                        businessDetailsContainer.setVisibility(View.VISIBLE);
                        purposeContainer.setVisibility(View.GONE);
                        selectedServices.clear();
                        selectedServices.add(new ServiceModel("Business Clearance", "", "", "0", false));
                    }else{
                        businessDetailsContainer.setVisibility(View.GONE);
                        purposeContainer.setVisibility(View.VISIBLE);
                        selectedServices.clear();
                    }

                    // IMAGE PREVIEW
                    // Assuming your images are in the "drawable" folder
                    String packageName = getPackageName();
                    Resources resources = getResources();
                    int drawableResourceId = resources.getIdentifier(imageFileName, "drawable", packageName);

                    if (drawableResourceId != 0) { // Check if the resource was found
                        final ImageView certificatePreview = bottomSheetView.findViewById(R.id.certificatePreview);
                        certificatePreview.setImageResource(drawableResourceId);
                    }

                    infoUser.setText("The request will be made by the current logged in account : " + currentUser.getFullName());
                    ArrayList<ServiceModel> services = new ArrayList<>();
                    services.add(new ServiceModel("Employment", "", "", "0", false));
                    services.add(new ServiceModel("Residential Proof", "", "", "0", false));
                    services.add(new ServiceModel("Business Transactions", "", "", "0", false));
                    services.add(new ServiceModel("School Requirements", "", "", "0", false));
                    services.add(new ServiceModel("Voter's Registration", "", "", "0", false));
                    services.add(new ServiceModel("Travel and Immigration", "", "", "0", false));
                    services.add(new ServiceModel("Health Services", "", "", "0", false));
                    services.add(new ServiceModel("Government Assistance Programs", "", "", "0", false));
                    services.add(new ServiceModel("Others", "", "", "0", false));
                    servicesRecViewAdapter = new SelectServicesRecViewAdapter(bottomSheetDialog.getContext());
                    servicesRecViewAdapter.setUsers(services);
                    // Assuming you want a 2-column grid
                    int numberOfColumns = 2;
                    servicesRecViewList.setLayoutManager(new GridLayoutManager(bottomSheetDialog.getContext(), numberOfColumns));

                    servicesRecViewList.setAdapter(servicesRecViewAdapter);


                    TextView requestTitle = bottomSheetView.findViewById(R.id.requestTitle);
                    requestTitle.setText("Request for " + type);

                    EditText requestType = bottomSheetView.findViewById(R.id.requestType);
                    requestType.setText(type);

                    // Handle the "Send" button click inside the BottomSheetDialog
                    Button submitButton = bottomSheetView.findViewById(R.id.submitButton);

                    submitButton.setOnClickListener(new View.OnClickListener() {
                        // Get the current date
                        Date currentDate = new Date();
                        // Create a SimpleDateFormat object to format the date
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        // Convert the date to a string
                        String dateString = dateFormat.format(currentDate);

                        // services list

                        @Override
                        public void onClick(View view) {

                            if(selectedServices.isEmpty()){
                                Toast.makeText(barangay_servicesActivity.this, "Select at least one Purpose", Toast.LENGTH_LONG).show();
                                return;
                            }

                            String purpose = "";
                            for (ServiceModel service : selectedServices) {
                                if ("Others".equals(service.getServiceName())) {
                                    TextView otherText = bottomSheetView.findViewById(R.id.others);
                                    String otherPurpose = otherText.getText().toString().trim();

                                    if (!otherPurpose.isEmpty()) {
                                        purpose += otherPurpose + ", ";
                                    }
                                    continue;
                                }

                                purpose += service.getServiceName() + ", ";
                            }
                            if(purpose.isEmpty()){
                                Toast.makeText(barangay_servicesActivity.this, "Select at least one Purpose", Toast.LENGTH_LONG).show();
                                return;
                            }

                            // Remove the trailing comma and space
                            if (!purpose.isEmpty()) {
                                purpose = purpose.substring(0, purpose.length() - 2);
                            }
                            progressDialog.show();
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("servicesRequests").child(userID);
                            String newRequestID = databaseReference.push().getKey();
                            Request request = new Request(currentUser,purpose ,type, "on-going",dateString, "Request is under review", kindOfBusiness.getText().toString(), addressOfBusiness.getText().toString());
                            FirebaseDatabase.getInstance().getReference("servicesRequests")  // Change to your desired database node
                                    .child(userID)
                                    .child(newRequestID)
                                    .setValue(request)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            progressDialog.dismiss();
                                            if (task.isSuccessful()) {
                                                selectedServices.clear();
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

    public static void clickAllServices(SelectServicesRecViewAdapter.ViewHolder skipHolder){
        try {
            SelectServicesRecViewAdapter adapter = (SelectServicesRecViewAdapter)servicesRecViewList.getAdapter();
            ArrayList<SelectServicesRecViewAdapter.ViewHolder> holder = adapter.getAllHolder();
            for (SelectServicesRecViewAdapter.ViewHolder currentHolder : holder) {
                if(!skipHolder.txtServiceName.equals(currentHolder.txtServiceName)){
                    currentHolder.txtServiceName.setTextColor(Color.parseColor("#212121"));
                    currentHolder.txtServicePrice.setTextColor(Color.parseColor("#212121"));
                    ColorStateList colorStateList = ColorStateList.valueOf(Color.parseColor("#ffffff"));
                    ViewCompat.setBackgroundTintList(currentHolder.parentBtn, colorStateList);
                }
            }
        }catch (NullPointerException e){
            Log.i(TAG, e.getMessage());
        }
    }

    public static void showOthers(boolean visible){
        TextView otherText = bottomSheetView.findViewById(R.id.others);
        if(visible)otherText.setVisibility(View.VISIBLE);
        else otherText.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_brgy_indigency:
                // Handle click for Barangay Indigency button
                showBottomSheetDialog("Barangay Indigency","barangay_indigency_page_0001");
                break;
            case R.id.btn_brgy_certification:
                // Handle click for Barangay Certification button
                showBottomSheetDialog("Barangay Certification", "barangay_certificate_page_0001");
                break;

            case R.id.btn_brgy_id:
                // Handle click for Barangay ID button
                showBottomSheetDialog("Barangay ID","barangay_id_page_0001");
                break;

            case R.id.btn_business_clearance:
                // Handle click for Business Clearance button
                showBottomSheetDialog("Business Clearance","barangay_cert_for_business_page_0001__1_");
                break;

            case R.id.btn_cedula:
                // Handle click for Cedula button
                showBottomSheetDialog("Cedula","barangay_certificate_page_0001");
                break;
        }
    }





    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
package com.example.app_e_ligas;



import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.example.app_e_ligas.SubmitReport.generateRandomString;
import static java.security.AccessController.getContext;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_e_ligas.Services.SelectServicesRecViewAdapter;
import com.example.app_e_ligas.Services.ServiceModel;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.gms.tasks.Continuation;
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
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class barangay_servicesActivity extends DrawerBasedActivity implements View.OnClickListener {
    ActivityBarangayServicesBinding activityBarangayServicesBinding;
    private FlexboxLayout uploadPhotoContainer;
    private Button upload1x1Photo;
    private ImageView upload1x1ImageView;
    private CardView btnBrgyCertification;
    private CardView btnBrgyIndegency;
    private CardView btnBrgyID;
    private CardView btnBusinessClearance;
    private CardView btnCedula;

    private TextView brgycertnotavailable;
    private TextView brgyindigencynotavailable;
    private TextView brgyidnotavailable;
    private TextView clearancenotavailable;
    private TextView cedullanotavailable;

    public static View bottomSheetView;
    // Create a RecyclerView and set its layout manager and adapter

    private static SelectServicesRecViewAdapter servicesRecViewAdapter;

    static RecyclerView servicesRecViewList;
    public static ArrayList<ServiceModel> selectedServices = new ArrayList<>();
    int totalRequests = 1;
    String fcmToken = "";
    String photo1x1URL = "https://imgv3.fotor.com/images/blog-richtext-image/ID-Photo-Requirements-for-Passport-and-Identity-Card.jpg";

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 123;
    ProgressDialog dialog;

    // Register the activity result launcher outside of the onClick method
    ActivityResultLauncher<String> getContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    // Handle the selected image URI
                    if (uri != null) {
                        // Do something with the selected image URI
                        // For example, display it in an ImageView
                         upload1x1ImageView.setImageURI(uri);
                        String randomString = generateRandomString(10);

                        StorageReference storageReference = FirebaseStorage.getInstance().getReference("reports/"+randomString);

                        UploadTask uploadTasId = storageReference.putFile(uri);

                        Task<Uri> urlTask = uploadTasId.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }

                                // Continue with the task to get the download URL
                                return storageReference.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Uri downloadUri = task.getResult();
                                    photo1x1URL = downloadUri.toString();
                                    dialog.dismiss();
                                }
                            }
                        });

                    }
                }
            }
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyFirebaseMessagingService msg = new MyFirebaseMessagingService();
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

        brgycertnotavailable = findViewById(R.id.brgycertnotavailable);
        brgyindigencynotavailable = findViewById(R.id.brgyindigencynotavailable);
        brgyidnotavailable = findViewById(R.id.brgyidnotavailable);
        clearancenotavailable = findViewById(R.id.clearancenotavailable);
        cedullanotavailable = findViewById(R.id.cedullanotavailable);


        // history
        getRequestHistory();

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

    }

    private void getRequestHistory() {
        Log.i("RequestFetch", "Fetching Request...");
        List<Request> requestList = new ArrayList<>();
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("servicesRequests");

        databaseReference
                .child(userID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        requestList.clear();
                        // Iterate through dataSnapshot to get the data
                        for (DataSnapshot requestSnaps : dataSnapshot.getChildren()) {
                            // Corrected this line to use the 'snapshot' variable
                                    Request request = requestSnaps.getValue(Request.class);
                                    requestList.add(0, request);
                                    Log.i("RequestFetch", request.getType());

                                    CardView toDisbaleCard = null;
                                    TextView toDisableTextView = null;
                                    switch (request.getType()){
                                        case "Barangay Certification":
                                            // Set background color with opacity (e.g., 50% transparent)
                                            toDisbaleCard = btnBrgyCertification;
                                            toDisableTextView = brgycertnotavailable;
                                            break;
                                        case "Barangay Indigency":
                                            toDisbaleCard = btnBrgyIndegency;
                                            toDisableTextView = brgyindigencynotavailable;

                                            break;
                                        case "Barangay ID":
                                            toDisableTextView = brgyidnotavailable;
                                            toDisbaleCard = btnBrgyID;
                                            break;
                                        case "Business Clearance":
                                            toDisableTextView = clearancenotavailable;
                                            toDisbaleCard = btnBusinessClearance;
                                            break;
                                        case "Cedula":
                                            toDisableTextView = cedullanotavailable;
                                            toDisbaleCard = btnCedula;
                                    }
                                    if(toDisbaleCard != null){
                                        if(!request.getStatus().equals("rejected")){
                                            toDisbaleCard.setCardBackgroundColor(getResources().getColor(R.color.md_blue_grey_100));
                                            toDisbaleCard.setEnabled(false);
                                            toDisableTextView.setVisibility(View.VISIBLE);
                                            if(request.getStatus().equals("on-going")){
                                                toDisableTextView.setText("Not Available \n You have pending request");
                                            }else{
                                                String createdAt = request.getCreatedAt();
                                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                                                // Parse string to Date
                                                Date date = null;
                                                try {
                                                    date = dateFormat.parse(createdAt);
                                                } catch (ParseException e) {
                                                    throw new RuntimeException(e);
                                                }

                                                // Create a Calendar instance and set it to the parsed date
                                                Calendar calendar = Calendar.getInstance();
                                                calendar.setTime(date);

                                                // Add 6 months
                                                calendar.add(Calendar.MONTH, 6);

                                                // Get the date after adding 6 months
                                                Date dateAfter6Months = calendar.getTime();

                                                // Define output date format

                                                SimpleDateFormat outputDateFormat = new SimpleDateFormat("MMMM dd, yyyy");

                                                // Format the date to "April 21, 2024"
                                                String formattedDate = "You already request \n Available Again in " + outputDateFormat.format(dateAfter6Months);
                                                toDisableTextView.setText( formattedDate);
                                            }

                                        }else{
                                            toDisbaleCard.setEnabled(true);
                                            toDisbaleCard.setCardBackgroundColor(getResources().getColor(R.color.white));
                                            toDisableTextView.setVisibility(View.GONE);
                                        }
                                    }

                                totalRequests++;
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

                    uploadPhotoContainer = bottomSheetDialog.findViewById(R.id.uploadPhotoContainer);
                    upload1x1Photo = bottomSheetDialog.findViewById(R.id.upload1x1Photo);
                    upload1x1ImageView = bottomSheetDialog.findViewById(R.id.upload1x1ImageView);
                    if(type.equals("Barangay ID")){
                        uploadPhotoContainer.setVisibility(View.VISIBLE);

                        upload1x1Photo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                // Inside your onClick method
                                ActivityCompat.requestPermissions(barangay_servicesActivity.this,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);

                                // Now, handle permission result in onRequestPermissionsResult method of your activity or fragment
                                dialog =  ProgressDialog.show(barangay_servicesActivity.this, "Uploading Image",
                                        "Please wait...", true);
                                // Once permission is granted, launch the file picker intent
                                getContent.launch("image/*"); // Adjust MIME type if you want to select a specific type of file

                            }
                        });
                    }else{
                        uploadPhotoContainer.setVisibility(View.GONE);
                    }

                    boolean hidePurposeContainer = false;

                    if(type.equals("Business Clearance")){
                        hidePurposeContainer = true;
                        businessDetailsContainer.setVisibility(View.VISIBLE);
                        selectedServices.clear();
                        selectedServices.add(new ServiceModel("Business Clearance", "", "", "0", false));
                    }else{
                        businessDetailsContainer.setVisibility(View.GONE);
                        selectedServices.clear();
                    }

                    if(type.equals("Barangay ID")){
                        hidePurposeContainer = true;
                        selectedServices.clear();
                        selectedServices.add(new ServiceModel("Residency", "", "", "0", false));
                        // TODO: Show the incase of emergency container
                    }else{
                        if(!type.equals("Business Clearance"))selectedServices.clear();
                    }

                    if(hidePurposeContainer){
                        purposeContainer.setVisibility(View.GONE);
                    }else{
                        purposeContainer.setVisibility(View.VISIBLE);
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
                            String controlNo = "000" + Integer.toString(totalRequests);

                            Request request = new Request(currentUser,purpose ,type, "on-going",dateString, "Request is under review", kindOfBusiness.getText().toString(), addressOfBusiness.getText().toString(), controlNo, photo1x1URL, fcmToken);
                            FirebaseDatabase.getInstance().getReference("servicesRequests")  // Change to your desired database node
                                    .child(userID)
                                    .child(newRequestID)
                                    .setValue(request)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            progressDialog.dismiss();
                                            if (task.isSuccessful()) {

                                                // Get the current date
                                                Calendar calendar = Calendar.getInstance();
                                                Date currentDate = calendar.getTime();

                                                // Add 6 months
                                                calendar.add(Calendar.MONTH, 6);

                                                // Get the date after adding 6 months
                                                Date dateAfter6Months = calendar.getTime();
                                                // Convert the date to a string
                                                String dateAfter6MonthsString = dateFormat.format(dateAfter6Months);

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
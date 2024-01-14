package com.example.app_e_ligas;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.namespace.R;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Random;


public class SubmitReport extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 123;
    private FusedLocationProviderClient fusedLocationClient;
    String emergencyType = "others";
    String othersHelp = "";

    CheckBox ambulanceBox;
    CheckBox barangayTanodBox;
    CheckBox policeBox;
    CheckBox fireTruckBox;

    ImageView viewImageCaptured;
    FlexboxLayout helpSelection;
    Button submitReportBtn;
    ProgressDialog dialog;
    Uri uri;
    Authentication auth;
    StorageReference storageReference;
    String reportLocation = "CX7J+4G5, Sampaguita, Bacoor, Cavite";

    ActivityResultLauncher<Uri> mGetContent = registerForActivityResult(
            new ActivityResultContracts.TakePicture(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    dialog.dismiss();
                    viewImageCaptured.setImageURI(uri);
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_report);
//        getSupportActionBar().setTitle("Submit Emergency Report");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Check for location permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Request the permission if not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {

            getCurrentLocation();
        }

        Bundle b = getIntent().getExtras();
        auth = new Authentication(this, null);
        if(b != null){
            emergencyType = b.getString("emergencyType");
        }
        viewImageCaptured = findViewById(R.id.viewImageId);
        submitReportBtn = findViewById(R.id.submitReportBtn);




        helpSelection = findViewById(R.id.helpSelection);

        ambulanceBox = findViewById(R.id.ambulanceBox);
        barangayTanodBox = findViewById(R.id.barangayTanodBox);
        policeBox = findViewById(R.id.policeBox);
        fireTruckBox = findViewById(R.id.fireTruckBox);

        ambulanceBox.setOnCheckedChangeListener(this);
        barangayTanodBox.setOnCheckedChangeListener(this);
        policeBox.setOnCheckedChangeListener(this);
        fireTruckBox.setOnCheckedChangeListener(this);

        if(emergencyType.equals("others")){
            helpSelection.setVisibility(View.VISIBLE);
        }
    }

    private void getCurrentLocation() {
        try {
            fusedLocationClient.getLastLocation()
                    .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                Location lastLocation = task.getResult();
                                double latitude = lastLocation.getLatitude();
                                double longitude = lastLocation.getLongitude();

                                String locationName = getLocationName(latitude, longitude);
                                // Permission already granted, start getting the location
                                if(ContextCompat.checkSelfPermission(SubmitReport.this,
                                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                                    ActivityCompat.requestPermissions(SubmitReport.this,
                                            new String[]{Manifest.permission.CAMERA}, 101 );
                                }
                                // Display the location in a TextView
                                File file = new File(getFilesDir(), "images");
                                uri = FileProvider.getUriForFile(SubmitReport.this, getApplicationContext().getPackageName() + ".provider", file);
                                dialog =  ProgressDialog.show(SubmitReport.this, "Emergency Report",
                                        "Please wait...", true);

                                new java.util.Timer().schedule(
                                        new java.util.TimerTask() {
                                            @Override
                                            public void run() {
                                                mGetContent.launch(uri);
                                            }
                                        },
                                        2000
                                );
                                submitReportBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        submitReport(locationName);
                                    }
                                });
                            } else {
                                // Handle the failure
                                Toast.makeText(SubmitReport.this, "Unable to get location", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private String getLocationName(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (((List<?>) addresses).size() > 0) {
                Address address = addresses.get(0);
                // Construct the location name from address components
                return address.getAddressLine(0) + " | Latitude: " + latitude + " | Longitude :" + longitude;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reportLocation + " | Latitude: " + latitude + " | Longitude :" + longitude;
    }

    private void submitReport(String locationName) {
        dialog.show();
        String randomString = generateRandomString(10);
        storageReference = FirebaseStorage.getInstance().getReference("reports/"+randomString);

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
                    saveReportToDatabase(downloadUri, locationName);
                }
            }
        });
    }

    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }

    private void saveReportToDatabase(Uri downloadUri, String locationName) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String key = mDatabase.child("posts").push().getKey();
        String userId =  auth.getUser().getUid();

        // Format the date using the specified formatter
        String formattedDate = generateFormattedDateTime();

        ReportModel report = new ReportModel(emergencyType,userId, downloadUri.toString(), getNeededHelp(emergencyType), "pending", formattedDate, locationName);
        mDatabase.child("reports").child(userId).setValue(report)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        finish();
                        Intent intent = new Intent(getApplicationContext(), PendingReport.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });;
    }

    public static String generateFormattedDateTime() {
        // Get the current date and time using ThreeTenABP
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime currentDateTime = null;
            currentDateTime = LocalDateTime.now();


            // Define the format for the date and time (including hour and minute)
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            // Format the date and time using the specified formatter
            String formattedDateTime = currentDateTime.format(formatter);

            return formattedDateTime;
        }
        return "2024-16-23 09:30";
    }

    private String getNeededHelp(String emergencyType) {
        switch (emergencyType){
            case "fire":
                return "Firetruck and Ambulance";
            case "earthquake":
                return "Ambulance";
            case "typhoon":
                return "Rescue Boat";
            case "robbery":
                return "Barangay Tanod";
            case "injuries":
                return "Ambulance";
            default:
                //others
                return othersHelp;
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        String toAddHelp = compoundButton.getText() + ", ";
        if(b){
            othersHelp += toAddHelp;
        }else{
            othersHelp = othersHelp.replace(toAddHelp, "");
        }
    }
}
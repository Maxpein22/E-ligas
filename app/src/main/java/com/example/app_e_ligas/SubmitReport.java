package com.example.app_e_ligas;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


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
    FlexboxLayout submitReportContainer;
    LinearLayout selectModeContainer;
    RelativeLayout photoBtn;
    RelativeLayout videoBtn;
    RelativeLayout voiceBtn;
    RelativeLayout messageBtn;
    Uri uri;
    Authentication auth;
    StorageReference storageReference;
    VideoView viewVideo;
    String reportLocation = "CX7J+4G5, Sampaguita, Bacoor, Cavite";
    EditText locationEmergencyEditText;
    String reportingType = "image";
    String fcmToken = "";
    RelativeLayout voicePlayContainer;
    Button play_button;
    SeekBar seek_bar;
    MediaPlayer mediaPlayer;
    FlexboxLayout messageEmergencyContainer;
    EditText messageEmergencyEditText;
    private Handler handler = new Handler();

    ActivityResultLauncher<Uri> photoGetContent = registerForActivityResult(
            new ActivityResultContracts.TakePicture(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    dialog.dismiss();
                    viewImageCaptured.setImageURI(uri);
                    viewImageCaptured.setVisibility(View.VISIBLE);
                    selectModeContainer.setVisibility(View.GONE);
                    submitReportContainer.setVisibility(View.VISIBLE);
                    reportingType = "image";
                }
            });

    ActivityResultLauncher voiceGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    // Handle the selected audio file URI
                    if (uri != null) {
                        dialog.dismiss();
                        selectModeContainer.setVisibility(View.GONE);
                        submitReportContainer.setVisibility(View.VISIBLE);
                        reportingType = "voice";
                    }
                }
            });

    private static final int REQUEST_RECORD_AUDIO = 1;
    ActivityResultLauncher<Uri> videoGetContent = registerForActivityResult(
            new ActivityResultContracts.CaptureVideo(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    dialog.dismiss();
                    viewVideo.setVideoURI(uri);
                    viewVideo.setVisibility(View.VISIBLE);
                    // Create a MediaController
                    MediaController mediaController = new MediaController(getBaseContext());
                    mediaController.setAnchorView(submitReportBtn); // Set the MediaController's anchor view to the VideoView

                    // Set MediaController to the VideoView
                    viewVideo.setMediaController(mediaController);
                    selectModeContainer.setVisibility(View.GONE);
                    submitReportContainer.setVisibility(View.VISIBLE);
                    //mediaController.show();
                    reportingType = "video";

                    new java.util.Timer().schedule(
                            new java.util.TimerTask() {
                                @Override
                                public void run() {
                                    viewVideo.start();
                                }
                            },
                            2000
                    );
                }
            });


    private ActivityResultLauncher<Intent> voiceRecorderLauncher;
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

        }

        Bundle b = getIntent().getExtras();
        auth = new Authentication(this, null);
        if(b != null){
            emergencyType = b.getString("emergencyType");
        }
        viewImageCaptured = findViewById(R.id.viewImageId);
        submitReportBtn = findViewById(R.id.submitReportBtn);

        submitReportContainer = findViewById(R.id.submitReportContainer);
        selectModeContainer = findViewById(R.id.selectModeContainer);
        photoBtn = findViewById(R.id.photoBtn);
        videoBtn = findViewById(R.id.videoBtn);
        voiceBtn = findViewById(R.id.voiceBtn);
        messageBtn = findViewById(R.id.messageBtn);
        viewVideo = findViewById(R.id.viewVideo);
        voicePlayContainer = findViewById(R.id.voicePlayContainer);
        play_button = findViewById(R.id.play_button);
        seek_bar = findViewById(R.id.seek_bar);

        messageEmergencyEditText = findViewById(R.id.messageEmergencyEditText);
        messageEmergencyContainer = findViewById(R.id.messageEmergencyContainer);
        locationEmergencyEditText = findViewById(R.id.locationEmergencyEditText);

        photoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation(photoGetContent, false, false);
            }
        });

        videoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation(videoGetContent, false, false);
            }
        });

        voiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation(voiceGetContent, true, false);
            }
        });

        messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation(voiceGetContent, false, true);
            }
        });


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

        // Register the launcher for voice recorder activity
        voiceRecorderLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        // Handle successful recording
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            // Get the URI of the recorded audio file
                            Uri recordedAudioUri = data.getData();
                            // Handle the URI as needed (e.g., save it, play it, etc.)
                            selectModeContainer.setVisibility(View.GONE);
                            submitReportContainer.setVisibility(View.VISIBLE);
                            voicePlayContainer.setVisibility(View.VISIBLE);
                            mediaPlayer =  MediaPlayer.create(this, recordedAudioUri); // Load audio file
                            play_button.setOnClickListener(view -> {
                                mediaPlayer.start(); // Start playback
                                updateSeekBar();
                            });

                            seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                @Override
                                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                    if (fromUser) {
                                        mediaPlayer.seekTo(progress);
                                    }
                                }

                                @Override
                                public void onStartTrackingTouch(SeekBar seekBar) {}

                                @Override
                                public void onStopTrackingTouch(SeekBar seekBar) {}
                            });

                            reportingType = "voice";
                            uri = recordedAudioUri;
                            Toast.makeText(this, "Recorded audio URI: " + recordedAudioUri, Toast.LENGTH_LONG).show();
                        } else {
                            // No data available
                            Toast.makeText(this, "No recorded audio available", Toast.LENGTH_SHORT).show();
                        }
                    } else if (result.getResultCode() == RESULT_CANCELED) {
                        // Handle cancellation
                        Toast.makeText(this, "Recording cancelled", Toast.LENGTH_SHORT).show();
                    } else {
                        // Handle other result codes
                        Toast.makeText(this, "Recording failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateSeekBar() {
        seek_bar.setMax(mediaPlayer.getDuration());

        Runnable updateSeekBarTask = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    seek_bar.setProgress(currentPosition);
                    seek_bar.postDelayed(this, 1000);
                }
            }
        };
        handler.postDelayed(updateSeekBarTask, 0);
    }

    private void getCurrentLocation(ActivityResultLauncher mGetContent, Boolean isVoice, Boolean isMessage) {
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
                                String locationNameWithoutLat = getLocationNameWithoutLat(latitude, longitude);
                                // Permission already granted, start getting the location
                                if(ContextCompat.checkSelfPermission(SubmitReport.this,
                                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                                    ActivityCompat.requestPermissions(SubmitReport.this,
                                            new String[]{Manifest.permission.CAMERA}, 101 );
                                }
                                dialog =  ProgressDialog.show(SubmitReport.this, "Emergency Report",
                                        "Please wait...", true);
                                locationEmergencyEditText.setText(locationNameWithoutLat);
                                if(isVoice){
                                    dialog.dismiss();
                                    // Launch the audio recorder
                                    Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                                    voiceRecorderLauncher.launch(intent);
                                }
                                else if (isMessage){
                                    dialog.dismiss();
                                    selectModeContainer.setVisibility(View.GONE);
                                    submitReportContainer.setVisibility(View.VISIBLE);
                                    messageEmergencyContainer.setVisibility(View.VISIBLE);
                                    reportingType = "message";
                                    submitReportBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            String messageText = String.valueOf(messageEmergencyEditText.getText());
                                            if(messageText.isEmpty()){
                                                Toast.makeText(SubmitReport.this, "Message is Required", Toast.LENGTH_LONG);
                                                return;
                                            }
                                            String toSubmitLocation = String.valueOf(locationEmergencyEditText.getText()) + " | Latitude: " + latitude + " | Longitude :" + longitude;
                                            saveReportToDatabase(messageText, toSubmitLocation);
                                        }
                                    });
                                    return;
                                }
                                else{
                                    // Display the location in a TextView
                                    File file = new File(getFilesDir(), "images");
                                    uri = FileProvider.getUriForFile(SubmitReport.this, getApplicationContext().getPackageName() + ".provider", file);
                                    new java.util.Timer().schedule(
                                            new java.util.TimerTask() {
                                                @Override
                                                public void run() {
                                                    mGetContent.launch(uri);
                                                }
                                            },
                                            2000
                                    );
                                }


                                submitReportBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String toSubmitLocation = String.valueOf(locationEmergencyEditText.getText()) + " | Latitude: " + latitude + " | Longitude :" + longitude;
                                        submitReport(toSubmitLocation);
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

    private String getLocationNameWithoutLat(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (((List<?>) addresses).size() > 0) {
                Address address = addresses.get(0);
                // Construct the location name from address components
                return address.getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reportLocation;
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
                    saveReportToDatabase(downloadUri.toString(), locationName);
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

    private void saveReportToDatabase(String downloadUri, String locationName) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String key = mDatabase.child("posts").push().getKey();
        String userId =  auth.getUser().getUid();

        // Format the date using the specified formatter
        String formattedDate = generateFormattedDateTime();

        ReportModel report = new ReportModel(emergencyType,userId, downloadUri, getNeededHelp(emergencyType), "pending", formattedDate, locationName, reportingType, fcmToken);
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
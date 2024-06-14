package com.example.app_e_ligas;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.namespace.R;
import com.example.namespace.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Profile extends DrawerBasedActivity {
    private static final String TAG = "ProfileActivity";
    ActivityProfileBinding activityProfileBinding;

    // Firebase variables
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;

    // Request code for selecting an image
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_COVER_IMAGE_REQUEST = 3;

    private static final int EDIT_PROFILE_REQUEST = 1001; // Request code for EditProfileActivity

    // Uri to store the image selected from gallery
    private Uri imageUri;
    private Uri coverImageUri;


    // Key for saving and restoring image URI
    private static final String IMAGE_URI_KEY = "imageUri";
    private static final String COVER_IMAGE_URI_KEY = "coverImageUri";


    // Add a request code for selecting an ID image
    private static final int PICK_VALID_ID_REQUEST = 2;


    private TextView verifyBtn, unverifiedTextView, editText;
    private ImageView iconImageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            coverImageUri = savedInstanceState.getParcelable(COVER_IMAGE_URI_KEY);
            if (coverImageUri != null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), coverImageUri);
                    activityProfileBinding.coverImageView.setImageBitmap(bitmap);
                    // Show the save button after a cover image is selected
                    activityProfileBinding.btnSaveforcoverphoto.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        super.onCreate(savedInstanceState);
        activityProfileBinding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(activityProfileBinding.getRoot());
        allocateActivityTitle("Profile");
        // Initialize Firebase variables and layout setup
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        mStorage = FirebaseStorage.getInstance().getReference();
        iconImageView = findViewById(R.id.iconImageView);
        unverifiedTextView = findViewById(R.id.unverifiedTextView);
        // Call checkUserValidationStatus() to update the visibility of the layout
        checkUserValidationStatus();

        verifyBtn = findViewById(R.id.verifyBtn);
        underlineText(verifyBtn);
        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    String userId = currentUser.getUid();
                    // Reference to the user's data in the database
                    DatabaseReference userRef = mDatabase.child(userId);

                    // Fetch user data only once
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // Check if dataSnapshot exists and contains user data
                            if (dataSnapshot.exists()) {
                                try {
                                    // Retrieve validationStatus and validIDUrl
                                    String validationStatus = dataSnapshot.child("validationStatus").getValue(String.class);
                                    String validIDUrl = dataSnapshot.child("validIDUrl").getValue(String.class);

                                    // Check if validIDUrl and validationStatus are properly retrieved
                                    if (validIDUrl == null || validIDUrl.isEmpty() || (validationStatus != null && validationStatus.equals("rejected"))) {
                                        Intent intent = new Intent(Profile.this, uploadValidId.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(Profile.this, "Please wait, your verification is in progress", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    Log.e(TAG, "Error retrieving user data: " + e.getMessage());
                                    Toast.makeText(Profile.this, "An error occurred", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Handle the case where the user data does not exist in the database
                                Toast.makeText(Profile.this, "User data not found", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.d(TAG, "Database error: " + databaseError.getMessage());
                        }
                    });
                }
            }
        });


        // Check if user is logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is logged in, retrieve user data
            String userId = currentUser.getUid();
            retrieveUserData(userId);
        } else {
            // User is not logged in, handle accordingly (e.g., redirect to login screen)
            Log.d(TAG, "User is not logged in");
            redirectToSignIn();
        }

        // Add an OnClickListener to the editButton
        activityProfileBinding.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the EditProfileActivity when the editButton is clicked
                Intent intent = new Intent(Profile.this, EditProfileActivity.class);
                startActivityForResult(intent, EDIT_PROFILE_REQUEST); // Start activity for result
            }
        });

        activityProfileBinding.editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the EditProfileActivity when the editButton is clicked
                Intent intent = new Intent(Profile.this, EditProfileActivity.class);
                startActivityForResult(intent, EDIT_PROFILE_REQUEST); // Start activity for result
            }
        });
        // Set click listener for profile icon
        activityProfileBinding.profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        // Set click listener for save button
        activityProfileBinding.btnSaveforprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage();
            }
        });

// Set click listener for changeCoverIcon
        activityProfileBinding.changeCoverIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCoverGallery();
            }
        });

// Set click listener for btnSaveforcoverphoto
        activityProfileBinding.btnSaveforcoverphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCoverPhoto();
            }
        });


        // Restore image URI if savedInstanceState is not null
        if (savedInstanceState != null) {
            imageUri = savedInstanceState.getParcelable(IMAGE_URI_KEY);
            if (imageUri != null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    activityProfileBinding.profileImageView.setImageBitmap(bitmap);
                    activityProfileBinding.btnSaveforprofile.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Method to redirect to sign-in activity
    private void redirectToSignIn() {
        Intent intent = new Intent(Profile.this, SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void retrieveUserData(String userId) {
        mDatabase.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        String coverPhotoUrl = dataSnapshot.child("coverPhotoUrl").getValue(String.class);
                        String firstName = dataSnapshot.child("userFirstName").getValue(String.class);
                        String middleName = dataSnapshot.child("userMiddleName").getValue(String.class);
                        String lastName = dataSnapshot.child("userLastName").getValue(String.class);
                        String fullName = firstName + " " + middleName + " " + lastName;
                        String civilstatus = dataSnapshot.child("civilStatus").getValue(String.class);
                        String gender = dataSnapshot.child("gender").getValue(String.class);

                        String birthday = dataSnapshot.child("birthday").getValue(String.class);
                        String age = dataSnapshot.child("age").getValue(String.class);
                        String birthpalce = dataSnapshot.child("birthPlace").getValue(String.class);
                        String phonenumber = dataSnapshot.child("userPhoneNumber").getValue(String.class);
                        String email = dataSnapshot.child("userEmail").getValue(String.class);


                        // Load profile image from userProfileImage URL
                        if (user.getUserProfileImage() != null) {
                            Glide.with(Profile.this)
                                    .load(user.getUserProfileImage())
                                    .transform(new CircleCrop()) // Apply circular transformation
                                    .into(activityProfileBinding.profileImageView);
                        }


                        // Load cover image using Glide if coverImageUrl is not null
                        if (coverPhotoUrl != null && !coverPhotoUrl.isEmpty()) {
                            Glide.with(Profile.this)
                                    .load(coverPhotoUrl)
                                    .into(activityProfileBinding.coverImageView);
                        }

                        activityProfileBinding.fullNameTextView.setText(fullName);
                        activityProfileBinding.CivilStatusTextView.setText(civilstatus);
                        activityProfileBinding.BirthdateTextView.setText(birthday);
                        String ageStr = calculateAgeBasedOnBday(birthday);
                        activityProfileBinding.ageTextView.setText(ageStr);
                        activityProfileBinding.BirthPlaceTextView.setText(birthpalce);
                        activityProfileBinding.phoneNumberTextView.setText(phonenumber);
                        activityProfileBinding.emailTextView.setText(email);
                        activityProfileBinding.GenderTextView.setText(gender);

                    }
                } else {
                    Log.d(TAG, "User data not found");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Database error: " + databaseError.getMessage());
            }
        });
    }

    public String calculateAgeBasedOnBday(String bday) {
        DateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        try {
            // Parse birthday string to Date object
            Date birthDate = dateFormat.parse(bday);

            // Get today's date
            Calendar today = Calendar.getInstance();
            // Get birthday as Calendar instance
            Calendar bdate = Calendar.getInstance();
            bdate.setTime(birthDate);

            // Calculate age
            int age = today.get(Calendar.YEAR) - bdate.get(Calendar.YEAR);
            // Decrease age by 1 if birthday hasn't occurred yet this year
            if (today.get(Calendar.DAY_OF_YEAR) < bdate.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }

            return String.valueOf(age);
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please use 'Month dd, yyyy'.");
        }
        return "21";
    }


    // Method to open the gallery for selecting a cover photo
    private void openCoverGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent, "Select Cover Photo"), PICK_COVER_IMAGE_REQUEST);
    }

    // Method to save cover photo
    private void saveCoverPhoto() {
        if (coverImageUri != null) {
            StorageReference coverImageRef = mStorage.child("cover_images/" + mAuth.getCurrentUser().getUid() + ".jpg");

            coverImageRef.putFile(coverImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Cover photo uploaded successfully, get the download URL
                            coverImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Cover photo URL retrieved successfully, save it to database
                                    String coverImageUrl = uri.toString();
                                    // Update user's cover photo URL in the database
                                    updateCoverPhotoUrl(coverImageUrl);
                                    // Hide the save button after successful upload
                                    activityProfileBinding.btnSaveforcoverphoto.setVisibility(View.GONE);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle failed upload
                            Toast.makeText(Profile.this, "Failed to upload cover photo", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // No cover photo selected, show a message to the user
            Toast.makeText(Profile.this, "Please select a cover photo", Toast.LENGTH_SHORT).show();
        }
    }


    private void openGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle profile image selection
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                activityProfileBinding.profileImageView.setImageBitmap(bitmap);
                // Show the save button after an image is selected
                activityProfileBinding.btnSaveforprofile.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Handle cover image selection
        else if (requestCode == PICK_COVER_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            coverImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), coverImageUri);
                activityProfileBinding.coverImageView.setImageBitmap(bitmap);
                // Show the save button after a cover image is selected
                activityProfileBinding.btnSaveforcoverphoto.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Handle result from EditProfileActivity
        else if (requestCode == EDIT_PROFILE_REQUEST && resultCode == RESULT_OK) {
            // Refresh the profile page after updating profile data in EditProfileActivity
            retrieveUserData(mAuth.getCurrentUser().getUid());
        }
    }





    private void saveImage() {
        if (imageUri != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                activityProfileBinding.profileImageView.setImageBitmap(bitmap);
                // Show the save button before uploading the image
                activityProfileBinding.btnSaveforprofile.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }

            StorageReference profileImageRef = mStorage.child("profile_images/" + mAuth.getCurrentUser().getUid() + ".jpg");

            profileImageRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Image uploaded successfully, get the download URL
                            profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Image URL retrieved successfully, save it to database or do whatever you want
                                    String imageUrl = uri.toString();
                                    // Update user's profile image URL in the database
                                    updateUserProfileImageUrl(imageUrl);
                                    // Hide the save button after successful upload
                                    activityProfileBinding.btnSaveforprofile.setVisibility(View.GONE);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle failed upload
                            Toast.makeText(Profile.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // No image selected, hide the save button
            activityProfileBinding.btnSaveforprofile.setVisibility(View.GONE);
            Toast.makeText(Profile.this, "Please select an image", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUserProfileImageUrl(String imageUrl) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            // Store the image URL under the user's node in the database
            mDatabase.child(userId).child("userProfileImage").setValue(imageUrl) // Update 'userProfileImage' node
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Profile.this, "Profile image updated successfully", Toast.LENGTH_SHORT).show();
                            // Refresh the profile page after updating profile image
                            retrieveUserData(userId);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Profile.this, "Failed to update profile image", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


    // Save and restore image URI when the activity state changes
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(IMAGE_URI_KEY, imageUri);
        outState.putParcelable(COVER_IMAGE_URI_KEY, coverImageUri); // Save cover image URI
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        imageUri = savedInstanceState.getParcelable(IMAGE_URI_KEY);
        coverImageUri = savedInstanceState.getParcelable(COVER_IMAGE_URI_KEY); // Restore cover image URI
        if (coverImageUri != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), coverImageUri);
                activityProfileBinding.coverImageView.setImageBitmap(bitmap);
                activityProfileBinding.btnSaveforcoverphoto.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkUserValidationStatus() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            // Reference to the user's data in the database
            DatabaseReference userRef = mDatabase.child(userId);

            // Add a listener to check the validation status
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Check if dataSnapshot exists and contains user data
                    if (dataSnapshot.exists()) {
                        String validationStatus = dataSnapshot.child("validationStatus").getValue(String.class);
                        String validIdUrl = dataSnapshot.child("validIdUrl").getValue(String.class);

                        if (validationStatus == null || validationStatus.equals("rejected")) {
                            // Show the unverified icon and text
                            iconImageView.setImageResource(R.drawable.checkes);
                            unverifiedTextView.setText("Unverified");

                            // Hide the verify button if validIdUrl exists
                            if (validIdUrl != null && !validIdUrl.isEmpty()) {
                                verifyBtn.setVisibility(View.GONE);
                            } else {
                                verifyBtn.setVisibility(View.VISIBLE);
                            }
                        } else if (validationStatus.equals("approved")) {
                            // Show the approved icon and text
                            iconImageView.setImageResource(R.drawable.check);
                            unverifiedTextView.setText("Verified");
                            verifyBtn.setVisibility(View.GONE);
                        }
                    } else {
                        Log.d(TAG, "User data not found");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(TAG, "Database error: " + databaseError.getMessage());
                }
            });
        }
    }


    // Method to open the gallery for selecting a valid ID image
    private void openGalleryForValidID() {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent, "Select Valid ID"), PICK_VALID_ID_REQUEST);
    }
    private void underlineText(TextView textView) {
        SpannableString content = new SpannableString(textView.getText());
        content.setSpan(new UnderlineSpan(), 0, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(content);
    }

    private void updateCoverPhotoUrl(String coverImageUrl) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            // Store the cover photo URL under the user's node in the database
            mDatabase.child(userId).child("coverPhotoUrl").setValue(coverImageUrl)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Profile.this, "Cover photo updated successfully", Toast.LENGTH_SHORT).show();
                            // Refresh the profile page after updating cover photo
                            retrieveUserData(userId);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Profile.this, "Failed to update cover photo", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


}
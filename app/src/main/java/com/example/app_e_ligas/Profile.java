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
import com.example.namespace.R;
import com.example.namespace.databinding.ActivityProfileBinding;
import com.example.uploadValidId;
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

public class Profile extends DrawerBasedActivity {
    private static final String TAG = "ProfileActivity";
    ActivityProfileBinding activityProfileBinding;

    // Firebase variables
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;

    // Request code for selecting an image
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int EDIT_PROFILE_REQUEST = 1001; // Request code for EditProfileActivity

    // Uri to store the image selected from gallery
    private Uri imageUri;

    // Key for saving and restoring image URI
    private static final String IMAGE_URI_KEY = "imageUri";

    // Add a request code for selecting an ID image
    private static final int PICK_VALID_ID_REQUEST = 2;


    private TextView verifyBtn, unverifiedTextView;
    private ImageView iconImageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
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


                                    Intent intent = new Intent(Profile.this, uploadValidId.class);
                                    startActivity(intent);
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

        // Set click listener for profile image view
        activityProfileBinding.profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        // Set click listener for save button
        activityProfileBinding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage();
            }
        });



        // Restore image URI if savedInstanceState is not null
        if (savedInstanceState != null) {
            imageUri = savedInstanceState.getParcelable(IMAGE_URI_KEY);
            if (imageUri != null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    activityProfileBinding.profileImageView.setImageBitmap(bitmap);
                    activityProfileBinding.btnSave.setVisibility(View.VISIBLE);
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

                        String alias = dataSnapshot.child("alias").getValue(String.class);
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
                                    .into(activityProfileBinding.profileImageView);
                        }
                        activityProfileBinding.fullNameTextView.setText(fullName);
                        activityProfileBinding.CivilStatusTextView.setText(civilstatus);
                        activityProfileBinding.BirthdateTextView.setText(birthday);
                        activityProfileBinding.ageTextView.setText(age);
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
                activityProfileBinding.btnSave.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == EDIT_PROFILE_REQUEST && resultCode == RESULT_OK) {
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
                activityProfileBinding.btnSave.setVisibility(View.VISIBLE);
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
                                    activityProfileBinding.btnSave.setVisibility(View.GONE);
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
            activityProfileBinding.btnSave.setVisibility(View.GONE);
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
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        imageUri = savedInstanceState.getParcelable(IMAGE_URI_KEY);
        if (imageUri != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                activityProfileBinding.profileImageView.setImageBitmap(bitmap);
                activityProfileBinding.btnSave.setVisibility(View.VISIBLE);
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
                            iconImageView.setImageResource(R.drawable.cross);
                            unverifiedTextView.setText("Unverified");

                            // Hide the verify button if validIdUrl exists
                            if (validIdUrl != null && !validIdUrl.isEmpty()) {
                                verifyBtn.setVisibility(View.GONE);
                            } else {
                                verifyBtn.setVisibility(View.VISIBLE);
                            }
                        } else if (validationStatus.equals("approved")) {
                            // Show the approved icon and text
                            iconImageView.setImageResource(R.drawable.checked);
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


}
package com.example.app_e_ligas;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityProfileBinding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(activityProfileBinding.getRoot());
        allocateActivityTitle("Profile");

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Initialize Firebase Realtime Database
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        // Initialize Firebase Storage
        mStorage = FirebaseStorage.getInstance().getReference();

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

        // Set click listener for logout button
        activityProfileBinding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sign out the user
                mAuth.signOut();
                Toast.makeText(Profile.this, "Logout Successfully", Toast.LENGTH_SHORT).show();
                // Redirect to the WelcomeActivity
                Intent intent = new Intent(Profile.this, WecolmeAcvtivity.class);
                // Clear the back stack and set WelcomeActivity as the new task
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                // Finish the Profile activity
                finish();
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
                        activityProfileBinding.fullNameTextView.setText(user.getFullName());
                        activityProfileBinding.phoneNumberTextView.setText(user.getUserPhoneNumber());
                        activityProfileBinding.emailTextView.setText(user.getUserEmail());
                        activityProfileBinding.addressTextView.setText(user.getAddress());
                        // Set new fields
                        activityProfileBinding.CivilStatusTextView.setText(user.getCivilStatus());
                        activityProfileBinding.ageTextView.setText(user.getAge());
                        activityProfileBinding.BirthdateTextView.setText(user.getBirthday());
                        activityProfileBinding.BirthPlaceTextView.setText(user.getBirthPlace());

                        // Load profile image
                        if (user.getValidIDUrl() != null) {
                            Glide.with(Profile.this)
                                    .load(user.getValidIDUrl())
                                    .into(activityProfileBinding.profileImageView);
                        }
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
            mDatabase.child(userId).child("validIDUrl").setValue(imageUrl) // Update 'validIDUrl' field instead of 'profileImageUrl'
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
}

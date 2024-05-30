package com.example.app_e_ligas;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.namespace.R;
import com.example.namespace.databinding.ActivityUploadValidIdBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class uploadValidId extends DrawerBasedActivity {
    ActivityUploadValidIdBinding activityUploadValidIdBinding;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private ImageView imageView;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUploadValidIdBinding = ActivityUploadValidIdBinding.inflate(getLayoutInflater());
        setContentView(activityUploadValidIdBinding.getRoot());
        allocateActivityTitle("Verification");

        // Initialize Firebase Auth and Database
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Button uploadButton = findViewById(R.id.uploadButton);
        Button submitButton = findViewById(R.id.submitButton);
        imageView = findViewById(R.id.imageView);

        uploadButton.setOnClickListener(v -> openGallery());

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if an image is selected
                if (imageUri != null) {
                    // Upload the image to Firebase Storage
                    uploadImageToStorage(imageUri);
                } else {
                    Toast.makeText(uploadValidId.this, "Please select an image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
            // You can add your image upload logic here
            Toast.makeText(this, "Image selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImageToStorage(Uri imageUri) {
        // Get reference to Firebase Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        // Create a reference to 'images/[currentUserId]/valid_id.jpg'
        String userId = mAuth.getCurrentUser().getUid();
        StorageReference imageRef = storageRef.child("images/" + userId + "/valid_id.jpg");

        // Upload file to Firebase Storage
        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Image uploaded successfully, now get the download URL
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Save the download URL under the current user's node with key "validIDUrl"
                        mDatabase.child("users").child(userId).child("validIDUrl").setValue(uri.toString())
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(uploadValidId.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                                    // Delete the validationStatus property
                                    mDatabase.child("users").child(userId).child("validationStatus").removeValue()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Navigate back to the profile page
                                                    Intent intent = new Intent(uploadValidId.this, Profile.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(uploadValidId.this, "Failed to delete validationStatus", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(uploadValidId.this, "Failed to save image URL to database", Toast.LENGTH_SHORT).show();
                                });
                    }).addOnFailureListener(exception -> {
                        // Handle any errors in getting the download URL
                        Toast.makeText(uploadValidId.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    });
                })
                .addOnFailureListener(exception -> {
                    // Handle unsuccessful uploads
                    Toast.makeText(uploadValidId.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                });
    }


    private void saveImageUrlToDatabase(String imageUrl) {
        // Get current user's ID
        String userId = mAuth.getCurrentUser().getUid();

        // Save the image URL under the current user's node with key "validIDUrl"
        mDatabase.child("users").child(userId).child("validIDUrl").setValue(imageUrl)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(uploadValidId.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failed database operation
                        Toast.makeText(uploadValidId.this, "Failed to upload image URL", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

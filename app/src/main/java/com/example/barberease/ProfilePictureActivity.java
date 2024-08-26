package com.example.barberease;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfilePictureActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Button uploadProfilePictureButton;
    private ImageView profilePicture;
    private Uri imageUri;

    private FirebaseAuth mAuth;
    private DatabaseReference barberReference;
    private StorageReference storageReference;
    private String barberId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_picture);

        // Initialize views
        uploadProfilePictureButton = findViewById(R.id.upload_profile_picture_button);
        profilePicture = findViewById(R.id.profile_picture);

        // Initialize Firebase
        initFirebase();

        // Load the current profile picture if available
        loadProfilePicture();

        // Set button click listener to open the file chooser
        uploadProfilePictureButton.setOnClickListener(v -> openFileChooser());
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("profile_pictures");
        barberReference = FirebaseDatabase.getInstance().getReference("barbers");

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            barberId = user.getUid();
        } else {
            showToast("User not authenticated.");
            finish(); // End the activity if the user is not authenticated
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            profilePicture.setImageURI(imageUri); // Display the selected image
            uploadProfilePicture(); // Upload the image to Firebase Storage
        }
    }

    private void uploadProfilePicture() {
        if (imageUri != null) {
            StorageReference fileReference = storageReference.child(barberId + ".jpg");

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                String imageUrl = uri.toString();
                                updateProfileImageUrl(imageUrl); // Save the image URL in the database
                            })
                            .addOnFailureListener(e -> showToast("Failed to get image URL: " + e.getMessage())))
                    .addOnFailureListener(e -> showToast("Failed to upload image: " + e.getMessage()));
        } else {
            showToast("No image selected");
        }
    }

    private void updateProfileImageUrl(String imageUrl) {
        barberReference.child(barberId).child("imageUrl").setValue(imageUrl)
                .addOnSuccessListener(aVoid -> {
                    showToast("Profile picture updated");
                    loadProfilePicture(); // Refresh the profile picture
                })
                .addOnFailureListener(e -> showToast("Failed to save image URL: " + e.getMessage()));
    }

    private void loadProfilePicture() {
        barberReference.child(barberId).child("imageUrl").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String imageUrl = task.getResult().getValue(String.class);
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    Glide.with(ProfilePictureActivity.this)
                            .load(imageUrl)
                            .apply(new RequestOptions().placeholder(R.drawable.ic_profile).error(R.drawable.ic_profile))
                            .into(profilePicture);
                } else {
                    profilePicture.setImageResource(R.drawable.ic_profile); // Default image
                }
            } else {
                showToast("Failed to load profile picture");
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(ProfilePictureActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}

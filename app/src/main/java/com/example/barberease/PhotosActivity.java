package com.example.barberease;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class PhotosActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Button uploadPhotoButton;
    private ImageView noPhotosIcon;
    private RecyclerView photosRecyclerView;
    private Uri imageUri;

    private FirebaseAuth mAuth;
    private DatabaseReference barberReference;
    private StorageReference storageReference;
    private String barberId;
    private List<String> photosList;
    private PhotosAdapter photosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        // Initialize UI components
        uploadPhotoButton = findViewById(R.id.upload_photo_button);
        noPhotosIcon = findViewById(R.id.no_photos_icon);
        photosRecyclerView = findViewById(R.id.photos_recycler_view);

        // Initialize Firebase references
        initFirebase();

        // Initialize RecyclerView
        photosList = new ArrayList<>();
        photosRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        photosAdapter = new PhotosAdapter(this, photosList);
        photosRecyclerView.setAdapter(photosAdapter);

        // Load existing photos from Firebase
        loadPhotos();

        // Set up button click listener for uploading photos
        uploadPhotoButton.setOnClickListener(v -> openFileChooser());
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("barber_photos");
        barberReference = FirebaseDatabase.getInstance().getReference("barbers");

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            barberId = currentUser.getUid();
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
            uploadPhoto();
        }
    }

    private void uploadPhoto() {
        if (imageUri != null) {
            StorageReference fileReference = storageReference.child(barberId).child(System.currentTimeMillis() + ".jpg");

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String photoUrl = uri.toString();
                        savePhotoUrlToDatabase(photoUrl);
                    }).addOnFailureListener(e -> showToast("Failed to get download URL: " + e.getMessage())))
                    .addOnFailureListener(e -> showToast("Failed to upload photo: " + e.getMessage()));
        } else {
            showToast("No image selected");
        }
    }

    private void savePhotoUrlToDatabase(String photoUrl) {
        String uploadId = barberReference.child(barberId).child("photos").push().getKey();
        if (uploadId != null) {
            barberReference.child(barberId).child("photos").child(uploadId).setValue(photoUrl)
                    .addOnSuccessListener(aVoid -> {
                        photosList.add(photoUrl);
                        photosAdapter.notifyDataSetChanged();
                        noPhotosIcon.setVisibility(View.GONE);
                        photosRecyclerView.setVisibility(View.VISIBLE);
                        showToast("Photo uploaded successfully");
                    })
                    .addOnFailureListener(e -> showToast("Failed to save photo to database: " + e.getMessage()));
        } else {
            showToast("Failed to generate unique key for photo");
        }
    }

    private void loadPhotos() {
        barberReference.child(barberId).child("photos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                photosList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    String photoUrl = postSnapshot.getValue(String.class);
                    photosList.add(photoUrl);
                }
                photosAdapter.notifyDataSetChanged();
                updateUI();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast("Failed to load photos: " + error.getMessage());
            }
        });
    }

    private void updateUI() {
        if (photosList.isEmpty()) {
            noPhotosIcon.setVisibility(View.VISIBLE);
            photosRecyclerView.setVisibility(View.GONE);
        } else {
            noPhotosIcon.setVisibility(View.GONE);
            photosRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void showToast(String message) {
        Toast.makeText(PhotosActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}

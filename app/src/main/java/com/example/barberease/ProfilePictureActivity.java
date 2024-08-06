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
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ProfilePictureActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Button uploadProfilePictureButton;
    private ImageView profilePicture;
    private Uri imageUri;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private String barberId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_picture);

        uploadProfilePictureButton = findViewById(R.id.upload_profile_picture_button);
        profilePicture = findViewById(R.id.profile_picture);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("barbers");
        storageReference = FirebaseStorage.getInstance().getReference("profile_pictures");
        barberId = mAuth.getCurrentUser().getUid();

        loadProfilePicture();

        uploadProfilePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
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
            uploadProfilePicture();
        }
    }

    private void uploadProfilePicture() {
        if (imageUri != null) {
            StorageReference fileReference = storageReference.child(barberId + ".jpg");
            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        databaseReference.child(barberId).child("imageUrl").setValue(uri.toString());
                        loadProfilePicture();
                        Toast.makeText(ProfilePictureActivity.this, "Profile picture updated", Toast.LENGTH_SHORT).show();
                    }))
                    .addOnFailureListener(e -> Toast.makeText(ProfilePictureActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    private void loadProfilePicture() {
        databaseReference.child(barberId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String imageUrl = snapshot.child("imageUrl").getValue(String.class);
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        Glide.with(ProfilePictureActivity.this).load(imageUrl).into(profilePicture);
                    } else {
                        profilePicture.setImageResource(R.drawable.ic_profile);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
            }
        });
    }
}

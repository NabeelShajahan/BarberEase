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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class PhotosActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Button uploadPhotoButton;
    private ImageView noPhotosIcon;
    private RecyclerView photosRecyclerView;
    private Uri imageUri;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private String barberId;
    private List<String> photosList;
    private PhotosAdapter photosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        uploadPhotoButton = findViewById(R.id.upload_photo_button);
        noPhotosIcon = findViewById(R.id.no_photos_icon);
        photosRecyclerView = findViewById(R.id.photos_recycler_view);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("barbers");
        storageReference = FirebaseStorage.getInstance().getReference("barber_posts");
        barberId = mAuth.getCurrentUser().getUid();
        photosList = new ArrayList<>();

        photosRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        photosAdapter = new PhotosAdapter(this, photosList);
        photosRecyclerView.setAdapter(photosAdapter);

        loadPhotos();

        uploadPhotoButton.setOnClickListener(new View.OnClickListener() {
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
            uploadPhoto();
        }
    }

    private void uploadPhoto() {
        if (imageUri != null) {
            StorageReference fileReference = storageReference.child(barberId).child(System.currentTimeMillis() + ".jpg");
            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String uploadId = databaseReference.push().getKey();
                        databaseReference.child(barberId).child("photos").child(uploadId).setValue(uri.toString());
                        photosList.add(uri.toString());
                        photosAdapter.notifyDataSetChanged();
                        noPhotosIcon.setVisibility(View.GONE);
                        photosRecyclerView.setVisibility(View.VISIBLE);
                        Toast.makeText(PhotosActivity.this, "Photo uploaded", Toast.LENGTH_SHORT).show();
                    }))
                    .addOnFailureListener(e -> Toast.makeText(PhotosActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    private void loadPhotos() {
        databaseReference.child(barberId).child("photos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                photosList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    String photoUrl = postSnapshot.getValue(String.class);
                    photosList.add(photoUrl);
                }
                photosAdapter.notifyDataSetChanged();
                if (photosList.isEmpty()) {
                    noPhotosIcon.setVisibility(View.VISIBLE);
                    photosRecyclerView.setVisibility(View.GONE);
                } else {
                    noPhotosIcon.setVisibility(View.GONE);
                    photosRecyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PhotosActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

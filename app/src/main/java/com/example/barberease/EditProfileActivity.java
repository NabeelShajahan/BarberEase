package com.example.barberease;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.IOException;

public class EditProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText editName, editPhone;
    private ImageView editImage;
    private Button saveButton, selectImageButton;
    private Uri imageUri;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        editName = findViewById(R.id.edit_name);
        editPhone = findViewById(R.id.edit_phone);
        editImage = findViewById(R.id.edit_image);
        saveButton = findViewById(R.id.save_button);
        selectImageButton = findViewById(R.id.select_image_button);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        storageReference = FirebaseStorage.getInstance().getReference("profile_images");

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            loadUserProfile(user.getUid());
        }

        selectImageButton.setOnClickListener(v -> openFileChooser());

        saveButton.setOnClickListener(v -> saveUserProfile());
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                editImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadUserProfile(String userId) {
        databaseReference.child(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    String name = snapshot.child("fullName").getValue(String.class);
                    String phone = snapshot.child("phone").getValue(String.class);
                    String imageUrl = snapshot.child("profileImage").getValue(String.class);

                    editName.setText(name);
                    editPhone.setText(phone);

                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        Glide.with(this).load(imageUrl).into(editImage);
                    }
                } else {
                    Toast.makeText(EditProfileActivity.this, "Profile not found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(EditProfileActivity.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserProfile() {
        String name = editName.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            editName.setError("Name is required");
            editName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(phone) || !phone.matches("\\d{10}")) {
            editPhone.setError("Valid 10-digit phone number is required");
            editPhone.requestFocus();
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            // Update name and phone in database
            databaseReference.child(userId).child("fullName").setValue(name);
            databaseReference.child(userId).child("phone").setValue(phone);

            if (imageUri != null) {
                StorageReference fileReference = storageReference.child(userId + ".jpg");
                fileReference.putFile(imageUri).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            String downloadUrl = uri.toString();
                            databaseReference.child(userId).child("profileImage").setValue(downloadUrl);
                            Toast.makeText(EditProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                            finish(); // Navigate back to ProfileActivity
                        });
                    } else {
                        Toast.makeText(EditProfileActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(EditProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                finish(); // Navigate back to ProfileActivity
            }
        }
    }
}

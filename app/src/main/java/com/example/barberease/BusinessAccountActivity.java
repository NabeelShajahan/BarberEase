package com.example.barberease;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class BusinessAccountActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView profileImage;
    private TextView profileName;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private String barberId;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_account);

        profileImage = findViewById(R.id.profile_image);
        profileName = findViewById(R.id.profile_name);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("barbers");
        storageReference = FirebaseStorage.getInstance().getReference("profile_pictures");
        barberId = mAuth.getCurrentUser().getUid();

        loadBarberProfile();

        profileImage.setOnClickListener(v -> openFileChooser());

        BusinessAccountPagerAdapter pagerAdapter = new BusinessAccountPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                viewPager.setCurrentItem(0);
                return true;
            } else if (itemId == R.id.nav_calendar) {
                Intent calendarIntent = new Intent(BusinessAccountActivity.this, AppointmentsActivity.class);
                startActivity(calendarIntent);
                return true;
            } else if (itemId == R.id.nav_list) {
                Intent listIntent = new Intent(BusinessAccountActivity.this, ProfileActivity.class);
                startActivity(listIntent);
                return true;
            }
            return false;
        });

        viewPager.setCurrentItem(0);
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
            profileImage.setImageURI(imageUri); // Display the selected image
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
        databaseReference.child(barberId).child("imageUrl").setValue(imageUrl)
                .addOnSuccessListener(aVoid -> {
                    showToast("Profile picture updated");
                    loadBarberProfile(); // Refresh the profile picture
                })
                .addOnFailureListener(e -> showToast("Failed to save image URL: " + e.getMessage()));
    }

    private void loadBarberProfile() {
        databaseReference.child(barberId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String imageUrl = snapshot.child("imageUrl").getValue(String.class);

                    profileName.setText(name != null ? name : "User Name");
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        Glide.with(BusinessAccountActivity.this).load(imageUrl).into(profileImage);
                    } else {
                        profileImage.setImageResource(R.drawable.ic_profile);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast("Failed to load profile data");
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(BusinessAccountActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}

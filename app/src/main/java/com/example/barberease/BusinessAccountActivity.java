package com.example.barberease;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class BusinessAccountActivity extends AppCompatActivity {

    private ImageView profileImage;
    private TextView profileName, tabInfo, tabReviews, tabServices, tabAddress, tabPhotos, tabProfilePicture;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private String barberId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_account);

        profileImage = findViewById(R.id.profile_image);
        profileName = findViewById(R.id.profile_name);
        tabInfo = findViewById(R.id.tab_info);
        tabReviews = findViewById(R.id.tab_reviews);
        tabServices = findViewById(R.id.tab_services);
        tabAddress = findViewById(R.id.tab_address);
        tabPhotos = findViewById(R.id.tab_photos);
        tabProfilePicture = findViewById(R.id.tab_profile_picture);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("barbers");
        storageReference = FirebaseStorage.getInstance().getReference("barber_posts");

        barberId = mAuth.getCurrentUser().getUid();

        loadBarberProfile();

        tabInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new InfoFragment());
                updateTabSelection(tabInfo);
            }
        });

        tabReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new ReviewsFragment());
                updateTabSelection(tabReviews);
            }
        });

        tabServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new ServicesFragment());
                updateTabSelection(tabServices);
            }
        });

        tabAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BusinessAccountActivity.this, AddressActivity.class));
            }
        });

        tabPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BusinessAccountActivity.this, PhotosActivity.class));
            }
        });

        tabProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BusinessAccountActivity.this, ProfilePictureActivity.class));
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    loadFragment(new HomeFragment());
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
            }
        });

        // Set initial fragment
        loadFragment(new HomeFragment());
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void updateTabSelection(TextView selectedTab) {
        tabInfo.setTextColor(getResources().getColor(selectedTab == tabInfo ? R.color.gold : R.color.gray));
        tabReviews.setTextColor(getResources().getColor(selectedTab == tabReviews ? R.color.gold : R.color.gray));
        tabServices.setTextColor(getResources().getColor(selectedTab == tabServices ? R.color.gold : R.color.gray));
        tabAddress.setTextColor(getResources().getColor(selectedTab == tabAddress ? R.color.gold : R.color.gray));
        tabPhotos.setTextColor(getResources().getColor(selectedTab == tabPhotos ? R.color.gold : R.color.gray));
        tabProfilePicture.setTextColor(getResources().getColor(selectedTab == tabProfilePicture ? R.color.gold : R.color.gray));
    }

    private void loadBarberProfile() {
        databaseReference.child(barberId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String imageUrl = snapshot.child("imageUrl").getValue(String.class);

                    profileName.setText(name);
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        Glide.with(BusinessAccountActivity.this).load(imageUrl).into(profileImage);
                    } else {
                        profileImage.setImageResource(R.drawable.ic_profile);
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

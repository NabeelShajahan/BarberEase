package com.example.barberease;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity {

    private TextView profileNameTextView, profileEmailTextView;
    private ImageView profileImageView;
    private Button logoutButton, businessAccountButton, editProfileButton;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        profileNameTextView = findViewById(R.id.profile_name);
        profileImageView = findViewById(R.id.profile_image);
        logoutButton = findViewById(R.id.logout_button);
        businessAccountButton = findViewById(R.id.business_account_button);
        editProfileButton = findViewById(R.id.edit_profile_button);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            loadUserProfile(user.getUid());
        }

        // Open EditProfileActivity when clicking the Edit Profile button
        editProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });

        businessAccountButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, BusinessAccountActivity.class);
            startActivity(intent);
        });

        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(ProfileActivity.this, loginActivity.class);
            startActivity(intent);
            finish();
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                Intent homeIntent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(homeIntent);
                return true;
            } else if (itemId == R.id.nav_calendar) {
                Intent calendarIntent = new Intent(ProfileActivity.this, AppointmentsActivity.class);
                startActivity(calendarIntent);
                return true;
            } else if (itemId == R.id.nav_profile) {
                return true;
            }
            return false;
        });
    }

    private void loadUserProfile(String userId) {
        databaseReference.child(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    String name = snapshot.child("fullName").getValue(String.class);
                    String imageUrl = snapshot.child("profileImage").getValue(String.class);
                    profileNameTextView.setText(name);

                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        Glide.with(this).load(imageUrl).into(profileImageView);
                    } else {
                        profileImageView.setImageResource(R.drawable.ic_profile); // Default profile image
                    }
                } else {
                    Toast.makeText(ProfileActivity.this, "Profile not found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ProfileActivity.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

package com.example.barberease;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity {

    private TextView profileNameTextView, profileEmailTextView;
    private ImageView profileImageView;
    private Button notificationsButton, helpSupportButton, logoutButton, paymentsButton, subscriptionsButton, businessAccountButton;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private BottomNavigationView bottomNavigationView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        profileNameTextView = findViewById(R.id.profile_name);
        profileEmailTextView = findViewById(R.id.profile_email);
        profileImageView = findViewById(R.id.profile_image);
        notificationsButton = findViewById(R.id.notifications_button);
        helpSupportButton = findViewById(R.id.help_support_button);
        logoutButton = findViewById(R.id.logout_button);
        paymentsButton = findViewById(R.id.paymentsandsubscription_button);
        businessAccountButton = findViewById(R.id.business_account_button);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            profileEmailTextView.setText(user.getEmail());
            // Load other profile data like name if available
            loadUserProfile(user.getUid());
        }

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        profileNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        profileEmailTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        notificationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle notifications logic
            }
        });

        helpSupportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle help and support logic
            }
        });

        paymentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, SubscriptionsActivity.class);
                startActivity(intent);
            }
        });


        businessAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, BusinessAccountActivity.class);
                startActivity(intent);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(ProfileActivity.this, loginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
                    // Stay in ProfileActivity
                    return true;
                }
                return false;
            }
        });
    }

    private void loadUserProfile(String userId) {
        databaseReference.child(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    String name = snapshot.child("fullName").getValue(String.class);
                    profileNameTextView.setText(name);
                } else {
                    Toast.makeText(ProfileActivity.this, "Profile not found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ProfileActivity.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

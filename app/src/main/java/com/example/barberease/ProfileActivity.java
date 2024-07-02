package com.example.barberease;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    private EditText profileNameEditText, profileEmailEditText, profilePhoneEditText;
    private Button changePictureButton, updateProfileButton, helpSupportButton, logoutButton;
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

        profileNameEditText = findViewById(R.id.profile_name);
        profileEmailEditText = findViewById(R.id.profile_email);
        profilePhoneEditText = findViewById(R.id.profile_phone);
        changePictureButton = findViewById(R.id.change_picture_button);
        updateProfileButton = findViewById(R.id.update_profile_button);
        helpSupportButton = findViewById(R.id.help_support_button);
        logoutButton = findViewById(R.id.logout_button);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            profileEmailEditText.setText(user.getEmail());
            // Load other profile data like name and phone if available
            loadUserProfile(user.getUid());
        }

        changePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle change picture logic
            }
        });

        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile(user.getUid());
            }
        });

        helpSupportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle help and support logic
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
                    String phone = snapshot.child("phone").getValue(String.class);
                    profileNameEditText.setText(name);
                    profilePhoneEditText.setText(phone);
                } else {
                    Toast.makeText(ProfileActivity.this, "Profile not found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ProfileActivity.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProfile(String userId) {
        String name = profileNameEditText.getText().toString().trim();
        String email = profileEmailEditText.getText().toString().trim();
        String phone = profilePhoneEditText.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.updateEmail(email).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    databaseReference.child(userId).child("fullName").setValue(name);
                    databaseReference.child(userId).child("phone").setValue(phone);
                    Toast.makeText(ProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProfileActivity.this, "Failed to update email", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}

package com.example.barberease;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfileActivity extends AppCompatActivity {

    private EditText editNameEditText, editEmailEditText, editPhoneEditText;
    private Button saveButton;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        editNameEditText = findViewById(R.id.edit_profile_name);
        editEmailEditText = findViewById(R.id.edit_profile_email);
        editPhoneEditText = findViewById(R.id.edit_profile_phone);
        saveButton = findViewById(R.id.save_button);

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            editEmailEditText.setText(user.getEmail());
            // Load other profile data like name and phone if available
            loadUserProfile(user.getUid());
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile(user.getUid());
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
                    editNameEditText.setText(name);
                    editPhoneEditText.setText(phone);
                } else {
                    Toast.makeText(EditProfileActivity.this, "Profile not found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(EditProfileActivity.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProfile(String userId) {
        String name = editNameEditText.getText().toString();
        String email = editEmailEditText.getText().toString();
        String phone = editPhoneEditText.getText().toString();

        databaseReference.child(userId).child("fullName").setValue(name);
        databaseReference.child(userId).child("email").setValue(email);
        databaseReference.child(userId).child("phone").setValue(phone);

        Toast.makeText(EditProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
    }
}

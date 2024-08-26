package com.example.barberease;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditInfoActivity extends AppCompatActivity {

    private EditText nameEditText, bioEditText, phoneEditText;
    private Button saveButton;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private String barberId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        nameEditText = findViewById(R.id.name_edit_text);
        bioEditText = findViewById(R.id.bio_edit_text);
        phoneEditText = findViewById(R.id.phone_edit_text);
        saveButton = findViewById(R.id.save_button);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("barbers");
        barberId = mAuth.getCurrentUser().getUid();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInfo();
            }
        });
    }

    private void saveInfo() {
        String name = nameEditText.getText().toString().trim();
        String bio = bioEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(bio) || TextUtils.isEmpty(phone)) {
            Toast.makeText(EditInfoActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseReference.child(barberId).child("name").setValue(name);
        databaseReference.child(barberId).child("bio").setValue(bio);
        databaseReference.child(barberId).child("phone").setValue(phone).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(EditInfoActivity.this, "Information Saved Successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(EditInfoActivity.this, "Failed to Save Information", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

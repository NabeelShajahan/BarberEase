package com.example.barberease;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class BusinessAccountActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText barberName, barberDetails, barberPrice, barberSchedule, barberPayments;
    private CheckBox serviceHaircut, serviceShave, serviceColoring, serviceStyling;
    private Button uploadPostsButton, registerButton;
    private Uri imageUri;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_account);

        barberName = findViewById(R.id.barber_name);
        barberDetails = findViewById(R.id.barber_details);
        barberPrice = findViewById(R.id.barber_price);
        barberSchedule = findViewById(R.id.barber_schedule);
        barberPayments = findViewById(R.id.barber_payments);
        serviceHaircut = findViewById(R.id.service_haircut);
        serviceShave = findViewById(R.id.service_shave);
        serviceColoring = findViewById(R.id.service_coloring);
        serviceStyling = findViewById(R.id.service_styling);
        uploadPostsButton = findViewById(R.id.upload_posts_button);
        registerButton = findViewById(R.id.register_button);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("barbers");
        storageReference = FirebaseStorage.getInstance().getReference("barber_posts");

        uploadPostsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerBarber();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            uploadImage();
        }
    }

    private void uploadImage() {
        if (imageUri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        // Save image URL to database or use as needed
                    }))
                    .addOnFailureListener(e -> Toast.makeText(BusinessAccountActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show());
        }
    }

    private String getFileExtension(Uri uri) {
        String extension;
        // Find the file extension from the URI
        try {
            extension = getContentResolver().getType(uri).split("/")[1];
        } catch (Exception e) {
            extension = "jpg";
        }
        return extension;
    }

    private void registerBarber() {
        String name = barberName.getText().toString().trim();
        String details = barberDetails.getText().toString().trim();
        String price = barberPrice.getText().toString().trim();
        String schedule = barberSchedule.getText().toString().trim();
        String payments = barberPayments.getText().toString().trim();
        boolean haircut = serviceHaircut.isChecked();
        boolean shave = serviceShave.isChecked();
        boolean coloring = serviceColoring.isChecked();
        boolean styling = serviceStyling.isChecked();

        if (name.isEmpty() || details.isEmpty() || price.isEmpty() || schedule.isEmpty() || payments.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            Barber barber = new Barber(userId, name, details, price, schedule, payments, haircut, shave, coloring, styling);
            databaseReference.child(userId).setValue(barber)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(BusinessAccountActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(BusinessAccountActivity.this, "Registration failed", Toast.LENGTH_SHORT).show());
        }
    }

    public static class Barber {
        public String userId;
        public String name;
        public String details;
        public String price;
        public String schedule;
        public String payments;
        public boolean haircut;
        public boolean shave;
        public boolean coloring;
        public boolean styling;

        public Barber(String userId, String name, String details, String price, String schedule, String payments, boolean haircut, boolean shave, boolean coloring, boolean styling) {
            this.userId = userId;
            this.name = name;
            this.details = details;
            this.price = price;
            this.schedule = schedule;
            this.payments = payments;
            this.haircut = haircut;
            this.shave = shave;
            this.coloring = coloring;
            this.styling = styling;
        }
    }
}

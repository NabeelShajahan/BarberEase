package com.example.barberease;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class BarberDetailsActivity extends AppCompatActivity {

    private static final String TAG = "BarberDetailsActivity";

    private TextView barberNameTextView, barberPhoneTextView, barberBioTextView, barberAddressTextView, barberHoursTextView;
    private ImageView barberImageView;
    private DatabaseReference databaseReference;
    private String barberId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barber_details);

        // Initialize UI components
        barberNameTextView = findViewById(R.id.barber_name);
        barberPhoneTextView = findViewById(R.id.barber_phone);
        barberBioTextView = findViewById(R.id.barber_bio);
        barberImageView = findViewById(R.id.barber_image);
        barberAddressTextView = findViewById(R.id.barber_address);
        barberHoursTextView = findViewById(R.id.barber_hours);

        // Get the barberId from the intent
        barberId = getIntent().getStringExtra("barberId");
        Log.d(TAG, "Received barberId: " + barberId);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("barbers");

        // Fetch and display barber details
        fetchBarberDetails();
    }

    private void fetchBarberDetails() {
        if (barberId == null || barberId.isEmpty()) {
            Log.e(TAG, "Barber ID is null or empty. Cannot fetch details.");
            Toast.makeText(this, "Unable to load barber details.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Fetch barber details from Firebase
        databaseReference.child(barberId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Barber barber = snapshot.getValue(Barber.class);
                if (barber != null) {
                    Log.d(TAG, "Barber details fetched successfully.");
                    displayBarberDetails(barber);
                } else {
                    Log.e(TAG, "Failed to parse barber details. Data is null.");
                    Toast.makeText(BarberDetailsActivity.this, "Failed to load barber details.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Database error: " + error.getMessage());
                Toast.makeText(BarberDetailsActivity.this, "Error loading barber details.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayBarberDetails(Barber barber) {
        // Display barber's name, phone number, and bio
        barberNameTextView.setText(barber.getName() != null ? barber.getName() : "Name not available");
        barberPhoneTextView.setText(barber.getPhone() != null ? barber.getPhone() : "Phone not available");
        barberBioTextView.setText(barber.getBio() != null ? barber.getBio() : "Bio not available");

        // Display barber's image
        if (barber.getImageUrl() != null && !barber.getImageUrl().isEmpty()) {
            Picasso.get().load(barber.getImageUrl()).into(barberImageView);
        } else {
            barberImageView.setImageResource(R.drawable.ic_profile); // Default profile image
        }

        // Display the barber's address
        Address address = barber.getAddress();
        if (address != null) {
            String fullAddress = String.format("%s, %s, %s, %s, %s, %s",
                    address.getStreetAddress() != null ? address.getStreetAddress() : "",
                    address.getBuildingFloor() != null ? address.getBuildingFloor() : "",
                    address.getCity() != null ? address.getCity() : "",
                    address.getStateRegion() != null ? address.getStateRegion() : "",
                    address.getZipCode() != null ? address.getZipCode() : "",
                    address.getCountry() != null ? address.getCountry() : "");
            barberAddressTextView.setText(fullAddress);
            Log.d(TAG, "Barber address: " + fullAddress);

            // Display working hours
            StringBuilder hoursStringBuilder = new StringBuilder();
            Map<String, Address.WorkingHours> hours = address.getHours();
            if (hours != null) {
                for (Map.Entry<String, Address.WorkingHours> entry : hours.entrySet()) {
                    Address.WorkingHours workingHours = entry.getValue();
                    if (workingHours != null) {
                        hoursStringBuilder.append(entry.getKey())
                                .append(": ")
                                .append(workingHours.isAvailable() ? workingHours.getHours() : "Closed")
                                .append("\n");
                    }
                }
                barberHoursTextView.setText(hoursStringBuilder.toString());
                Log.d(TAG, "Working hours: " + hoursStringBuilder.toString());
            } else {
                barberHoursTextView.setText("Working hours not available");
                Log.e(TAG, "No working hours data available.");
            }
        } else {
            barberAddressTextView.setText("Address not available");
            barberHoursTextView.setText("Working hours not available");
            Log.e(TAG, "Address data is null.");
        }
    }
}

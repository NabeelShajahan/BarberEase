package com.example.barberease;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class BarberDetailsActivity extends AppCompatActivity {

    private static final String TAG = "BarberDetailsActivity";

    private TextView barberNameTextView, barberPhoneTextView, barberAddressTextView;
    private ImageView barberImageView;
    private DatabaseReference databaseReference;
    private String barberId;
    private String barberImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barber_details);

        barberNameTextView = findViewById(R.id.barber_name);
        barberPhoneTextView = findViewById(R.id.barber_phone);
        barberAddressTextView = findViewById(R.id.barber_address);
        barberImageView = findViewById(R.id.barber_image);

        databaseReference = FirebaseDatabase.getInstance().getReference("barbers");
        barberId = getIntent().getStringExtra("barberId");
        barberImageUrl = getIntent().getStringExtra("barberImageUrl");

        if (barberImageUrl != null && !barberImageUrl.isEmpty()) {
            Picasso.get().load(barberImageUrl).into(barberImageView);
        } else {
            barberImageView.setImageResource(R.drawable.ic_profile);
        }

        fetchBarberDetails();
    }

    private void fetchBarberDetails() {
        databaseReference.child(barberId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Barber barber = snapshot.getValue(Barber.class);
                if (barber != null) {
                    displayBarberDetails(barber);
                } else {
                    Log.e(TAG, "Barber is null for snapshot: " + snapshot.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Database error: " + error.getMessage());
            }
        });
    }

    private void displayBarberDetails(Barber barber) {
        barberNameTextView.setText(barber.getName());
        barberPhoneTextView.setText(barber.getPhone());

        String address = String.format("%s, %s, %s, %s, %s, %s, %s",
                barber.getStreetAddress(),
                barber.getBuildingFloor(),
                barber.getCity(),
                barber.getStateRegion(),
                barber.getZipCode(),
                barber.getCountry(),
                barber.getLocationType());

        barberAddressTextView.setText(address);
    }
}

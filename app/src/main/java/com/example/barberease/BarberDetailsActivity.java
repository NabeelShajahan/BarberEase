package com.example.barberease;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BarberDetailsActivity extends AppCompatActivity {

    private ImageView barberImage;
    private TextView barberName, barberDescription, barberAvailability, barberServices, barberPayments;
    private DatabaseReference barberRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barber_details);

        barberImage = findViewById(R.id.barberImage);
        barberName = findViewById(R.id.barberName);
        barberDescription = findViewById(R.id.barberDescription);
        barberAvailability = findViewById(R.id.barberAvailability);
        barberServices = findViewById(R.id.barberServices);
        barberPayments = findViewById(R.id.barberPayments);

        String barberId = getIntent().getStringExtra("barberId");
        Log.d("BarberDetailsActivity", "Received barberId: " + barberId);
        if (barberId == null) {
            Log.e("BarberDetailsActivity", "barberId is null");
            // You can show an error message or finish the activity here
            finish();
            return;
        }

        barberRef = FirebaseDatabase.getInstance().getReference("barbers").child(barberId);

        barberRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Barber barber = dataSnapshot.getValue(Barber.class);
                if (barber != null) {
                    Glide.with(BarberDetailsActivity.this).load(barber.getImageUrl()).into(barberImage);
                    barberName.setText(barber.getName());
                    barberDescription.setText(barber.getDetails());
                    barberAvailability.setText(barber.getSchedule());
                    barberServices.setText(getServicesString(barber));
                    barberPayments.setText(barber.getPayments());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("BarberDetailsActivity", "DatabaseError: " + databaseError.getMessage());
            }
        });
    }

    private String getServicesString(Barber barber) {
        StringBuilder services = new StringBuilder();
        if (barber.isHaircut()) services.append("Haircut\n");
        if (barber.isShave()) services.append("Shaving\n");
        if (barber.isColoring()) services.append("Coloring\n");
        if (barber.isStyling()) services.append("Styling\n");
        return services.toString().trim();
    }
}

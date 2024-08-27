package com.example.barberease;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AppointmentsActivity extends AppCompatActivity {

    private RecyclerView pastAppointmentsRecyclerView, upcomingAppointmentsRecyclerView;
    private List<Map<String, String>> pastAppointmentsList, upcomingAppointmentsList;
    private DatabaseReference databaseReference;
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);

        pastAppointmentsRecyclerView = findViewById(R.id.past_appointments_recycler_view);
        upcomingAppointmentsRecyclerView = findViewById(R.id.upcoming_appointments_recycler_view);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        setupRecyclerViews();
        fetchAppointmentsFromDatabase();
        setupBottomNavigation();

    }

    private void setupRecyclerViews() {
        pastAppointmentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        upcomingAppointmentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void fetchAppointmentsFromDatabase() {
        String customerId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Assuming user is logged in

        // Fetch past appointments
        databaseReference.child("appointments").child("customers").child(customerId).child("past").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pastAppointmentsList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Map<String, String> appointment = (Map<String, String>) dataSnapshot.getValue();
                    if (appointment != null) {
                        pastAppointmentsList.add(appointment);
                    }
                }
                setupAdapter(pastAppointmentsRecyclerView, pastAppointmentsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AppointmentsActivity.this, "Failed to load past appointments.", Toast.LENGTH_SHORT).show();
            }
        });

        // Fetch upcoming appointments
        databaseReference.child("appointments").child("customers").child(customerId).child("upcoming").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                upcomingAppointmentsList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Map<String, String> appointment = (Map<String, String>) dataSnapshot.getValue();
                    if (appointment != null) {
                        upcomingAppointmentsList.add(appointment);
                    }
                }
                setupAdapter(upcomingAppointmentsRecyclerView, upcomingAppointmentsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AppointmentsActivity.this, "Failed to load upcoming appointments.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupAdapter(RecyclerView recyclerView, List<Map<String, String>> appointmentsList) {
        AppointmentsAdapter adapter = new AppointmentsAdapter(appointmentsList, this);
        recyclerView.setAdapter(adapter);

        // Handle item click to show details
        adapter.setOnItemClickListener(position -> {
            Map<String, String> appointment = appointmentsList.get(position);
            showAppointmentDetails(appointment);
        });
    }

    private void showAppointmentDetails(Map<String, String> appointment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Appointment Details")
                .setMessage("Barber Name: " + appointment.get("barberName") + "\n" +
                        "Date: " + appointment.get("date") + "\n" +
                        "Time: " + appointment.get("time") + "\n" +
                        "Address: " + appointment.get("address"))
                .setPositiveButton("OK", null)
                .show();
    }
    private void setupBottomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(AppointmentsActivity.this, MainActivity.class));
                return true;
            } else if (itemId == R.id.nav_appointments) {
                return true; // We're already in AppointmentsActivity
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(AppointmentsActivity.this, ProfileActivity.class));
                return true;
            }
            return false;
        });
    }
}
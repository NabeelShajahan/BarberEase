package com.example.barberease;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class AppointmentsActivity extends AppCompatActivity {

    private RecyclerView pastAppointmentsRecyclerView, upcomingAppointmentsRecyclerView;
    private List<String> pastAppointmentsList, upcomingAppointmentsList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);

        pastAppointmentsRecyclerView = findViewById(R.id.past_appointments_recycler_view);
        upcomingAppointmentsRecyclerView = findViewById(R.id.upcoming_appointments_recycler_view);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        setupRecyclerViews();
        fetchAppointmentsFromDatabase();
    }

    private void setupRecyclerViews() {
        pastAppointmentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        upcomingAppointmentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void fetchAppointmentsFromDatabase() {
        databaseReference.child("appointments").child("past").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pastAppointmentsList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String appointment = dataSnapshot.getValue(String.class);
                    pastAppointmentsList.add(appointment);
                }
                RecyclerView.Adapter adapter = new AppointmentsAdapter(pastAppointmentsList, AppointmentsActivity.this);
                pastAppointmentsRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AppointmentsActivity.this, "Failed to load past appointments.", Toast.LENGTH_SHORT).show();
            }
        });

        databaseReference.child("appointments").child("upcoming").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                upcomingAppointmentsList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String appointment = dataSnapshot.getValue(String.class);
                    upcomingAppointmentsList.add(appointment);
                }
                RecyclerView.Adapter adapter = new AppointmentsAdapter(upcomingAppointmentsList, AppointmentsActivity.this);
                upcomingAppointmentsRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AppointmentsActivity.this, "Failed to load upcoming appointments.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

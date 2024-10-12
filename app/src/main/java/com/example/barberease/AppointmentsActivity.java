package com.example.barberease;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    }

    private void setupRecyclerViews() {
        pastAppointmentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        upcomingAppointmentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void fetchAppointmentsFromDatabase() {
        String customerId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Assuming user is logged in
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        // Fetch appointments from Firebase
        databaseReference.child("appointments").child("customers").child(customerId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pastAppointmentsList = new ArrayList<>();
                upcomingAppointmentsList = new ArrayList<>();
                Date today = new Date(); // Get current date

                for (DataSnapshot dataSnapshot : snapshot.child("upcoming").getChildren()) {
                    Map<String, String> appointment = (Map<String, String>) dataSnapshot.getValue();
                    if (appointment != null) {
                        try {
                            Date appointmentDate = sdf.parse(appointment.get("date"));
                            String appointmentId = dataSnapshot.getKey(); // Get appointment ID
                            String barberId = appointment.get("barberId"); // Ensure barberId is available

                            // Add barberId and appointmentId to the appointment map
                            appointment.put("appointmentId", appointmentId);
                            appointment.put("barberId", barberId);

                            if (appointmentDate != null && appointmentDate.before(today)) {
                                pastAppointmentsList.add(appointment); // Add to past if before today
                            } else {
                                upcomingAppointmentsList.add(appointment); // Add to upcoming if today or future
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                setupAdapter(pastAppointmentsRecyclerView, pastAppointmentsList);
                setupAdapter(upcomingAppointmentsRecyclerView, upcomingAppointmentsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AppointmentsActivity.this, "Failed to load appointments.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAppointmentDetails(Map<String, String> appointment) {
        String barberId = appointment.get("barberId");
        String appointmentId = appointment.get("appointmentId");  // Ensure this is passed correctly

        if (barberId != null && appointmentId != null) {
            DatabaseReference appointmentRef = FirebaseDatabase.getInstance()
                    .getReference("appointments/barbers/" + barberId + "/upcoming/" + appointmentId + "/status");

            appointmentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String status = snapshot.getValue(String.class);

                    // Debug log to check the value of status
                    System.out.println("Appointment status from Firebase: " + status);

                    String statusMessage;
                    int statusColor;

                    if ("Confirmed".equals(status)) {
                        statusMessage = "Confirmed";
                        statusColor = Color.GREEN;
                    } else {
                        statusMessage = "Pending";
                        statusColor = Color.parseColor("#FFA500"); // Orange for pending
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(AppointmentsActivity.this);
                    builder.setTitle("Appointment Details")
                            .setMessage("Barber: " + appointment.get("barberName") + "\n" +
                                    "Date: " + appointment.get("date") + "\n" +
                                    "Time: " + appointment.get("time") + "\n" +
                                    "Status: " + statusMessage)
                            .setPositiveButton("OK", null)
                            .show();

                    // Get the alert dialog and update only the status text color
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    TextView messageTextView = dialog.findViewById(android.R.id.message);
                    if (messageTextView != null) {
                        String messageText = messageTextView.getText().toString();
                        int startIndex = messageText.indexOf("Status:");
                        if (startIndex != -1) {
                            int endIndex = messageText.length(); // Till the end of message
                            Spannable spannable = new SpannableString(messageText);
                            spannable.setSpan(new ForegroundColorSpan(statusColor),
                                    startIndex + 8, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            messageTextView.setText(spannable);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(AppointmentsActivity.this, "Failed to load appointment status.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Error: Missing appointment details.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupAdapter(RecyclerView recyclerView, List<Map<String, String>> appointmentsList) {
        AppointmentsAdapter adapter = new AppointmentsAdapter(appointmentsList, this);
        recyclerView.setAdapter(adapter);

        // Set click listener for showing details
        adapter.setOnItemClickListener(position -> {
            Map<String, String> appointment = appointmentsList.get(position);
            showAppointmentDetails(appointment);
        });
    }

}

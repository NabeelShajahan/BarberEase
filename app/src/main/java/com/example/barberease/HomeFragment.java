package com.example.barberease;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {

    private TextView addressAndHours, info, newAppointmentNotification, appointmentDetails;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private String barberId;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home_fragment, container, false);
        initializeUI(view);
        setupFirebase();
        loadNewAppointmentData();
        setOnClickListeners();
        return view;
    }

    private void initializeUI(View view) {
        addressAndHours = view.findViewById(R.id.address_and_hours);
        info = view.findViewById(R.id.info);
        newAppointmentNotification = view.findViewById(R.id.new_appointment_notification);
        appointmentDetails = view.findViewById(R.id.appointment_details);
        appointmentDetails.setVisibility(View.GONE); // Initially hidden
    }

    private void setupFirebase() {
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("barbers");
        barberId = mAuth.getCurrentUser().getUid();
    }

    private void loadNewAppointmentData() {
        databaseReference.child(barberId).child("appointments").child("upcoming").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot appointmentSnapshot : snapshot.getChildren()) {
                        String customerName = appointmentSnapshot.child("customerName").getValue(String.class);
                        String appointmentDate = appointmentSnapshot.child("date").getValue(String.class);
                        String appointmentTime = appointmentSnapshot.child("time").getValue(String.class);

                        String details = "Customer: " + customerName + "\nDate: " + appointmentDate + "\nTime: " + appointmentTime;
                        appointmentDetails.setText(details);

                        newAppointmentNotification.setVisibility(View.VISIBLE);
                        appointmentDetails.setVisibility(View.GONE);

                        newAppointmentNotification.setOnClickListener(v -> {
                            appointmentDetails.setVisibility(View.VISIBLE);
                            Toast.makeText(getContext(), "Appointment Details Shown", Toast.LENGTH_SHORT).show();
                        });
                    }
                } else {
                    newAppointmentNotification.setText("No new appointments");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load appointment data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setOnClickListeners() {
        addressAndHours.setOnClickListener(v -> startActivity(new Intent(getContext(), AddressActivity.class)));
        info.setOnClickListener(v -> startActivity(new Intent(getContext(), EditInfoActivity.class)));
    }
}

package com.example.barberease;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {

    private TextView todaySchedule, completedAppts, estimatedRevenue, hoursBooked, chairUtilization;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private String barberId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        todaySchedule = view.findViewById(R.id.today_schedule);
        completedAppts = view.findViewById(R.id.completed_appts);
        estimatedRevenue = view.findViewById(R.id.estimated_revenue);
        hoursBooked = view.findViewById(R.id.hours_booked);
        chairUtilization = view.findViewById(R.id.chair_utilization);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("barbers");

        barberId = mAuth.getCurrentUser().getUid();

        loadHomeData();

        return view;
    }

    private void loadHomeData() {
        databaseReference.child(barberId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Example data structure
                    String schedule = snapshot.child("schedule").getValue(String.class);
                    int completedAppointments = snapshot.child("completedAppointments").getValue(Integer.class);
                    double revenue = snapshot.child("estimatedRevenue").getValue(Double.class);
                    int bookedHours = snapshot.child("hoursBooked").getValue(Integer.class);
                    double utilization = snapshot.child("chairUtilization").getValue(Double.class);

                    todaySchedule.setText(schedule);
                    completedAppts.setText(String.valueOf(completedAppointments));
                    estimatedRevenue.setText(String.valueOf(revenue));
                    hoursBooked.setText(String.valueOf(bookedHours));
                    chairUtilization.setText(String.valueOf(utilization));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
            }
        });
    }
}

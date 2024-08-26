package com.example.barberease;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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

    private TextView todaySchedule, completedAppts, estimatedRevenue, hoursBooked, chairUtilization, addressAndHours, info, photos, profilePicture;
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
        loadHomeData();
        setOnClickListeners();
        return view;
    }

    private void initializeUI(View view) {
        todaySchedule = view.findViewById(R.id.today_schedule);
        completedAppts = view.findViewById(R.id.completed_appts);
        estimatedRevenue = view.findViewById(R.id.estimated_revenue);
        hoursBooked = view.findViewById(R.id.hours_booked);
        chairUtilization = view.findViewById(R.id.chair_utilization);
        addressAndHours = view.findViewById(R.id.address_and_hours);
        info = view.findViewById(R.id.info);
        photos = view.findViewById(R.id.photos);
        profilePicture = view.findViewById(R.id.profile_picture);
    }

    private void setupFirebase() {
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("barbers");
        barberId = mAuth.getCurrentUser().getUid();
    }

    private void loadHomeData() {
        databaseReference.child(barberId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    updateUI(snapshot);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
            }
        });
    }

    private void updateUI(DataSnapshot snapshot) {
        String schedule = snapshot.child("schedule").getValue(String.class);
        Integer completedAppointments = snapshot.child("completedAppointments").getValue(Integer.class);
        Double revenue = snapshot.child("estimatedRevenue").getValue(Double.class);
        Integer bookedHours = snapshot.child("hoursBooked").getValue(Integer.class);
        Double utilization = snapshot.child("chairUtilization").getValue(Double.class);

        todaySchedule.setText("Today's Schedule: " + (schedule != null ? schedule : "0"));
        completedAppts.setText("Completed Appointments: " + (completedAppointments != null ? completedAppointments : "0"));
        estimatedRevenue.setText("Estimated Revenue: " + (revenue != null ? revenue : "0"));
        hoursBooked.setText("Hours Booked: " + (bookedHours != null ? bookedHours : "0"));
        chairUtilization.setText("Chair Utilization: " + (utilization != null ? utilization : "0"));
    }

    private void setOnClickListeners() {
        addressAndHours.setOnClickListener(v -> startActivity(new Intent(getContext(), AddressActivity.class)));
        photos.setOnClickListener(v -> startActivity(new Intent(getContext(), PhotosActivity.class)));
        profilePicture.setOnClickListener(v -> startActivity(new Intent(getContext(), ProfilePictureActivity.class)));
        info.setOnClickListener(v -> startActivity(new Intent(getContext(), EditInfoActivity.class))); // Add this line

    }
}

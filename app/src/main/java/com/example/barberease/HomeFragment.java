package com.example.barberease;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {

    private TextView todaySchedule, completedAppts, estimatedRevenue, hoursBooked, chairUtilization, profileName;
    private ImageView profileImage;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private String barberId;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home_fragment, container, false);
        todaySchedule = view.findViewById(R.id.today_schedule);
        completedAppts = view.findViewById(R.id.completed_appts);
        estimatedRevenue = view.findViewById(R.id.estimated_revenue);
        hoursBooked = view.findViewById(R.id.hours_booked);
        chairUtilization = view.findViewById(R.id.chair_utilization);
        profileImage = view.findViewById(R.id.profile_image);
        profileName = view.findViewById(R.id.profile_name);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("barbers");

        barberId = mAuth.getCurrentUser().getUid();

        loadHomeData();

        return view;
    }

    private void loadHomeData() {
        databaseReference.child(barberId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String imageUrl = snapshot.child("imageUrl").getValue(String.class);
                    String schedule = snapshot.child("schedule").getValue(String.class);
                    Integer completedAppointments = snapshot.child("completedAppointments").getValue(Integer.class);
                    Double revenue = snapshot.child("estimatedRevenue").getValue(Double.class);
                    Integer bookedHours = snapshot.child("hoursBooked").getValue(Integer.class);
                    Double utilization = snapshot.child("chairUtilization").getValue(Double.class);

                    profileName.setText(name != null ? name : "User Name");
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        Glide.with(HomeFragment.this).load(imageUrl).into(profileImage);
                    } else {
                        profileImage.setImageResource(R.drawable.ic_profile);
                    }

                    todaySchedule.setText("Today's Schedule: " + (schedule != null ? schedule : "0"));
                    completedAppts.setText("Completed Appointments: " + (completedAppointments != null ? completedAppointments : "0"));
                    estimatedRevenue.setText("Estimated Revenue: " + (revenue != null ? revenue : "0"));
                    hoursBooked.setText("Hours Booked: " + (bookedHours != null ? bookedHours : "0"));
                    chairUtilization.setText("Chair Utilization: " + (utilization != null ? utilization : "0"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
            }
        });
    }
}

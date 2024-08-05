package com.example.barberease;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {

    private TextView todaySchedule, completedAppts, estimatedRevenue, hoursBooked, chairUtilization;
    private ImageView profileImage;
    private TextView addressAndHours, info, photos, profilePicture;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private String barberId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home_fragment, container, false);

        todaySchedule = view.findViewById(R.id.today_schedule);
        completedAppts = view.findViewById(R.id.completed_appts);
        estimatedRevenue = view.findViewById(R.id.estimated_revenue);
        hoursBooked = view.findViewById(R.id.hours_booked);
        chairUtilization = view.findViewById(R.id.chair_utilization);

        profileImage = view.findViewById(R.id.profile_image);
        addressAndHours = view.findViewById(R.id.address_and_hours);
        info = view.findViewById(R.id.info);
        photos = view.findViewById(R.id.photos);
        profilePicture = view.findViewById(R.id.profile_picture);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("barbers");

        barberId = mAuth.getCurrentUser().getUid();

        loadHomeData();
        setupProfileActions();

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

                    String imageUrl = snapshot.child("imageUrl").getValue(String.class);

                    todaySchedule.setText(schedule);
                    completedAppts.setText(String.valueOf(completedAppointments));
                    estimatedRevenue.setText(String.valueOf(revenue));
                    hoursBooked.setText(String.valueOf(bookedHours));
                    chairUtilization.setText(String.valueOf(utilization));

                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        Glide.with(getActivity()).load(imageUrl).into(profileImage);
                    } else {
                        profileImage.setImageResource(R.drawable.testusericon);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
            }
        });
    }

    private void setupProfileActions() {
        addressAndHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddressActivity.class));
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), InfoActivity.class));
            }
        });

        photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PhotosActivity.class));
            }
        });

        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ProfilePictureActivity.class));
            }
        });
    }
}

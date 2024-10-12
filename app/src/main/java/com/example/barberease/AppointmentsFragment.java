package com.example.barberease;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AppointmentsFragment extends Fragment {

    private static final String TAG = "AppointmentsFragment";
    private RecyclerView appointmentsRecyclerView;
    private BarberAppointmentsAdapter barberAppointmentsAdapter;
    private List<Appointment> appointmentList;
    private DatabaseReference appointmentsRef;
    private String barberId;
    private TextView noAppointmentsTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_reviews_fragment, container, false);
        appointmentsRecyclerView = view.findViewById(R.id.appointments_recycler_view);
        appointmentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        noAppointmentsTextView = view.findViewById(R.id.no_appointments_text_view);
        appointmentList = new ArrayList<>();
        barberAppointmentsAdapter = new BarberAppointmentsAdapter(appointmentList);
        appointmentsRecyclerView.setAdapter(barberAppointmentsAdapter);

        appointmentsRef = FirebaseDatabase.getInstance().getReference("appointments/barbers");
        barberId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        loadAppointmentsData();

        return view;
    }

    private void loadAppointmentsData() {
        Log.d(TAG, "Loading appointments for barber ID: " + barberId);
        appointmentsRef.child(barberId).child("upcoming").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                appointmentList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot appointmentSnapshot : snapshot.getChildren()) {
                        Appointment appointment = appointmentSnapshot.getValue(Appointment.class);
                        if (appointment != null) {
                            appointment.setId(appointmentSnapshot.getKey());
                            appointmentList.add(appointment);
                            Log.d(TAG, "Appointment ID: " + appointment.getId() + ", Date: " + appointment.getDate());
                        }
                    }
                    barberAppointmentsAdapter.notifyDataSetChanged();
                    updateNoAppointmentsMessage();
                } else {
                    Log.d(TAG, "No upcoming appointments found.");
                    updateNoAppointmentsMessage();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to load appointments: " + error.getMessage());
                Toast.makeText(getContext(), "Failed to load appointments.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateNoAppointmentsMessage() {
        if (appointmentList.isEmpty()) {
            noAppointmentsTextView.setVisibility(View.VISIBLE);
            appointmentsRecyclerView.setVisibility(View.GONE);
        } else {
            noAppointmentsTextView.setVisibility(View.GONE);
            appointmentsRecyclerView.setVisibility(View.VISIBLE);
        }
    }
}

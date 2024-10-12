package com.example.barberease;

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

public class HomeFragment extends Fragment {

    private TextView addressAndHours, info, viewAppointments;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home_fragment, container, false);
        initializeUI(view);
        setupFirebase();
        setOnClickListeners();
        return view;
    }

    private void initializeUI(View view) {
        addressAndHours = view.findViewById(R.id.address_and_hours);
        info = view.findViewById(R.id.info);
        viewAppointments = view.findViewById(R.id.view_appointments);
    }

    private void setupFirebase() {
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(getContext(), "User not authenticated.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setOnClickListeners() {
        addressAndHours.setOnClickListener(v -> startActivity(new Intent(getContext(), AddressActivity.class)));
        info.setOnClickListener(v -> startActivity(new Intent(getContext(), EditInfoActivity.class)));

        viewAppointments.setOnClickListener(v -> {
            // Navigate to the Appointments tab (index 1) using the ViewPager
            if (getActivity() instanceof BusinessAccountActivity) {
                ((BusinessAccountActivity) getActivity()).getViewPager().setCurrentItem(1);
            } else {
                Toast.makeText(getContext(), "Unable to navigate to Appointments", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

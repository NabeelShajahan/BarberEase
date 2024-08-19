package com.example.barberease;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ServicesFragment extends Fragment {

    private EditText serviceName, servicePrice, serviceDuration, serviceDescription;
    private Switch premiumHoursSwitch, offerPromotionSwitch;
    private Button saveButton;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private String barberId;

    public ServicesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_services_fragment, container, false);

        serviceName = view.findViewById(R.id.service_name);
        servicePrice = view.findViewById(R.id.service_price);
        serviceDuration = view.findViewById(R.id.service_duration);
        serviceDescription = view.findViewById(R.id.service_description);
        premiumHoursSwitch = view.findViewById(R.id.premium_hours_switch);
        offerPromotionSwitch = view.findViewById(R.id.offer_promotion_switch);
        saveButton = view.findViewById(R.id.save_button);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("services");
        barberId = mAuth.getCurrentUser().getUid();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveService();
            }
        });

        return view;
    }

    private void saveService() {
        String name = serviceName.getText().toString().trim();
        String price = servicePrice.getText().toString().trim();
        String duration = serviceDuration.getText().toString().trim();
        String description = serviceDescription.getText().toString().trim();
        boolean premiumHours = premiumHoursSwitch.isChecked();
        boolean offerPromotion = offerPromotionSwitch.isChecked();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(price) || TextUtils.isEmpty(duration) || TextUtils.isEmpty(description)) {
            Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String serviceId = databaseReference.push().getKey();
        Service service = new Service(serviceId, barberId, name, price, duration, description, premiumHours, offerPromotion);

        if (serviceId != null) {
            databaseReference.child(serviceId).setValue(service).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Service saved successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Failed to save service", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public static class Service {
        public String serviceId;
        public String barberId;
        public String name;
        public String price;
        public String duration;
        public String description;
        public boolean premiumHours;
        public boolean offerPromotion;

        public Service() {
            // Default constructor required for calls to DataSnapshot.getValue(Service.class)
        }

        public Service(String serviceId, String barberId, String name, String price, String duration, String description, boolean premiumHours, boolean offerPromotion) {
            this.serviceId = serviceId;
            this.barberId = barberId;
            this.name = name;
            this.price = price;
            this.duration = duration;
            this.description = description;
            this.premiumHours = premiumHours;
            this.offerPromotion = offerPromotion;
        }
    }
}

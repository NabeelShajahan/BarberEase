package com.example.barberease;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddressActivity extends AppCompatActivity {

    private EditText locationType, barbershopName, streetAddress, buildingFloor, city, stateRegion, zipCode, country;
    private Switch sundaySwitch, mondaySwitch, tuesdaySwitch, wednesdaySwitch, thursdaySwitch, fridaySwitch, saturdaySwitch;
    private EditText sundayHours, mondayHours, tuesdayHours, wednesdayHours, thursdayHours, fridayHours, saturdayHours;
    private Button saveButton;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private String barberId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        // Initialize all views
        initializeViews();

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("barbers");
        barberId = mAuth.getCurrentUser().getUid();

        saveButton.setOnClickListener(v -> saveAddress());
    }

    private void initializeViews() {
        locationType = findViewById(R.id.location_type);
        barbershopName = findViewById(R.id.barbershop_name);
        streetAddress = findViewById(R.id.street_address);
        buildingFloor = findViewById(R.id.building_floor);
        city = findViewById(R.id.city);
        stateRegion = findViewById(R.id.state_region);
        zipCode = findViewById(R.id.zip_code);
        country = findViewById(R.id.country);

        sundaySwitch = findViewById(R.id.switch_sunday);
        mondaySwitch = findViewById(R.id.switch_monday);
        tuesdaySwitch = findViewById(R.id.switch_tuesday);
        wednesdaySwitch = findViewById(R.id.switch_wednesday);
        thursdaySwitch = findViewById(R.id.switch_thursday);
        fridaySwitch = findViewById(R.id.switch_friday);
        saturdaySwitch = findViewById(R.id.switch_saturday);

        sundayHours = findViewById(R.id.hours_sunday);
        mondayHours = findViewById(R.id.hours_monday);
        tuesdayHours = findViewById(R.id.hours_tuesday);
        wednesdayHours = findViewById(R.id.hours_wednesday);
        thursdayHours = findViewById(R.id.hours_thursday);
        fridayHours = findViewById(R.id.hours_friday);
        saturdayHours = findViewById(R.id.hours_saturday);

        saveButton = findViewById(R.id.save_button);
    }

    private void saveAddress() {
        String locType = locationType.getText().toString().trim();
        String barberName = barbershopName.getText().toString().trim();
        String street = streetAddress.getText().toString().trim();
        String building = buildingFloor.getText().toString().trim();
        String cityName = city.getText().toString().trim();
        String state = stateRegion.getText().toString().trim();
        String zip = zipCode.getText().toString().trim();
        String countryName = country.getText().toString().trim();

        // Validate input fields
        if (locType.isEmpty() || barberName.isEmpty() || street.isEmpty() || cityName.isEmpty() || state.isEmpty() || zip.isEmpty() || countryName.isEmpty()) {
            Toast.makeText(AddressActivity.this, "Please fill all the required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save the hours as a map
        Map<String, WorkingHours> hours = new HashMap<>();
        hours.put("sunday", new WorkingHours(sundaySwitch.isChecked(), sundayHours.getText().toString().trim()));
        hours.put("monday", new WorkingHours(mondaySwitch.isChecked(), mondayHours.getText().toString().trim()));
        hours.put("tuesday", new WorkingHours(tuesdaySwitch.isChecked(), tuesdayHours.getText().toString().trim()));
        hours.put("wednesday", new WorkingHours(wednesdaySwitch.isChecked(), wednesdayHours.getText().toString().trim()));
        hours.put("thursday", new WorkingHours(thursdaySwitch.isChecked(), thursdayHours.getText().toString().trim()));
        hours.put("friday", new WorkingHours(fridaySwitch.isChecked(), fridayHours.getText().toString().trim()));
        hours.put("saturday", new WorkingHours(saturdaySwitch.isChecked(), saturdayHours.getText().toString().trim()));

        Address address = new Address(locType, barberName, street, building, cityName, state, zip, countryName, hours);
        databaseReference.child(barberId).child("address").setValue(address).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(AddressActivity.this, "Address Saved Successfully", Toast.LENGTH_SHORT).show();
                finish(); // Close activity after saving
            } else {
                Toast.makeText(AddressActivity.this, "Failed to Save Address", Toast.LENGTH_SHORT).show();
                Log.e("AddressActivity", "Failed to save address", task.getException());
            }
        });
    }

    public static class WorkingHours {
        public boolean available;
        public String hours;

        public WorkingHours() {
            // Default constructor required for calls to DataSnapshot.getValue(WorkingHours.class)
        }

        public WorkingHours(boolean available, String hours) {
            this.available = available;
            this.hours = hours;
        }
    }

    public static class Address {
        public String locationType, barbershopName, streetAddress, buildingFloor, city, stateRegion, zipCode, country;
        public Map<String, WorkingHours> hours;

        public Address() {
            // Default constructor required for calls to DataSnapshot.getValue(Address.class)
        }

        public Address(String locationType, String barbershopName, String streetAddress, String buildingFloor, String city, String stateRegion, String zipCode, String country, Map<String, WorkingHours> hours) {
            this.locationType = locationType;
            this.barbershopName = barbershopName;
            this.streetAddress = streetAddress;
            this.buildingFloor = buildingFloor;
            this.city = city;
            this.stateRegion = stateRegion;
            this.zipCode = zipCode;
            this.country = country;
            this.hours = hours;
        }
    }
}

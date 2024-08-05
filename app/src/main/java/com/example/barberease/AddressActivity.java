package com.example.barberease;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddressActivity extends AppCompatActivity {

    private EditText locationType, barbershopName, streetAddress, buildingFloor, city, stateRegion, zipCode, country;
    private Button saveButton;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private String barberId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        locationType = findViewById(R.id.location_type);
        barbershopName = findViewById(R.id.barbershop_name);
        streetAddress = findViewById(R.id.street_address);
        buildingFloor = findViewById(R.id.building_floor);
        city = findViewById(R.id.city);
        stateRegion = findViewById(R.id.state_region);
        zipCode = findViewById(R.id.zip_code);
        country = findViewById(R.id.country);
        saveButton = findViewById(R.id.save_button);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("barbers");
        barberId = mAuth.getCurrentUser().getUid();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAddress();
            }
        });
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

        Address address = new Address(locType, barberName, street, building, cityName, state, zip, countryName);
        databaseReference.child(barberId).child("address").setValue(address).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(AddressActivity.this, "Address Saved Successfully", Toast.LENGTH_SHORT).show();
                finish(); // Close activity after saving
            } else {
                Toast.makeText(AddressActivity.this, "Failed to Save Address", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class Address {
        public String locationType, barbershopName, streetAddress, buildingFloor, city, stateRegion, zipCode, country;

        public Address() {
        }

        public Address(String locationType, String barbershopName, String streetAddress, String buildingFloor, String city, String stateRegion, String zipCode, String country) {
            this.locationType = locationType;
            this.barbershopName = barbershopName;
            this.streetAddress = streetAddress;
            this.buildingFloor = buildingFloor;
            this.city = city;
            this.stateRegion = stateRegion;
            this.zipCode = zipCode;
            this.country = country;
        }
    }
}

package com.example.barberease;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddDataActivity extends AppCompatActivity {

    private EditText nameEditText, phoneEditText, imageUrlEditText;
    private EditText streetAddressEditText, buildingFloorEditText, cityEditText, stateRegionEditText, zipCodeEditText, countryEditText, locationTypeEditText;
    private Button saveButton;
    private DatabaseReference barbersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        nameEditText = findViewById(R.id.name);
        phoneEditText = findViewById(R.id.phone);
        imageUrlEditText = findViewById(R.id.image_url);

        streetAddressEditText = findViewById(R.id.street_address);
        buildingFloorEditText = findViewById(R.id.building_floor);
        cityEditText = findViewById(R.id.city);
        stateRegionEditText = findViewById(R.id.state_region);
        zipCodeEditText = findViewById(R.id.zip_code);
        countryEditText = findViewById(R.id.country);
        locationTypeEditText = findViewById(R.id.location_type);

        saveButton = findViewById(R.id.save_button);
        barbersRef = FirebaseDatabase.getInstance().getReference("barbers");

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBarber();
            }
        });
    }

    private void saveBarber() {
        String name = nameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String imageUrl = imageUrlEditText.getText().toString().trim();

        String streetAddress = streetAddressEditText.getText().toString().trim();
        String buildingFloor = buildingFloorEditText.getText().toString().trim();
        String city = cityEditText.getText().toString().trim();
        String stateRegion = stateRegionEditText.getText().toString().trim();
        String zipCode = zipCodeEditText.getText().toString().trim();
        String country = countryEditText.getText().toString().trim();
        String locationType = locationTypeEditText.getText().toString().trim();


        Barber barber = new Barber();
//        Barber barber = new Barber(
//                /* userId */ "someId",
//                imageUrl,
//                name,
//                phone,
//                /* details */ "some details",
//                /* price */ "some price",
//                /* schedule */ "some schedule",
//                /* payments */ "some payments",
//                /* haircut */ true,
//                /* shave */ false,
//                /* coloring */ true,
//                /* styling */ true,
//                locationType,
//                name,
//                streetAddress,
//                buildingFloor,
//                city,
//                stateRegion,
//                zipCode,
//                country,null,null
//        );

        barbersRef.child("barber1").setValue(barber).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Handle success
            } else {
                // Handle failure
            }
        });
    }
}

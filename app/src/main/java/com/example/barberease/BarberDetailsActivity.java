package com.example.barberease;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BarberDetailsActivity extends AppCompatActivity {

    private static final String TAG = "BarberDetailsActivity";

    private TextView barberNameTextView, barberPhoneTextView, barberBioTextView, barberAddressTextView, barberHoursTextView;
    private ImageView barberImageView;
    private Button btnDatePicker, btnTimePicker, btnConfirmAppointment;
    private DatabaseReference databaseReference;
    private String barberId;
    private String selectedDate, selectedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barber_details);

        // Initialize UI components
        barberNameTextView = findViewById(R.id.barber_name);
        barberPhoneTextView = findViewById(R.id.barber_phone);
        barberBioTextView = findViewById(R.id.barber_bio);
        barberImageView = findViewById(R.id.barber_image);
        barberAddressTextView = findViewById(R.id.barber_address);
        barberHoursTextView = findViewById(R.id.barber_hours);
        btnDatePicker = findViewById(R.id.btn_date_picker);
        btnTimePicker = findViewById(R.id.btn_time_picker);
        btnConfirmAppointment = findViewById(R.id.btn_confirm_appointment);

        // Get the barberId from the intent
        barberId = getIntent().getStringExtra("barberId");
        Log.d(TAG, "Received barberId: " + barberId);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("barbers");

        // Fetch and display barber details
        fetchBarberDetails();

        // Set up date picker dialog
        btnDatePicker.setOnClickListener(v -> showDatePickerDialog());

        // Set up time picker dialog
        btnTimePicker.setOnClickListener(v -> showTimePickerDialog());

        // Confirm appointment and save to Firebase
        btnConfirmAppointment.setOnClickListener(v -> confirmAppointment());
    }

    private void fetchBarberDetails() {
        if (barberId == null || barberId.isEmpty()) {
            Log.e(TAG, "Barber ID is null or empty. Cannot fetch details.");
            Toast.makeText(this, "Unable to load barber details.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Fetch barber details from Firebase
        databaseReference.child(barberId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Barber barber = snapshot.getValue(Barber.class);
                if (barber != null) {
                    displayBarberDetails(barber);
                } else {
                    Toast.makeText(BarberDetailsActivity.this, "Failed to load barber details.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BarberDetailsActivity.this, "Error loading barber details.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayBarberDetails(Barber barber) {
        barberNameTextView.setText(barber.getName() != null ? barber.getName() : "Name not available");
        barberPhoneTextView.setText(barber.getPhone() != null ? barber.getPhone() : "Phone not available");
        barberBioTextView.setText(barber.getBio() != null ? barber.getBio() : "Bio not available");

        if (barber.getImageUrl() != null && !barber.getImageUrl().isEmpty()) {
            Picasso.get().load(barber.getImageUrl()).into(barberImageView);
        } else {
            barberImageView.setImageResource(R.drawable.ic_profile); // Default profile image
        }

        Address address = barber.getAddress();
        if (address != null) {
            String fullAddress = String.format("%s, %s, %s, %s, %s, %s",
                    address.getStreetAddress() != null ? address.getStreetAddress() : "",
                    address.getBuildingFloor() != null ? address.getBuildingFloor() : "",
                    address.getCity() != null ? address.getCity() : "",
                    address.getStateRegion() != null ? address.getStateRegion() : "",
                    address.getZipCode() != null ? address.getZipCode() : "",
                    address.getCountry() != null ? address.getCountry() : "");
            barberAddressTextView.setText(fullAddress);

            StringBuilder hoursStringBuilder = new StringBuilder();
            Map<String, Address.WorkingHours> hours = address.getHours();
            if (hours != null) {
                for (Map.Entry<String, Address.WorkingHours> entry : hours.entrySet()) {
                    Address.WorkingHours workingHours = entry.getValue();
                    if (workingHours != null) {
                        hoursStringBuilder.append(entry.getKey())
                                .append(": ")
                                .append(workingHours.isAvailable() ? workingHours.getHours() : "Closed")
                                .append("\n");
                    }
                }
                barberHoursTextView.setText(hoursStringBuilder.toString());
            } else {
                barberHoursTextView.setText("Working hours not available");
            }
        } else {
            barberAddressTextView.setText("Address not available");
            barberHoursTextView.setText("Working hours not available");
        }
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    Calendar selectedCalendar = Calendar.getInstance();
                    selectedCalendar.set(year, month, dayOfMonth);

                    // Check if the selected date is before today
                    if (selectedCalendar.before(Calendar.getInstance())) {
                        Toast.makeText(BarberDetailsActivity.this, "Please select a future date.", Toast.LENGTH_SHORT).show();
                    } else {
                        selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        btnDatePicker.setText(selectedDate);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        // Set the minimum date to today
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }


    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    Calendar selectedCalendar = Calendar.getInstance();
                    selectedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    selectedCalendar.set(Calendar.MINUTE, minute);

                    // Check if the selected time is before the current time today
                    if (selectedDate != null && selectedDate.equals(getCurrentDate()) && selectedCalendar.before(Calendar.getInstance())) {
                        Toast.makeText(BarberDetailsActivity.this, "Please select a future time.", Toast.LENGTH_SHORT).show();
                    } else {
                        selectedTime = hourOfDay + ":" + String.format("%02d", minute);
                        btnTimePicker.setText(selectedTime);
                    }
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true);

        timePickerDialog.show();
    }

    // Helper method to get the current date in the same format as selectedDate
    private String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);
    }


    private void confirmAppointment() {
        if (selectedDate == null || selectedTime == null) {
            Toast.makeText(this, "Please select a date and time.", Toast.LENGTH_SHORT).show();
            return;
        }

        String customerId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Assuming user is logged in

        // Create appointment data
        Map<String, Object> appointmentData = new HashMap<>();
        appointmentData.put("date", selectedDate);
        appointmentData.put("time", selectedTime);
        appointmentData.put("barberId", barberId);
        appointmentData.put("customerId", customerId);
        appointmentData.put("barberName", barberNameTextView.getText().toString());
        appointmentData.put("status", "Upcoming");

        DatabaseReference appointmentRef = FirebaseDatabase.getInstance().getReference("appointments");

        // Save under customer's node
        appointmentRef.child("customers").child(customerId).child("upcoming").push().setValue(appointmentData)
                .addOnSuccessListener(aVoid -> Toast.makeText(BarberDetailsActivity.this, "Appointment booked successfully!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(BarberDetailsActivity.this, "Failed to book appointment.", Toast.LENGTH_SHORT).show());

        // Save under barber's node
        appointmentRef.child("barbers").child(barberId).child("upcoming").push().setValue(appointmentData)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Appointment also added to barber's node"))
                .addOnFailureListener(e -> Log.e(TAG, "Failed to add appointment to barber's node"));
    }

}

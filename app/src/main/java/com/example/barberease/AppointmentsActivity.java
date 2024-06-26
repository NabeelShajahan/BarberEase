package com.example.barberease;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class AppointmentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);

        ImageButton navHome = findViewById(R.id.nav_home);
        ImageButton navAppointments = findViewById(R.id.nav_appointments);
        ImageButton navProfile = findViewById(R.id.nav_profile);

        navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AppointmentsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        navAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Current activity, no action needed
            }
        });

        navProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AppointmentsActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}

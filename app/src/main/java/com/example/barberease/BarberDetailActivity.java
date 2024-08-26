package com.example.barberease;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class BarberDetailActivity extends AppCompatActivity {

    private TextView barberName, barberDetails, barberPrice, barberSchedule, barberPayments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barber_detail);

        barberName = findViewById(R.id.barber_name);
        barberDetails = findViewById(R.id.barber_details);
        barberPrice = findViewById(R.id.barber_price);
        barberSchedule = findViewById(R.id.barber_schedule);
        barberPayments = findViewById(R.id.barber_payments);

        Barber barber = (Barber) getIntent().getSerializableExtra("barber");

        if (barber != null) {
            barberName.setText(barber.getName());
            barberDetails.setText(barber.getBio());
//            barberPrice.setText(barber.getPrice());
//            barberSchedule.setText(barber.getSchedule());
//            barberPayments.setText(barber.getPayments());
        }
    }
}

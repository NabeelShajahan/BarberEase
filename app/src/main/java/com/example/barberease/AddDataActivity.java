package com.example.barberease;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddDataActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        addShopsData();
        addBarbersData();
    }

    private void addShopsData() {
        DatabaseReference shopsRef = mDatabase.child("shops");
        shopsRef.child("shop1").setValue(new BarberShop("https://example.com/shop1.jpg"));
        shopsRef.child("shop2").setValue(new BarberShop("https://example.com/shop2.jpg"));
    }

    private void addBarbersData() {
        DatabaseReference barbersRef = mDatabase.child("barbers");
        barbersRef.child("barber1").setValue(new Barber(
                "barber1Id",
                "https://example.com/barber1.jpg",
                "Barber One",
                "Details about Barber One",
                "$20",
                "9 AM - 5 PM",
                "PayPal",
                true,
                true,
                false,
                true
        ));
        barbersRef.child("barber2").setValue(new Barber(
                "barber2Id",
                "https://example.com/barber2.jpg",
                "Barber Two",
                "Details about Barber Two",
                "$25",
                "10 AM - 6 PM",
                "Stripe",
                true,
                false,
                true,
                false
        ));
    }
}

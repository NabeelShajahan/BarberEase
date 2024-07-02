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
        barbersRef.child("barber1").setValue(new BarberShop("https://example.com/barber1.jpg"));
        barbersRef.child("barber2").setValue(new BarberShop("https://example.com/barber2.jpg"));
    }
}

package com.example.barberease;

public class BarberShop {
    public String imageUrl;

    public BarberShop() {
        // Default constructor required for calls to DataSnapshot.getValue(BarberShop.class)
    }

    public BarberShop(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

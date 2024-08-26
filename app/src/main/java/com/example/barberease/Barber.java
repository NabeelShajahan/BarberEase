package com.example.barberease;

import java.util.List;
import java.util.Map;

public class Barber {
    private String userId;
    private String imageUrl;
    private String name;
    private String phone;
    private String bio;
    private Address address; // Nested Address object
    private List<String> photos; // List of photo URLs
    private Map<String, Service> services; // Map of services

    public Barber() {
        // Default constructor required for calls to DataSnapshot.getValue(Barber.class)
    }

    public Barber(String userId, String imageUrl, String name, String phone, String bio, Address address, List<String> photos, Map<String, Service> services) {
        this.userId = userId;
        this.imageUrl = imageUrl;
        this.name = name;
        this.phone = phone;
        this.bio = bio;
        this.address = address;
        this.photos = photos;
        this.services = services;
    }

    // Getters and setters

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public Map<String, Service> getServices() {
        return services;
    }

    public void setServices(Map<String, Service> services) {
        this.services = services;
    }
}

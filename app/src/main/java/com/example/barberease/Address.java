package com.example.barberease;

import java.util.Map;

public class Address {
    private String locationType;
    private String barbershopName;
    private String streetAddress;
    private String buildingFloor;
    private String city;
    private String stateRegion;
    private String zipCode;
    private String country;
    private Map<String, WorkingHours> hours; // Using WorkingHours class

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

    // Getters and setters for each field

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public String getBarbershopName() {
        return barbershopName;
    }

    public void setBarbershopName(String barbershopName) {
        this.barbershopName = barbershopName;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getBuildingFloor() {
        return buildingFloor;
    }

    public void setBuildingFloor(String buildingFloor) {
        this.buildingFloor = buildingFloor;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateRegion() {
        return stateRegion;
    }

    public void setStateRegion(String stateRegion) {
        this.stateRegion = stateRegion;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Map<String, WorkingHours> getHours() {
        return hours;
    }

    public void setHours(Map<String, WorkingHours> hours) {
        this.hours = hours;
    }

    public static class WorkingHours {
        private boolean available;
        private String hours;

        public WorkingHours() {
            // Default constructor required for calls to DataSnapshot.getValue(WorkingHours.class)
        }

        public WorkingHours(boolean available, String hours) {
            this.available = available;
            this.hours = hours;
        }

        // Getters and setters for each field

        public boolean isAvailable() {
            return available;
        }

        public void setAvailable(boolean available) {
            this.available = available;
        }

        public String getHours() {
            return hours;
        }

        public void setHours(String hours) {
            this.hours = hours;
        }
    }
}

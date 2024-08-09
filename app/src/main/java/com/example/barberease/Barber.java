package com.example.barberease;

public class Barber {
    private String userId;
    private String imageUrl;
    private String name;
    private String phone;
    private String details;
    private String price;
    private String schedule;
    private String payments;
    private boolean haircut;
    private boolean shave;
    private boolean coloring;
    private boolean styling;
    private String locationType;
    private String barbershopName;
    private String streetAddress;
    private String buildingFloor;
    private String city;
    private String stateRegion;
    private String zipCode;
    private String country;

    public Barber() {
        // Default constructor required for calls to DataSnapshot.getValue(Barber.class)
    }

    public Barber(String userId, String imageUrl, String name, String phone, String details, String price, String schedule, String payments, boolean haircut, boolean shave, boolean coloring, boolean styling, String locationType, String barbershopName, String streetAddress, String buildingFloor, String city, String stateRegion, String zipCode, String country) {
        this.userId = userId;
        this.imageUrl = imageUrl;
        this.name = name;
        this.phone = phone;
        this.details = details;
        this.price = price;
        this.schedule = schedule;
        this.payments = payments;
        this.haircut = haircut;
        this.shave = shave;
        this.coloring = coloring;
        this.styling = styling;
        this.locationType = locationType;
        this.barbershopName = barbershopName;
        this.streetAddress = streetAddress;
        this.buildingFloor = buildingFloor;
        this.city = city;
        this.stateRegion = stateRegion;
        this.zipCode = zipCode;
        this.country = country;
    }

    // Getter and Setter methods for all fields

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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getPayments() {
        return payments;
    }

    public void setPayments(String payments) {
        this.payments = payments;
    }

    public boolean isHaircut() {
        return haircut;
    }

    public void setHaircut(boolean haircut) {
        this.haircut = haircut;
    }

    public boolean isShave() {
        return shave;
    }

    public void setShave(boolean shave) {
        this.shave = shave;
    }

    public boolean isColoring() {
        return coloring;
    }

    public void setColoring(boolean coloring) {
        this.coloring = coloring;
    }

    public boolean isStyling() {
        return styling;
    }

    public void setStyling(boolean styling) {
        this.styling = styling;
    }

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
}

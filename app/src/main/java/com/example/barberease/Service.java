package com.example.barberease;

public class Service {
    private String serviceId;
    private String barberId;
    private String name;
    private String price;
    private String duration;
    private String description;
    private boolean premiumHours;
    private boolean offerPromotion;

    public Service() {
        // Default constructor required for calls to DataSnapshot.getValue(Service.class)
    }

    public Service(String serviceId, String barberId, String name, String price, String duration, String description, boolean premiumHours, boolean offerPromotion) {
        this.serviceId = serviceId;
        this.barberId = barberId;
        this.name = name;
        this.price = price;
        this.duration = duration;
        this.description = description;
        this.premiumHours = premiumHours;
        this.offerPromotion = offerPromotion;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getBarberId() {
        return barberId;
    }

    public void setBarberId(String barberId) {
        this.barberId = barberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPremiumHours() {
        return premiumHours;
    }

    public void setPremiumHours(boolean premiumHours) {
        this.premiumHours = premiumHours;
    }

    public boolean isOfferPromotion() {
        return offerPromotion;
    }

    public void setOfferPromotion(boolean offerPromotion) {
        this.offerPromotion = offerPromotion;
    }
}

package com.example.barberease;

public class Category {
    private String name;
    private String imageResource;

    public Category() {
        // Default constructor required for calls to DataSnapshot.getValue(Category.class)
    }

    public Category(String name, String imageResource) {
        this.name = name;
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageResource() {
        return imageResource;
    }

    public void setImageResource(String imageResource) {
        this.imageResource = imageResource;
    }
}
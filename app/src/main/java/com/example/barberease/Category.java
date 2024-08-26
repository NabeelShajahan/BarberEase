package com.example.barberease;

public class Category {
    private String imageResource;
    private String name;

    // Default constructor required for calls to DataSnapshot.getValue(Category.class)
    public Category() {
    }

    public Category(String imageResource, String name) {
        this.imageResource = imageResource;
        this.name = name;
    }

    public String getImageResource() {
        return imageResource;
    }

    public void setImageResource(String imageResource) {
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

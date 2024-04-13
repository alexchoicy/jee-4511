package com.cems.Model;

import java.io.Serializable;

public class Equipment implements Serializable {
    private int id;
    private String name;
    private String description;
    private String imagePath;
    private int availableQuantity;
    private boolean isStaffOnly;
    private boolean isListed;
    private boolean isWishListed;

    public Equipment() {

    }

    public Equipment(String name, String description, String imagePath,  boolean isStaffOnly, boolean isListed) {
        this.name = name;
        this.description = description;
        this.imagePath = imagePath;
        this.isStaffOnly = isStaffOnly;
        this.isListed = isListed;
    }

    public boolean isListed() {
        return isListed;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setListed(boolean listed) {
        isListed = listed;
    }

    public boolean isWishListed() {
        return isWishListed;
    }

    public void setWishListed(boolean wishListed) {
        isWishListed = wishListed;
    }

    public boolean isStaffOnly() {
        return isStaffOnly;
    }

    public void setStaffOnly(boolean staffOnly) {
        isStaffOnly = staffOnly;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }
}




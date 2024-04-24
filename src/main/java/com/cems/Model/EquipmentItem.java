package com.cems.Model;

import com.cems.Enums.ItemStatus;

import java.io.Serializable;

public class EquipmentItem implements Serializable {
    private int id;
    private String serialNumber;
    private int borrowedTimes;
    private boolean isStaffOnly;
    private boolean isListed;
    private ItemStatus status;
    private int equipmentId;
    private int currentLocation;
    private Location location;
    private String equipmentName;
    public int getId() {
        return id;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isListed() {
        return isListed;
    }

    public void setListed(boolean listed) {
        isListed = listed;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public int getBorrowedTimes() {
        return borrowedTimes;
    }

    public void setBorrowedTimes(int borrowedTimes) {
        this.borrowedTimes = borrowedTimes;
    }

    public boolean isStaffOnly() {
        return isStaffOnly;
    }

    public void setStaffOnly(boolean staffOnly) {
        isStaffOnly = staffOnly;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    public int getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(int equipmentId) {
        this.equipmentId = equipmentId;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(int currentLocation) {
        this.currentLocation = currentLocation;
    }
}

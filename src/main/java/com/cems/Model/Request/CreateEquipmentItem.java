package com.cems.Model.Request;

public class CreateEquipmentItem {
    private String serialNumber;
    private int status;
    private int location;
    private String errorMessages;
    public CreateEquipmentItem() {
    }

    public CreateEquipmentItem(String serialNumber,int status, int location) {
        this.serialNumber = serialNumber;
        this.status = status;
        this.location = location;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public String getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(String errorMessages) {
        this.errorMessages = errorMessages;
    }
}

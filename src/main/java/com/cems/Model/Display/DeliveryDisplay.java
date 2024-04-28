/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cems.Model.Display;

import java.io.Serializable;

/**
 *
 * @author MarkYu
 */
public class DeliveryDisplay implements Serializable {

    private int itemId;
    private int deliveryItemId;
    private int serialNumber;
    private int deliveryId;
    private int deliveryBy;
    private int courier;
    private int fromLocation;
    private int toLocation;
    private String itemName;
    private String loctionName;
    private String destionName;
    private String startTime;
    private String endTime;
    private String deadline;
    private String pickupDateTime;
    private String arriveDateTime;

    public int getDeliveryItemId() {
        return deliveryItemId;
    }

    public void setDeliveryItemId(int deliveryItemId) {
        this.deliveryItemId = deliveryItemId;
    }

    public int getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(int fromLocation) {
        this.fromLocation = fromLocation;
    }

    public int getToLocation() {
        return toLocation;
    }

    public void setToLocation(int toLocation) {
        this.toLocation = toLocation;
    }

    public int getDeliveryBy() {
        return deliveryBy;
    }

    public void setDeliveryBy(int deliveryBy) {
        this.deliveryBy = deliveryBy;
    }

    public String getPickupDateTime() {
        return pickupDateTime;
    }

    public void setPickupDateTime(String pickupDateTime) {
        this.pickupDateTime = pickupDateTime;
    }

    public String getArriveDateTime() {
        return arriveDateTime;
    }

    public void setArriveDateTime(String arriveDateTime) {
        this.arriveDateTime = arriveDateTime;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getLoctionName() {
        return loctionName;
    }

    public void setLoctionName(String loctionName) {
        this.loctionName = loctionName;
    }

    public String getDestionName() {
        return destionName;
    }

    public void setDestionName(String destionName) {
        this.destionName = destionName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getCourier() {
        return courier;
    }

    public void setCourier(int courier) {
        this.courier = courier;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public int getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(int deliveryId) {
        this.deliveryId = deliveryId;
    }
}

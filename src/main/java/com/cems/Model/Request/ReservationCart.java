package com.cems.Model.Request;

import java.io.Serializable;
import java.sql.ResultSet;

public class ReservationCart implements Serializable {
    private int equipmentID;
    private String equipmentName;
    private int quantity;

    public int getEquipmentID() {
        return equipmentID;
    }

    public void setEquipmentID(int equipmentID) {
        this.equipmentID = equipmentID;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public static ReservationCart create(ResultSet resultSet, int quantity) {
        ReservationCart reservationCart = new ReservationCart();
        try {
            reservationCart.setEquipmentID(resultSet.getInt("equipment_id"));
            reservationCart.setEquipmentName(resultSet.getString("equipment_name"));
            reservationCart.setQuantity(quantity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reservationCart;
    }
}

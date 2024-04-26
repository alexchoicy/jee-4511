package com.cems.Model;

import com.cems.Enums.ReservationStatus;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Reservations implements Serializable {
    private int id;
    private User user;
    private Location destination;
    private Timestamp startTime;
    private Timestamp endTime;
    private Timestamp checkin_time;
    private Timestamp checkout_time;
    private Timestamp createdAt;
    private ReservationStatus status;
    private ArrayList<EquipmentItem> items;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Location getDestination() {
        return destination;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public ArrayList<EquipmentItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<EquipmentItem> items) {
        this.items = items;
    }

    public Timestamp getCheckin_time() {
        return checkin_time;
    }

    public void setCheckin_time(Timestamp checkin_time) {
        this.checkin_time = checkin_time;
    }

    public Timestamp getCheckout_time() {
        return checkout_time;
    }

    public void setCheckout_time(Timestamp checkout_time) {
        this.checkout_time = checkout_time;
    }

    public static Reservations create(ResultSet resultSet) throws SQLException {
        Reservations reservation = new Reservations();
        reservation.setId(resultSet.getInt("reservation_id"));
        reservation.setStartTime(resultSet.getTimestamp("start_time"));
        reservation.setEndTime(resultSet.getTimestamp("end_time"));
        reservation.setCreatedAt(resultSet.getTimestamp("CreatedAt"));
        reservation.setStatus(ReservationStatus.getStatus(resultSet.getInt("status")));
        reservation.setCheckin_time(resultSet.getTimestamp("checkin_time"));
        reservation.setCheckout_time(resultSet.getTimestamp("checkout_time"));
        reservation.setDestination(Location.create(resultSet));
        reservation.setUser(User.create(resultSet));
        return reservation;
    }
}

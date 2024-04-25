package com.cems.Model;

import com.cems.Enums.ReservationStatus;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Reservations implements Serializable {
    private int id;
    private User user;
    private Location destination;
    private Timestamp startTime;
    private Timestamp endTime;
    private Timestamp createdAt;
    private ReservationStatus status;

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

    public static Reservations create(ResultSet resultSet) throws SQLException {
        Reservations reservation = new Reservations();
        reservation.setId(resultSet.getInt("reservation_id"));
        reservation.setStartTime(resultSet.getTimestamp("start_time"));
        reservation.setEndTime(resultSet.getTimestamp("end_time"));
        reservation.setCreatedAt(resultSet.getTimestamp("CreatedAt"));
        reservation.setStatus(ReservationStatus.getStatus(resultSet.getInt("status")));
        reservation.setDestination(Location.create(resultSet));
        reservation.setUser(User.create(resultSet));
        return reservation;
    }
}

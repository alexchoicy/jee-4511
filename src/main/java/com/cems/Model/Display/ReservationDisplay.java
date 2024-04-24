package com.cems.Model.Display;

import com.cems.Model.Reservations;

import java.io.Serializable;
import java.util.ArrayList;

public class ReservationDisplay implements Serializable {
    private ArrayList<Reservations> waitingToApprove;
    private ArrayList<Reservations> approved;
    private ArrayList<Reservations> active;
    private ArrayList<Reservations> completed;

    public ArrayList<Reservations> getWaitingToApprove() {
        return waitingToApprove;
    }

    public void setWaitingToApprove(ArrayList<Reservations> waitingToApprove) {
        this.waitingToApprove = waitingToApprove;
    }

    public ArrayList<Reservations> getApproved() {
        return approved;
    }

    public void setApproved(ArrayList<Reservations> approved) {
        this.approved = approved;
    }

    public ArrayList<Reservations> getActive() {
        return active;
    }

    public void setActive(ArrayList<Reservations> active) {
        this.active = active;
    }

    public ArrayList<Reservations> getCompleted() {
        return completed;
    }

    public void setCompleted(ArrayList<Reservations> completed) {
        this.completed = completed;
    }
}

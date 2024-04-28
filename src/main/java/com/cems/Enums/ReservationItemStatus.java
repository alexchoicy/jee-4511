package com.cems.Enums;

import java.util.HashMap;

public enum ReservationItemStatus {
    NEED_DELIVERY(1),
    ARRIVED(2),
    HAVE_DELIVERY_ORDER(3);

    private final int value;

    private static final HashMap<Integer, ReservationItemStatus> map = new HashMap<>();

    static {
        for (ReservationItemStatus status : ReservationItemStatus.values()) {
            map.put(status.value, status);
        }
    }

    ReservationItemStatus(int value) {
        this.value = value;
    }

    public static ReservationItemStatus getStatus(int status) { return map.get(status);}

    public int getValue() {
        return value;
    }
}
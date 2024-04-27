package com.cems.Enums;

import java.util.HashMap;

public enum ReservationStatus {

    PENDING(1, "Pending for approval"),
    APPROVED(2, "Approved"),
    ACTIVE(3, "Active"),
    FINISHED(4, "Finished"),
    CANCELED(5, "Canceled"),
    REJECTED(6, "Rejected");
    private final int value;
    private final String displayValue;
    private static final HashMap<Integer, ReservationStatus> map = new HashMap<>();

    static {
        for (ReservationStatus status : ReservationStatus.values()) {
            map.put(status.value, status);
        }
    }

    ReservationStatus(int value, String displayValue) {
        this.value = value;
        this.displayValue = displayValue;
    }

    public static ReservationStatus getStatus(int status) {
        return map.get(status);
    }

    public int getValue() {
        return value;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}

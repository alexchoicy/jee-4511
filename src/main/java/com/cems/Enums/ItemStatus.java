package com.cems.Enums;

import java.util.HashMap;

public enum ItemStatus {

    AVAILABLE(1, "Available"),
    IN_USE(2, "In Use"),
    UNDER_MAINTENANCE(3, "Under Maintenance"),
    BOOKED(4, "is booked");
    private final int value;
    private final String displayValue;
    private static final HashMap<Integer, ItemStatus> map = new HashMap<>();

    static {
        for (ItemStatus status : ItemStatus.values()) {
            map.put(status.value, status);
        }
    }

    ItemStatus(int value, String displayValue) {
        this.value = value;
        this.displayValue = displayValue;
    }

    public static ItemStatus getStatus(int status) {
        return map.get(status);
    }

    public int getValue() {
        return value;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}

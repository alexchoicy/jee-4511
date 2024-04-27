package com.cems.Enums;

import java.util.HashMap;

public enum DamagedStatus {
    REPORTED(1, "Reported"),
    CONFIRMED(2, "Confirmed"),
    REPAIRED(3 ,"Repaired"),
    DISCARDED(4 ,"Discarded");

    private final int value;
    private final String name;
    private static final HashMap<Integer, DamagedStatus> map = new HashMap<>();

    static {
        for (DamagedStatus damagedStatus : DamagedStatus.values()) {
            map.put(damagedStatus.value, damagedStatus);
        }
    }
    DamagedStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public static DamagedStatus valueOf(int damagedStatus) {
        return map.get(damagedStatus);
    }

    public String getName() {
        return name;
    }



}

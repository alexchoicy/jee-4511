package com.cems.Enums.page;

public enum ReservationAction {
    APPROVE(1),
    DECLINE(2),
    CHECK_IN(3),
    CHECK_OUT(4);

    private final int value;
    private static final java.util.HashMap<Integer, ReservationAction> map = new java.util.HashMap<>();

    static {
        for (ReservationAction action : ReservationAction.values()) {
            map.put(action.value, action);
        }
    }

    ReservationAction(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ReservationAction getAction(int action) {
        return map.get(action);
    }
}

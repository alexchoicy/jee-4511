package com.cems.Enums;

import java.util.HashMap;

public enum UserRoles {
    ADMIN(1),
    TECHNICIAN(2),
    STAFF(3),
    COURIER(4),
    USER(5);

    private final int value;
    private static final HashMap<Integer, UserRoles> map = new HashMap<>();

    static {
        for (UserRoles role : UserRoles.values()) {
            map.put(role.value, role);
        }
    }

    UserRoles(int value) {
        this.value = value;
    }

    public static UserRoles getRole(int role) {
        return map.get(role);
    }

    public int getValue() {
        return value;
    }
}

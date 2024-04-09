package com.cems.Exceptions;

public class EquipmentNotFoundException extends Exception {
    public EquipmentNotFoundException(int id) {
        super("Equipment with id " + id + " not found");
    }
}

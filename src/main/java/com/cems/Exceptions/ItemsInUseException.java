package com.cems.Exceptions;

public class ItemsInUseException extends Exception {
    public ItemsInUseException(int equipmentId, int itemId) {
        super("Equipment with id " + equipmentId + " has item with id " + itemId + " in use");
    }
}


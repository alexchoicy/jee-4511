package com.cems.Exceptions;

public class ItemNotFoundException extends Exception {
    public ItemNotFoundException(int id) {
        super("Item with id " + id + " not found");
    }
}


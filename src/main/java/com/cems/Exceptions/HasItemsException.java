package com.cems.Exceptions;

public class HasItemsException extends Exception {
    public HasItemsException(int id) {
        super("Equipment with id " + id + " has items");
    }
}


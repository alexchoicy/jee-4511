package com.cems.Exceptions;

public class NotEnoughItemException extends Exception {
    public NotEnoughItemException(int equipmentId, int availableQuantity, int requested) {
        super(String.format("The ordered Item Equipment ID : %s, Current Available Quantity is %s, But Requested %s",equipmentId,availableQuantity,requested));
    }
}

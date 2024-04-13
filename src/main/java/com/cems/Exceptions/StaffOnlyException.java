package com.cems.Exceptions;

public class StaffOnlyException extends Exception{
    public StaffOnlyException(int equipmentId) {
        super("The equipment id "+ equipmentId + " is Staff Only ");
    }
}

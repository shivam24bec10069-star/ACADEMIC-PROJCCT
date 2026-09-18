package com.logiflow.exception;

public class InvalidShipmentDataException extends LogisticsException {
    public InvalidShipmentDataException(String fieldName, String reason) {
        super(String.format("Invalid data for field '%s': %s", fieldName, reason));
    }
}

package com.logiflow.exception;

public class ShipmentNotFoundException extends LogisticsException {
    public ShipmentNotFoundException(String shipmentId) {
        super("Shipment with ID '" + shipmentId + "' was not found in the system registry.");
    }
}

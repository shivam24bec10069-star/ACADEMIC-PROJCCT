package com.logiflow.exception;

public class InsufficientCapacityException extends LogisticsException {
    public InsufficientCapacityException(String vehicleId, double requestedWeight, double availableWeight) {
        super(String.format("Vehicle '%s' does not have sufficient payload capacity: requested %.2f kg, available %.2f kg.",
                vehicleId, requestedWeight, availableWeight));
    }
}

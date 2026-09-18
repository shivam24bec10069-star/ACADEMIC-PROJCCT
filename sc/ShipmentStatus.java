package com.logiflow.model;

/**
 * Enumeration representing the lifecycle states of a freight shipment.
 */
public enum ShipmentStatus {
    CREATED("Pending Assignment"),
    ASSIGNED("Assigned to Fleet"),
    IN_TRANSIT("In Transit"),
    DELAYED("Delayed (Traffic/Weather)"),
    DELIVERED("Successfully Delivered"),
    CANCELLED("Cancelled");

    private final String displayName;

    ShipmentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

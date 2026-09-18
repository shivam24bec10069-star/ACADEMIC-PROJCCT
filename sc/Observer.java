package com.logiflow.pattern;

import com.logiflow.model.Shipment;
import com.logiflow.model.ShipmentStatus;

/**
 * Observer interface for reacting to shipment status and telemetry events.
 */
public interface Observer {
    void onStatusUpdate(Shipment shipment, ShipmentStatus oldStatus, ShipmentStatus newStatus, String message);
    void onTelemetryAlert(String vehicleId, String message, boolean isWarning);
}

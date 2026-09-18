package com.logiflow.pattern;

import com.logiflow.model.Shipment;
import com.logiflow.model.ShipmentStatus;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Concrete Observer that outputs styled real-time event logs to the operational console.
 */
public class ConsoleNotificationListener implements Observer {
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public void onStatusUpdate(Shipment shipment, ShipmentStatus oldStatus, ShipmentStatus newStatus, String message) {
        String timestamp = LocalTime.now().format(TIME_FMT);
        System.out.printf("[EVENT %s] >>> Shipment %s: %s -> %s | Note: %s%n",
                timestamp, shipment.getId(), oldStatus, newStatus, message);
    }

    @Override
    public void onTelemetryAlert(String vehicleId, String message, boolean isWarning) {
        String timestamp = LocalTime.now().format(TIME_FMT);
        String level = isWarning ? "[ALERT-WARN]" : "[TELEMETRY ]";
        System.out.printf("%s [%s] Fleet Vehicle %s: %s%n",
                level, timestamp, vehicleId, message);
    }
}

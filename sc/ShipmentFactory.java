package com.logiflow.pattern;

import com.logiflow.exception.InvalidShipmentDataException;
import com.logiflow.model.ExpressShipment;
import com.logiflow.model.FragileShipment;
import com.logiflow.model.Shipment;
import com.logiflow.model.StandardShipment;

/**
 * Creational Design Pattern: Factory Pattern for dynamic instantiation of polymorphic Shipment entities.
 */
public class ShipmentFactory {

    public static Shipment createShipment(String type, String id, String sender, String receiver,
                                          String origin, String destination, double weight,
                                          double volume, double declaredValue, double extraParam)
            throws InvalidShipmentDataException {

        if (id == null || id.trim().isEmpty()) {
            throw new InvalidShipmentDataException("id", "Tracking ID cannot be null or empty.");
        }
        if (weight <= 0) {
            throw new InvalidShipmentDataException("weightKg", "Weight must be strictly positive.");
        }
        if (volume <= 0) {
            throw new InvalidShipmentDataException("volumeM3", "Volume must be strictly positive.");
        }

        String normalizedType = type.trim().toUpperCase();
        switch (normalizedType) {
            case "STANDARD":
                return new StandardShipment(id, sender, receiver, origin, destination, weight, volume, declaredValue);
            case "EXPRESS":
                int slaHours = extraParam > 0 ? (int) extraParam : 24;
                return new ExpressShipment(id, sender, receiver, origin, destination, weight, volume, declaredValue, slaHours);
            case "FRAGILE":
                double maxG = extraParam > 0 ? extraParam : 2.5;
                return new FragileShipment(id, sender, receiver, origin, destination, weight, volume, declaredValue, maxG);
            default:
                throw new InvalidShipmentDataException("type", "Unknown shipment classification: " + type);
        }
    }
}

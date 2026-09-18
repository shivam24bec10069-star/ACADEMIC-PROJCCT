package com.logiflow.model;

import com.logiflow.exception.InsufficientCapacityException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a physical transport asset in the company's dispatch fleet.
 * Demonstrates state management, capacity validation, and encapsulation.
 */
public class Vehicle {
    private final String id;
    private final VehicleType type;
    private String driverName;
    private String currentLocationCode;
    private double currentPayloadKg;
    private double currentVolumeM3;
    private boolean available;
    private double fuelOrBatteryPercent;
    private double totalKmDriven;
    private final List<String> loadedShipmentIds;

    public Vehicle(String id, VehicleType type, String driverName, String currentLocationCode) {
        this.id = id;
        this.type = type;
        this.driverName = driverName;
        this.currentLocationCode = currentLocationCode;
        this.currentPayloadKg = 0.0;
        this.currentVolumeM3 = 0.0;
        this.available = true;
        this.fuelOrBatteryPercent = 100.0;
        this.totalKmDriven = 0.0;
        this.loadedShipmentIds = new ArrayList<>();
    }

    public synchronized boolean canAccommodate(double weightKg, double volumeM3) {
        return (currentPayloadKg + weightKg <= type.getMaxPayloadKg()) &&
               (currentVolumeM3 + volumeM3 <= type.getMaxVolumeM3()) &&
               available;
    }

    public synchronized void loadShipment(Shipment shipment) throws InsufficientCapacityException {
        if (!canAccommodate(shipment.getWeightKg(), shipment.getVolumeM3())) {
            double remaining = type.getMaxPayloadKg() - currentPayloadKg;
            throw new InsufficientCapacityException(id, shipment.getWeightKg(), remaining);
        }
        loadedShipmentIds.add(shipment.getId());
        currentPayloadKg += shipment.getWeightKg();
        currentVolumeM3 += shipment.getVolumeM3();
        shipment.setAssignedVehicleId(this.id);
        shipment.setStatus(ShipmentStatus.ASSIGNED);
    }

    public synchronized void unloadShipment(Shipment shipment) {
        if (loadedShipmentIds.remove(shipment.getId())) {
            currentPayloadKg = Math.max(0.0, currentPayloadKg - shipment.getWeightKg());
            currentVolumeM3 = Math.max(0.0, currentVolumeM3 - shipment.getVolumeM3());
        }
    }

    public synchronized void completeMission(double distanceKm, String newLocationCode) {
        this.currentLocationCode = newLocationCode;
        this.totalKmDriven += distanceKm;
        // Decrease fuel/battery linearly based on distance (approx 1% per 15 km)
        this.fuelOrBatteryPercent = Math.max(5.0, this.fuelOrBatteryPercent - (distanceKm * 0.065));
        this.available = true;
        this.loadedShipmentIds.clear();
        this.currentPayloadKg = 0.0;
        this.currentVolumeM3 = 0.0;
    }

    public double getUtilizationPercentage() {
        return (currentPayloadKg / type.getMaxPayloadKg()) * 100.0;
    }

    public String getId() { return id; }
    public VehicleType getType() { return type; }
    public String getDriverName() { return driverName; }
    public void setDriverName(String driverName) { this.driverName = driverName; }
    public String getCurrentLocationCode() { return currentLocationCode; }
    public void setCurrentLocationCode(String currentLocationCode) { this.currentLocationCode = currentLocationCode; }
    public double getCurrentPayloadKg() { return currentPayloadKg; }
    public double getCurrentVolumeM3() { return currentVolumeM3; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    public double getFuelOrBatteryPercent() { return fuelOrBatteryPercent; }
    public void refuel() { this.fuelOrBatteryPercent = 100.0; }
    public double getTotalKmDriven() { return totalKmDriven; }
    public List<String> getLoadedShipmentIds() { return Collections.unmodifiableList(loadedShipmentIds); }

    @Override
    public String toString() {
        return String.format("[%s | %-12s] Driver: %-14s | Hub: %s | Load: %6.1f/%-6.1f kg (%.1f%%) | Fuel: %.0f%% | Status: %s",
                id, type.getLabel(), driverName, currentLocationCode, currentPayloadKg,
                type.getMaxPayloadKg(), getUtilizationPercentage(), fuelOrBatteryPercent,
                available ? "READY" : "DISPATCHED");
    }
}

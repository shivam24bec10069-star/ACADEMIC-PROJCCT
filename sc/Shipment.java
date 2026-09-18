package com.logiflow.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Abstract domain model representing a freight consignment in the logistics system.
 * Demonstrates Object-Oriented Principles: Encapsulation, Abstraction, Polymorphism.
 */
public abstract class Shipment implements Comparable<Shipment> {
    private final String id;
    private final String sender;
    private final String receiver;
    private final String originCode;
    private final String destinationCode;
    private final double weightKg;
    private final double volumeM3;
    private final double declaredValue;
    private ShipmentStatus status;
    private String assignedVehicleId;
    private final LocalDateTime creationTime;
    private final int priorityScore;

    public Shipment(String id, String sender, String receiver, String originCode,
                    String destinationCode, double weightKg, double volumeM3,
                    double declaredValue, int priorityScore) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.originCode = originCode;
        this.destinationCode = destinationCode;
        this.weightKg = weightKg;
        this.volumeM3 = volumeM3;
        this.declaredValue = declaredValue;
        this.status = ShipmentStatus.CREATED;
        this.assignedVehicleId = null;
        this.creationTime = LocalDateTime.now();
        this.priorityScore = priorityScore;
    }

    /**
     * Polymorphic calculation of freight tariff based on weight, volume, distance, and SLA rules.
     */
    public abstract double calculateCost(double distanceKm);

    /**
     * Identifies the category/type of the shipment.
     */
    public abstract String getShipmentType();

    /**
     * Special cargo handling instructions for logistics handlers and drivers.
     */
    public abstract String getHandlingInstructions();

    // Priority ordering: Higher priority score comes first. If equal, earlier creation comes first.
    @Override
    public int compareTo(Shipment other) {
        int scoreComparison = Integer.compare(other.priorityScore, this.priorityScore);
        if (scoreComparison != 0) {
            return scoreComparison;
        }
        return this.creationTime.compareTo(other.creationTime);
    }

    public String getId() { return id; }
    public String getSender() { return sender; }
    public String getReceiver() { return receiver; }
    public String getOriginCode() { return originCode; }
    public String getDestinationCode() { return destinationCode; }
    public double getWeightKg() { return weightKg; }
    public double getVolumeM3() { return volumeM3; }
    public double getDeclaredValue() { return declaredValue; }
    public ShipmentStatus getStatus() { return status; }
    public void setStatus(ShipmentStatus status) { this.status = status; }
    public String getAssignedVehicleId() { return assignedVehicleId; }
    public void setAssignedVehicleId(String assignedVehicleId) { this.assignedVehicleId = assignedVehicleId; }
    public LocalDateTime getCreationTime() { return creationTime; }
    public int getPriorityScore() { return priorityScore; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shipment shipment = (Shipment) o;
        return Objects.equals(id, shipment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("[%s | %-8s] %s -> %s (%.1f kg, Status: %s)",
                id, getShipmentType(), originCode, destinationCode, weightKg, status.getDisplayName());
    }

    public String toDetailString(double distanceKm) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return String.format(
            "--------------------------------------------------\n" +
            " Tracking ID     : %s\n" +
            " Type            : %s\n" +
            " Priority Score  : %d\n" +
            " Route           : %s -> %s (~%.1f km)\n" +
            " Parties         : Sender: %s | Receiver: %s\n" +
            " Weight & Volume : %.2f kg | %.3f m³\n" +
            " Declared Value  : $%.2f\n" +
            " Status          : %s\n" +
            " Assigned Fleet  : %s\n" +
            " Created At      : %s\n" +
            " Tariff / Cost   : $%.2f\n" +
            " Handling Note   : %s\n" +
            "--------------------------------------------------",
            id, getShipmentType(), priorityScore, originCode, destinationCode, distanceKm,
            sender, receiver, weightKg, volumeM3, declaredValue, status.getDisplayName(),
            assignedVehicleId == null ? "None (Awaiting Dispatch)" : assignedVehicleId,
            creationTime.format(dtf), calculateCost(distanceKm), getHandlingInstructions()
        );
    }
}

package com.logiflow.model;

/**
 * Represents standard commercial or consumer freight with typical turnaround times.
 */
public class StandardShipment extends Shipment {
    private static final double BASE_FLAT_RATE = 15.0;
    private static final double RATE_PER_KG = 0.85;
    private static final double RATE_PER_KM = 0.05;

    public StandardShipment(String id, String sender, String receiver, String originCode,
                            String destinationCode, double weightKg, double volumeM3,
                            double declaredValue) {
        super(id, sender, receiver, originCode, destinationCode, weightKg, volumeM3, declaredValue, 10);
    }

    @Override
    public double calculateCost(double distanceKm) {
        double cost = BASE_FLAT_RATE + (getWeightKg() * RATE_PER_KG) + (distanceKm * RATE_PER_KM);
        return Math.round(cost * 100.0) / 100.0;
    }

    @Override
    public String getShipmentType() {
        return "STANDARD";
    }

    @Override
    public String getHandlingInstructions() {
        return "Standard industrial palletizing; ambient dry storage.";
    }
}

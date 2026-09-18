package com.logiflow.model;

/**
 * Represents sensitive, high-value, or breakable freight requiring shock damping and inspection.
 */
public class FragileShipment extends Shipment {
    private static final double BASE_FLAT_RATE = 25.0;
    private static final double RATE_PER_KG = 1.10;
    private static final double RATE_PER_KM = 0.08;
    private static final double FRAGILE_INSURANCE_FACTOR = 0.015; // 1.5% of declared value

    private final double maxGForceAllowed;

    public FragileShipment(String id, String sender, String receiver, String originCode,
                           String destinationCode, double weightKg, double volumeM3,
                           double declaredValue, double maxGForceAllowed) {
        super(id, sender, receiver, originCode, destinationCode, weightKg, volumeM3, declaredValue, 30);
        this.maxGForceAllowed = maxGForceAllowed;
    }

    @Override
    public double calculateCost(double distanceKm) {
        double insuranceFee = getDeclaredValue() * FRAGILE_INSURANCE_FACTOR;
        double cost = BASE_FLAT_RATE + (getWeightKg() * RATE_PER_KG) + (distanceKm * RATE_PER_KM) + insuranceFee;
        return Math.round(cost * 100.0) / 100.0;
    }

    @Override
    public String getShipmentType() {
        return "FRAGILE";
    }

    public double getMaxGForceAllowed() {
        return maxGForceAllowed;
    }

    @Override
    public String getHandlingInstructions() {
        return String.format("FRAGILE SENSITIVE: Keep upright, do not stack. Shock limit: %.1fG. Temperature controlled.",
                maxGForceAllowed);
    }
}

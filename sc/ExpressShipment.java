package com.logiflow.model;

/**
 * Represents high-priority expedited freight with strict delivery SLA windows.
 */
public class ExpressShipment extends Shipment {
    private static final double BASE_FLAT_RATE = 35.0;
    private static final double RATE_PER_KG = 1.60;
    private static final double RATE_PER_KM = 0.12;
    private static final double URGENT_DISPATCH_FEE = 20.0;
    private final int slaHoursGuarantee;

    public ExpressShipment(String id, String sender, String receiver, String originCode,
                           String destinationCode, double weightKg, double volumeM3,
                           double declaredValue, int slaHoursGuarantee) {
        super(id, sender, receiver, originCode, destinationCode, weightKg, volumeM3, declaredValue, 50);
        this.slaHoursGuarantee = slaHoursGuarantee;
    }

    @Override
    public double calculateCost(double distanceKm) {
        double cost = BASE_FLAT_RATE + URGENT_DISPATCH_FEE + (getWeightKg() * RATE_PER_KG) + (distanceKm * RATE_PER_KM);
        return Math.round(cost * 100.0) / 100.0;
    }

    @Override
    public String getShipmentType() {
        return "EXPRESS";
    }

    public int getSlaHoursGuarantee() {
        return slaHoursGuarantee;
    }

    @Override
    public String getHandlingInstructions() {
        return String.format("EXPEDITED PRIORITY: Maximum %d-hour SLA window. Direct routing mandatory.", slaHoursGuarantee);
    }
}

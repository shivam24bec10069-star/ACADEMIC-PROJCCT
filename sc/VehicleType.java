package com.logiflow.model;

/**
 * Enumeration representing categories of fleet vehicles with operational specs.
 */
public enum VehicleType {
    VAN("Delivery Van", 1200.0, 8.0, 75.0, 1.50, 0.12),
    TRUCK("Medium Duty Truck", 5000.0, 24.0, 60.0, 2.80, 0.25),
    SEMI_TRAILER("Heavy Semi-Trailer", 22000.0, 70.0, 50.0, 4.50, 0.40),
    ELECTRIC_CARGO("Electric Urban Hauler", 900.0, 6.0, 65.0, 1.20, 0.05);

    private final String label;
    private final double maxPayloadKg;
    private final double maxVolumeM3;
    private final double averageSpeedKmh;
    private final double operationalCostPerKm;
    private final double carbonPerKmKg;

    VehicleType(String label, double maxPayloadKg, double maxVolumeM3,
                double averageSpeedKmh, double operationalCostPerKm, double carbonPerKmKg) {
        this.label = label;
        this.maxPayloadKg = maxPayloadKg;
        this.maxVolumeM3 = maxVolumeM3;
        this.averageSpeedKmh = averageSpeedKmh;
        this.operationalCostPerKm = operationalCostPerKm;
        this.carbonPerKmKg = carbonPerKmKg;
    }

    public String getLabel() { return label; }
    public double getMaxPayloadKg() { return maxPayloadKg; }
    public double getMaxVolumeM3() { return maxVolumeM3; }
    public double getAverageSpeedKmh() { return averageSpeedKmh; }
    public double getOperationalCostPerKm() { return operationalCostPerKm; }
    public double getCarbonPerKmKg() { return carbonPerKmKg; }
}

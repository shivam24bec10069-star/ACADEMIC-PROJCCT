package com.logiflow.model;

/**
 * Represents a direct transit corridor edge connecting two location nodes.
 */
public class RouteSegment {
    private final String sourceCode;
    private final String destinationCode;
    private final double distanceKm;
    private final double estimatedHours;
    private final double tollCost;
    private final double ecoScore; // Scale 1 (least eco) to 10 (most eco-friendly)

    public RouteSegment(String sourceCode, String destinationCode, double distanceKm,
                        double estimatedHours, double tollCost, double ecoScore) {
        this.sourceCode = sourceCode;
        this.destinationCode = destinationCode;
        this.distanceKm = distanceKm;
        this.estimatedHours = estimatedHours;
        this.tollCost = tollCost;
        this.ecoScore = ecoScore;
    }

    public String getSourceCode() { return sourceCode; }
    public String getDestinationCode() { return destinationCode; }
    public double getDistanceKm() { return distanceKm; }
    public double getEstimatedHours() { return estimatedHours; }
    public double getTollCost() { return tollCost; }
    public double getEcoScore() { return ecoScore; }

    @Override
    public String toString() {
        return String.format("[%s -> %s: %.1f km, %.1f hrs, $%.2f tolls]",
                sourceCode, destinationCode, distanceKm, estimatedHours, tollCost);
    }
}

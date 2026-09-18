package com.logiflow.pattern;

import com.logiflow.model.RouteSegment;

/**
 * Optimizes strictly for monetary cost (fuel operational factor + toll charges).
 */
public class CheapestRoutingStrategy implements RoutingStrategy {
    private static final double FUEL_COST_FACTOR_PER_KM = 0.22;

    @Override
    public double calculateCost(RouteSegment segment) {
        return (segment.getDistanceKm() * FUEL_COST_FACTOR_PER_KM) + segment.getTollCost();
    }

    @Override
    public String getName() {
        return "Lowest Financial Cost (Toll & Fuel Optimized)";
    }

    @Override
    public String getUnit() {
        return "USD";
    }
}

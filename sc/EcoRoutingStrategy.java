package com.logiflow.pattern;

import com.logiflow.model.RouteSegment;

/**
 * Optimizes for environmental sustainability (carbon emission factor / eco score).
 */
public class EcoRoutingStrategy implements RoutingStrategy {
    @Override
    public double calculateCost(RouteSegment segment) {
        // Lower score means better route. Dividing distance by eco-friendly factor (1-10)
        return segment.getDistanceKm() / Math.max(1.0, segment.getEcoScore());
    }

    @Override
    public String getName() {
        return "Green Logistics (Carbon & Eco-Score Optimized)";
    }

    @Override
    public String getUnit() {
        return "eco-index";
    }
}

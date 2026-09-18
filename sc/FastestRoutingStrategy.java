package com.logiflow.pattern;

import com.logiflow.model.RouteSegment;

/**
 * Optimizes strictly for shortest transit time (hours).
 */
public class FastestRoutingStrategy implements RoutingStrategy {
    @Override
    public double calculateCost(RouteSegment segment) {
        return segment.getEstimatedHours();
    }

    @Override
    public String getName() {
        return "Fastest Transit (Time-Optimized)";
    }

    @Override
    public String getUnit() {
        return "hours";
    }
}

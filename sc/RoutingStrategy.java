package com.logiflow.pattern;

import com.logiflow.model.RouteSegment;

/**
 * Behavioral Design Pattern: Strategy Pattern for calculating dynamic routing costs.
 */
public interface RoutingStrategy {
    double calculateCost(RouteSegment segment);
    String getName();
    String getUnit();
}

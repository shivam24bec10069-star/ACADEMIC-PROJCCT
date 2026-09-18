package com.logiflow.model;

import java.util.Objects;

/**
 * Represents a geographical logistics node such as a central hub, distribution center, or warehouse.
 */
public class LocationNode {
    private final String code;
    private final String name;
    private final String region;
    private final double latitude;
    private final double longitude;

    public LocationNode(String code, String name, String region, double latitude, double longitude) {
        this.code = code;
        this.name = name;
        this.region = region;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public String getRegion() { return region; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationNode that = (LocationNode) o;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return String.format("%s (%s, %s)", name, code, region);
    }
}

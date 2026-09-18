package com.logiflow.exception;

public class RouteNotFoundException extends LogisticsException {
    public RouteNotFoundException(String origin, String destination) {
        super("No viable transit route found connecting origin '" + origin + "' to destination '" + destination + "'.");
    }
}

package com.logiflow.storage;

import com.logiflow.exception.InvalidShipmentDataException;
import com.logiflow.model.*;
import com.logiflow.pattern.ShipmentFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Handles persistent storage of shipments, fleet entities, and network graph topology using CSV files.
 */
public class FilePersistenceManager {

    private static final String SHIPMENT_HEADER = "id,type,sender,receiver,origin,destination,weightKg,volumeM3,declaredValue,status,assignedVehicleId,extraParam";
    private static final String ROUTE_HEADER = "sourceCode,destinationCode,distanceKm,estimatedHours,tollCost,ecoScore";

    /**
     * Exports a list of shipments to a CSV file.
     */
    public static void saveShipmentsToCsv(Collection<Shipment> shipments, String filePath) throws IOException {
        File file = new File(filePath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            writer.write(SHIPMENT_HEADER);
            writer.newLine();

            for (Shipment s : shipments) {
                double extraParam = 0.0;
                if (s instanceof ExpressShipment) {
                    extraParam = ((ExpressShipment) s).getSlaHoursGuarantee();
                } else if (s instanceof FragileShipment) {
                    extraParam = ((FragileShipment) s).getMaxGForceAllowed();
                }

                String line = String.format("%s,%s,%s,%s,%s,%s,%.2f,%.3f,%.2f,%s,%s,%.1f",
                        escape(s.getId()),
                        escape(s.getShipmentType()),
                        escape(s.getSender()),
                        escape(s.getReceiver()),
                        escape(s.getOriginCode()),
                        escape(s.getDestinationCode()),
                        s.getWeightKg(),
                        s.getVolumeM3(),
                        s.getDeclaredValue(),
                        s.getStatus().name(),
                        s.getAssignedVehicleId() == null ? "" : escape(s.getAssignedVehicleId()),
                        extraParam);
                writer.write(line);
                writer.newLine();
            }
        }
    }

    /**
     * Imports shipments from a CSV file.
     */
    public static List<Shipment> loadShipmentsFromCsv(String filePath) throws IOException {
        List<Shipment> shipments = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return shipments;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line = reader.readLine(); // Header
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                String[] parts = line.split(",", -1);
                if (parts.length >= 12) {
                    try {
                        String id = parts[0].trim();
                        String type = parts[1].trim();
                        String sender = parts[2].trim();
                        String receiver = parts[3].trim();
                        String origin = parts[4].trim();
                        String dest = parts[5].trim();
                        double weight = Double.parseDouble(parts[6].trim());
                        double volume = Double.parseDouble(parts[7].trim());
                        double value = Double.parseDouble(parts[8].trim());
                        String statusStr = parts[9].trim();
                        String vehicleId = parts[10].trim();
                        double extra = Double.parseDouble(parts[11].trim());

                        Shipment shipment = ShipmentFactory.createShipment(
                                type, id, sender, receiver, origin, dest, weight, volume, value, extra);

                        if (!statusStr.isEmpty()) {
                            shipment.setStatus(ShipmentStatus.valueOf(statusStr));
                        }
                        if (!vehicleId.isEmpty()) {
                            shipment.setAssignedVehicleId(vehicleId);
                        }
                        shipments.add(shipment);
                    } catch (InvalidShipmentDataException | IllegalArgumentException e) {
                        AuditLogger.getInstance().warn("Persistence", "Skipping corrupt shipment row: " + line + " -> " + e.getMessage());
                    }
                }
            }
        }
        return shipments;
    }

    /**
     * Loads route segments from a CSV file.
     */
    public static List<RouteSegment> loadRouteSegmentsFromCsv(String filePath) throws IOException {
        List<RouteSegment> segments = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return segments;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line = reader.readLine(); // Header
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    try {
                        String src = parts[0].trim();
                        String dst = parts[1].trim();
                        double dist = Double.parseDouble(parts[2].trim());
                        double hrs = Double.parseDouble(parts[3].trim());
                        double toll = Double.parseDouble(parts[4].trim());
                        double eco = Double.parseDouble(parts[5].trim());
                        segments.add(new RouteSegment(src, dst, dist, hrs, toll, eco));
                    } catch (NumberFormatException e) {
                        AuditLogger.getInstance().warn("Persistence", "Skipping invalid route row: " + line);
                    }
                }
            }
        }
        return segments;
    }

    private static String escape(String val) {
        if (val == null) return "";
        return val.replace(",", ";");
    }
}

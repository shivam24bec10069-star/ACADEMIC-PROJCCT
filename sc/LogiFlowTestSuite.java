package com.logiflow.test;

import com.logiflow.exception.InsufficientCapacityException;
import com.logiflow.exception.InvalidShipmentDataException;
import com.logiflow.exception.RouteNotFoundException;
import com.logiflow.exception.ShipmentNotFoundException;
import com.logiflow.model.*;
import com.logiflow.pattern.Observer;
import com.logiflow.pattern.ShipmentFactory;
import com.logiflow.pattern.FastestRoutingStrategy;
import com.logiflow.pattern.CheapestRoutingStrategy;
import com.logiflow.pattern.EcoRoutingStrategy;
import com.logiflow.service.*;
import com.logiflow.service.RoutingService.PathResult;
import com.logiflow.storage.FilePersistenceManager;

import java.io.File;
import java.util.*;

/**
 * Self-contained Unit and Validation Test Suite for LogiFlow Pro.
 * Requires zero external test framework dependencies, runnable on any standard JDK.
 */
public class LogiFlowTestSuite {

    private static int testsRun = 0;
    private static int testsPassed = 0;
    private static int testsFailed = 0;

    public static void main(String[] args) {
        System.out.println("================================================================");
        System.out.println("           LOGIFLOW PRO - AUTOMATED UNIT & INTEGRATION SUITE     ");
        System.out.println("================================================================");
        long startTime = System.currentTimeMillis();

        runTest("testShipmentFactoryAndPolymorphism", LogiFlowTestSuite::testShipmentFactoryAndPolymorphism);
        runTest("testPolymorphicTariffCalculations", LogiFlowTestSuite::testPolymorphicTariffCalculations);
        runTest("testVehicleCapacityConstraintEnforcement", LogiFlowTestSuite::testVehicleCapacityConstraintEnforcement);
        runTest("testDijkstraOptimalRoutingPath", LogiFlowTestSuite::testDijkstraOptimalRoutingPath);
        runTest("testAlternativeRoutingStrategies", LogiFlowTestSuite::testAlternativeRoutingStrategies);
        runTest("testPriorityQueueDispatchOrdering", LogiFlowTestSuite::testPriorityQueueDispatchOrdering);
        runTest("testObserverPatternNotifications", LogiFlowTestSuite::testObserverPatternNotifications);
        runTest("testFilePersistenceCsvSerialization", LogiFlowTestSuite::testFilePersistenceCsvSerialization);
        runTest("testStreamAnalyticsCalculations", LogiFlowTestSuite::testStreamAnalyticsCalculations);
        runTest("testCustomExceptionHandling", LogiFlowTestSuite::testCustomExceptionHandling);
        runTest("testMultithreadedTelemetrySimulation", LogiFlowTestSuite::testMultithreadedTelemetrySimulation);

        long elapsed = System.currentTimeMillis() - startTime;
        System.out.println("================================================================");
        System.out.printf(" RESULTS: Total: %d | Passed: %d | Failed: %d (in %d ms)%n",
                testsRun, testsPassed, testsFailed, elapsed);
        if (testsFailed == 0) {
            System.out.println(" STATUS: ALL UNIT AND INTEGRATION TESTS PASSED (100% SUCCESS)");
        } else {
            System.err.println(" STATUS: SOME TESTS FAILED!");
            System.exit(1);
        }
        System.out.println("================================================================");
    }

    private static void runTest(String testName, TestCase test) {
        testsRun++;
        try {
            test.execute();
            testsPassed++;
            System.out.printf("  [PASS] %-45s%n", testName);
        } catch (Throwable t) {
            testsFailed++;
            System.err.printf("  [FAIL] %-45s -> %s%n", testName, t.getMessage());
            t.printStackTrace(System.err);
        }
    }

    @FunctionalInterface
    interface TestCase {
        void execute() throws Exception;
    }

    // --- TEST IMPLEMENTATIONS ---

    private static void testShipmentFactoryAndPolymorphism() throws Exception {
        Shipment standard = ShipmentFactory.createShipment(
                "STANDARD", "TEST-STD", "Sender A", "Recv B", "JFK", "ORD", 50.0, 0.4, 500.0, 0);
        Shipment express = ShipmentFactory.createShipment(
                "EXPRESS", "TEST-EXP", "Sender A", "Recv B", "JFK", "ORD", 20.0, 0.2, 1200.0, 12);
        Shipment fragile = ShipmentFactory.createShipment(
                "FRAGILE", "TEST-FRG", "Sender A", "Recv B", "JFK", "ORD", 15.0, 0.1, 8000.0, 2.0);

        assertTrue(standard instanceof StandardShipment, "Standard instance check");
        assertTrue(express instanceof ExpressShipment, "Express instance check");
        assertTrue(fragile instanceof FragileShipment, "Fragile instance check");
        assertEquals("STANDARD", standard.getShipmentType(), "Standard type label");
        assertEquals("EXPRESS", express.getShipmentType(), "Express type label");
        assertEquals("FRAGILE", fragile.getShipmentType(), "Fragile type label");
    }

    private static void testPolymorphicTariffCalculations() throws Exception {
        Shipment standard = new StandardShipment("S1", "A", "B", "JFK", "ORD", 100.0, 1.0, 500.0);
        // Standard: 15 + (100 * 0.85) + (1000 * 0.05) = 15 + 85 + 50 = 150.00
        double costStd = standard.calculateCost(1000.0);
        assertEquals(150.00, costStd, 0.01, "Standard cost calculation");

        Shipment express = new ExpressShipment("E1", "A", "B", "JFK", "ORD", 50.0, 0.5, 2000.0, 24);
        // Express: 35 + 20 + (50 * 1.60) + (1000 * 0.12) = 55 + 80 + 120 = 255.00
        double costExp = express.calculateCost(1000.0);
        assertEquals(255.00, costExp, 0.01, "Express cost calculation");

        Shipment fragile = new FragileShipment("F1", "A", "B", "JFK", "ORD", 40.0, 0.3, 10000.0, 2.0);
        // Fragile: 25 + (40 * 1.10) + (1000 * 0.08) + (10000 * 0.015) = 25 + 44 + 80 + 150 = 299.00
        double costFrg = fragile.calculateCost(1000.0);
        assertEquals(299.00, costFrg, 0.01, "Fragile cost calculation");
    }

    private static void testVehicleCapacityConstraintEnforcement() throws Exception {
        Vehicle van = new Vehicle("TEST-VAN", VehicleType.VAN, "Tester", "ORD");
        assertEquals(1200.0, van.getType().getMaxPayloadKg(), 0.01, "Van max weight");

        Shipment small = new StandardShipment("S-SM", "A", "B", "ORD", "ATL", 500.0, 2.0, 100.0);
        van.loadShipment(small);
        assertEquals(500.0, van.getCurrentPayloadKg(), 0.01, "Current payload after 1st load");

        Shipment overweight = new StandardShipment("S-BIG", "A", "B", "ORD", "ATL", 800.0, 2.0, 100.0);
        // 500 + 800 = 1300 > 1200, must throw InsufficientCapacityException
        boolean threw = false;
        try {
            van.loadShipment(overweight);
        } catch (InsufficientCapacityException e) {
            threw = true;
        }
        assertTrue(threw, "Must throw InsufficientCapacityException when exceeding payload limit");
    }

    private static void testDijkstraOptimalRoutingPath() throws Exception {
        RoutingService routing = new RoutingService();
        PathResult result = routing.findOptimalPath("JFK", "LAX");

        assertTrue(result.getSegments().size() > 0, "Path should have multiple segments");
        assertTrue(result.getTotalDistanceKm() > 3000.0, "Total distance from NYC to LAX should be realistic (>3000km)");
        assertEquals("JFK", result.getSegments().get(0).getSourceCode(), "Start at JFK");
        assertEquals("LAX", result.getSegments().get(result.getSegments().size() - 1).getDestinationCode(), "End at LAX");
    }

    private static void testAlternativeRoutingStrategies() throws Exception {
        RoutingService routing = new RoutingService();
        PathResult fastest = routing.findOptimalPath("JFK", "DFW", new FastestRoutingStrategy());
        PathResult cheapest = routing.findOptimalPath("JFK", "DFW", new CheapestRoutingStrategy());
        PathResult eco = routing.findOptimalPath("JFK", "DFW", new EcoRoutingStrategy());

        assertTrue(fastest.getTotalHours() > 0, "Fastest time check");
        assertTrue(cheapest.getTotalTolls() >= 0, "Cheapest toll check");
        assertTrue(eco.getTotalDistanceKm() > 0, "Eco distance check");
    }

    private static void testPriorityQueueDispatchOrdering() {
        PriorityQueue<Shipment> pq = new PriorityQueue<>();
        Shipment std = new StandardShipment("S1", "A", "B", "ORD", "DEN", 50.0, 0.5, 100.0); // Priority 10
        Shipment exp = new ExpressShipment("E1", "A", "B", "ORD", "DEN", 50.0, 0.5, 100.0, 12); // Priority 50
        Shipment frg = new FragileShipment("F1", "A", "B", "ORD", "DEN", 50.0, 0.5, 100.0, 2.0); // Priority 30

        pq.add(std);
        pq.add(exp);
        pq.add(frg);

        // Expected polling order: Highest priority first -> Express (50), Fragile (30), Standard (10)
        assertEquals("E1", pq.poll().getId(), "Highest priority should be Express");
        assertEquals("F1", pq.poll().getId(), "Second priority should be Fragile");
        assertEquals("S1", pq.poll().getId(), "Third priority should be Standard");
    }

    private static void testObserverPatternNotifications() throws Exception {
        ShipmentService service = new ShipmentService();
        final List<String> capturedEvents = new ArrayList<>();

        Observer testObserver = new Observer() {
            @Override
            public void onStatusUpdate(Shipment shipment, ShipmentStatus oldStatus, ShipmentStatus newStatus, String message) {
                capturedEvents.add(shipment.getId() + ":" + oldStatus + "->" + newStatus);
            }
            @Override
            public void onTelemetryAlert(String vehicleId, String message, boolean isWarning) {
                capturedEvents.add(vehicleId + ":" + message);
            }
        };

        service.registerObserver(testObserver);
        Shipment s = service.createShipment("STANDARD", "OBS-1", "A", "B", "JFK", "ORD", 20.0, 0.1, 200.0, 0);
        service.updateStatus("OBS-1", ShipmentStatus.IN_TRANSIT, "On road");

        assertTrue(capturedEvents.size() >= 2, "Should capture intake and status transition events");
        assertTrue(capturedEvents.get(1).contains("CREATED->IN_TRANSIT"), "Status transition captured correctly");
    }

    private static void testFilePersistenceCsvSerialization() throws Exception {
        File tempFile = File.createTempFile("logiflow_test_", ".csv");
        tempFile.deleteOnExit();

        List<Shipment> original = Arrays.asList(
                new StandardShipment("CSV-1", "A Corp", "B LLC", "JFK", "ATL", 80.0, 0.8, 1500.0),
                new ExpressShipment("CSV-2", "C Inc", "D Co", "ORD", "LAX", 30.0, 0.3, 5000.0, 16)
        );

        FilePersistenceManager.saveShipmentsToCsv(original, tempFile.getAbsolutePath());
        List<Shipment> loaded = FilePersistenceManager.loadShipmentsFromCsv(tempFile.getAbsolutePath());

        assertEquals(2, loaded.size(), "Loaded shipment count");
        assertEquals("CSV-1", loaded.get(0).getId(), "First loaded ID");
        assertEquals("CSV-2", loaded.get(1).getId(), "Second loaded ID");
        assertEquals(80.0, loaded.get(0).getWeightKg(), 0.01, "Weight preservation");
    }

    private static void testStreamAnalyticsCalculations() throws Exception {
        ShipmentService shipmentService = new ShipmentService();
        FleetService fleetService = new FleetService();
        RoutingService routingService = new RoutingService();

        shipmentService.createShipment("STANDARD", "AN-1", "A", "B", "JFK", "ORD", 100.0, 1.0, 1000.0, 0);
        shipmentService.createShipment("EXPRESS", "AN-2", "A", "B", "ORD", "ATL", 50.0, 0.5, 2000.0, 24);

        AnalyticsService analytics = new AnalyticsService(shipmentService, fleetService, routingService);
        double totalRev = analytics.calculateTotalRevenue();
        assertTrue(totalRev > 0, "Total revenue calculation should be positive");

        DoubleSummaryStatistics weightStats = analytics.getWeightStatistics();
        assertEquals(150.0, weightStats.getSum(), 0.01, "Weight sum");
        assertEquals(75.0, weightStats.getAverage(), 0.01, "Weight average");
    }

    private static void testCustomExceptionHandling() {
        boolean thrownShipment = false;
        ShipmentService service = new ShipmentService();
        try {
            service.getShipment("NON_EXISTENT_ID");
        } catch (ShipmentNotFoundException e) {
            thrownShipment = true;
        }
        assertTrue(thrownShipment, "ShipmentNotFoundException caught");

        boolean thrownRoute = false;
        RoutingService routing = new RoutingService();
        try {
            routing.findOptimalPath("UNKNOWN_SRC", "UNKNOWN_DST");
        } catch (RouteNotFoundException e) {
            thrownRoute = true;
        }
        assertTrue(thrownRoute, "RouteNotFoundException caught");
    }

    private static void testMultithreadedTelemetrySimulation() throws Exception {
        RoutingService routing = new RoutingService();
        FleetService fleet = new FleetService();
        ShipmentService shipments = new ShipmentService();

        Shipment s = shipments.createShipment("STANDARD", "SIM-1", "A", "B", "JFK", "ORD", 50.0, 0.5, 100.0, 0);
        Vehicle v = fleet.getAvailableVehiclesAtHub("JFK").get(0);
        v.loadShipment(s);

        TelemetrySimulator sim = new TelemetrySimulator(routing, fleet, shipments);
        // Fast simulation (20ms step delay)
        sim.simulateActiveTransits(true, 20);
        sim.shutdown();

        Shipment updated = shipments.getShipment("SIM-1");
        assertEquals(ShipmentStatus.DELIVERED, updated.getStatus(), "Shipment should reach DELIVERED status after simulation");
    }

    // --- ASSERTION UTILITIES ---

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError("Assertion Failed: " + message);
        }
    }

    private static void assertEquals(Object expected, Object actual, String message) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError(String.format("Assertion Failed [%s]: expected <%s> but was <%s>",
                    message, expected, actual));
        }
    }

    private static void assertEquals(double expected, double actual, double delta, String message) {
        if (Math.abs(expected - actual) > delta) {
            throw new AssertionError(String.format("Assertion Failed [%s]: expected <%.4f> but was <%.4f> (delta %.4f)",
                    message, expected, actual, delta));
        }
    }
}

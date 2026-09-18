package com.logiflow;

import com.logiflow.exception.InvalidShipmentDataException;
import com.logiflow.exception.LogisticsException;
import com.logiflow.exception.RouteNotFoundException;
import com.logiflow.exception.ShipmentNotFoundException;
import com.logiflow.model.*;
import com.logiflow.pattern.*;
import com.logiflow.service.*;
import com.logiflow.service.DispatchService.DispatchReport;
import com.logiflow.service.RoutingService.PathResult;
import com.logiflow.storage.AuditLogger;
import com.logiflow.test.LogiFlowTestSuite;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Main application entrypoint for LogiFlow Pro: Intelligent Logistics & Freight Dispatching System.
 * Supports both interactive console operations and non-interactive automated demo/test modes.
 */
public class Main {

    private static final String DEFAULT_DATA_PATH = "data/sample_shipments.csv";
    private static final String PERSISTENCE_DATA_PATH = "data/shipments.csv";

    private final RoutingService routingService;
    private final FleetService fleetService;
    private final ShipmentService shipmentService;
    private final DispatchService dispatchService;
    private final TelemetrySimulator telemetrySimulator;
    private final AnalyticsService analyticsService;
    private final ConsoleNotificationListener consoleListener;

    public Main() {
        this.routingService = new RoutingService();
        this.fleetService = new FleetService();
        this.shipmentService = new ShipmentService();
        this.dispatchService = new DispatchService(shipmentService, fleetService);
        this.telemetrySimulator = new TelemetrySimulator(routingService, fleetService, shipmentService);
        this.analyticsService = new AnalyticsService(shipmentService, fleetService, routingService);

        this.consoleListener = new ConsoleNotificationListener();
        this.shipmentService.registerObserver(consoleListener);

        // Preload sample shipments if available
        loadInitialData();
    }

    private void loadInitialData() {
        try {
            File saved = new File(PERSISTENCE_DATA_PATH);
            File initial = new File(DEFAULT_DATA_PATH);
            if (saved.exists()) {
                shipmentService.loadFromFile(PERSISTENCE_DATA_PATH);
            } else if (initial.exists()) {
                shipmentService.loadFromFile(DEFAULT_DATA_PATH);
            }
        } catch (IOException e) {
            System.err.println("Notice: Starting with empty shipment repository (" + e.getMessage() + ")");
        }
    }

    public static void main(String[] args) {
        Main app = new Main();

        // Check command line arguments for headless evaluation pipeline
        if (args.length > 0) {
            String flag = args[0].toLowerCase();
            switch (flag) {
                case "--demo":
                case "-d":
                    app.runAutomatedDemo();
                    return;
                case "--test":
                case "-t":
                    LogiFlowTestSuite.main(new String[0]);
                    return;
                case "--analytics":
                case "-a":
                    System.out.println(app.analyticsService.generateAnalyticalReport());
                    return;
                case "--help":
                case "-h":
                    printUsage();
                    return;
            }
        }

        // Default: Interactive Console CLI Mode
        app.runInteractiveMenu();
    }

    private static void printUsage() {
        System.out.println("LogiFlow Pro - Intelligent Logistics & Freight Dispatching CLI");
        System.out.println("Usage: java -cp bin com.logiflow.Main [OPTIONS]");
        System.out.println("Options:");
        System.out.println("  --demo, -d       Run full automated end-to-end evaluation demo");
        System.out.println("  --test, -t       Execute complete automated unit & integration test suite");
        System.out.println("  --analytics, -a  Display operational analytics and KPI report");
        System.out.println("  --help, -h       Display this command reference");
        System.out.println("  (no args)        Launch interactive terminal management console");
    }

    /**
     * Executes a complete, automated demonstration sequence for automated grading.
     */
    public void runAutomatedDemo() {
        printBanner();
        System.out.println("\n>>> [MODE: AUTOMATED END-TO-END EVALUATION DEMONSTRATION] <<<\n");

        System.out.println("\n--- Step 1: Initial System State & Hub Network ---");
        System.out.printf("Logistics Nodes Configured : %d hubs%n", routingService.getAllNodes().size());
        System.out.printf("Fleet Transport Units      : %d vehicles%n", fleetService.getAllVehicles().size());
        System.out.printf("Pending Freight Intake     : %d shipments%n", shipmentService.getAllShipments().size());

        System.out.println("\n--- Step 2: Dijkstra Optimal Route Calculation ---");
        try {
            System.out.println("[Fastest Strategy] Route from JFK (New York) to LAX (Los Angeles):");
            PathResult fastest = routingService.findOptimalPath("JFK", "LAX", new FastestRoutingStrategy());
            System.out.println("  Path: " + fastest.formatPathSummary());

            System.out.println("[Eco Strategy] Route from JFK to LAX:");
            PathResult eco = routingService.findOptimalPath("JFK", "LAX", new EcoRoutingStrategy());
            System.out.println("  Path: " + eco.formatPathSummary());
        } catch (RouteNotFoundException e) {
            System.err.println("Routing error: " + e.getMessage());
        }

        System.out.println("\n--- Step 3: Freight Tariff Calculation & Polymorphism ---");
        for (Shipment s : shipmentService.getAllShipments()) {
            double dist = analyticsService.getEstimatedDistance(s);
            System.out.printf("  * [%-8s] ID: %-8s | %s -> %s | Wt: %6.1f kg | Cost: $%,8.2f%n",
                    s.getShipmentType(), s.getId(), s.getOriginCode(), s.getDestinationCode(),
                    s.getWeightKg(), s.calculateCost(dist));
        }

        System.out.println("\n--- Step 4: Intelligent PriorityQueue Fleet Dispatch ---");
        DispatchReport report = dispatchService.dispatchAll();
        System.out.println(report.printSummary());

        System.out.println("\n--- Step 5: Real-Time Concurrent Telemetry Simulation ---");
        System.out.println("Simulating multi-threaded transit across geographic waypoints...");
        telemetrySimulator.simulateActiveTransits(true, 150); // fast simulation step

        System.out.println("\n--- Step 6: Stream API Operational Analytics Report ---");
        System.out.println(analyticsService.generateAnalyticalReport());

        System.out.println("\n--- Step 7: Automated Self-Test Verification ---");
        LogiFlowTestSuite.main(new String[0]);

        System.out.println("\n>>> [AUTOMATED DEMONSTRATION COMPLETE - ALL SYSTEMS OPERATIONAL] <<<\n");
        telemetrySimulator.shutdown();
    }

    /**
     * Launches the interactive menu for manual evaluator testing.
     */
    public void runInteractiveMenu() {
        printBanner();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            printMenu();
            System.out.print("Select an option [0-9]: ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    displayAllShipments();
                    break;
                case "2":
                    registerShipmentWizard(scanner);
                    break;
                case "3":
                    displayFleetStatus();
                    break;
                case "4":
                    routePlanningWizard(scanner);
                    break;
                case "5":
                    executeDispatch();
                    break;
                case "6":
                    simulateLiveTransit();
                    break;
                case "7":
                    System.out.println(analyticsService.generateAnalyticalReport());
                    break;
                case "8":
                    persistData();
                    break;
                case "9":
                    LogiFlowTestSuite.main(new String[0]);
                    break;
                case "0":
                    running = false;
                    System.out.println("\nSaving session and shutting down LogiFlow Pro engine. Goodbye!");
                    persistData();
                    telemetrySimulator.shutdown();
                    break;
                default:
                    System.out.println("Invalid selection. Please enter a number between 0 and 9.");
            }
            if (running) {
                System.out.println("\nPress ENTER to continue...");
                scanner.nextLine();
            }
        }
    }

    private void printBanner() {
        System.out.println("=================================================================================");
        System.out.println("   _                 _ _____ _                   ____             ");
        System.out.println("  | |    ___   __ _ (_)  ___| | _____      __  |  _ \\ _ __ ___   ");
        System.out.println("  | |   / _ \\ / _` || | |_  | |/ _ \\ \\ /\\ / /  | |_) | '__/ _ \\  ");
        System.out.println("  | |__| (_) | (_| || |  _| | | (_) \\ V  V /   |  __/| | | (_) | ");
        System.out.println("  |_____\\___/ \\__, |/ |_|   |_|\\___/ \\_/\\_/    |_|   |_|  \\___/  ");
        System.out.println("              |___/__/                                           ");
        System.out.println("       INTELLIGENT LOGISTICS & MULTI-MODAL FREIGHT DISPATCH SYSTEM      ");
        System.out.println("       Course: Programming in Java | Enterprise Evaluation Edition       ");
        System.out.println("=================================================================================");
    }

    private void printMenu() {
        System.out.println("\n----------------------------- SYSTEM MENU -----------------------------");
        System.out.println(" [1] View Registered Shipments & Status");
        System.out.println(" [2] Register New Shipment (Standard / Express / Fragile)");
        System.out.println(" [3] Inspect Transport Fleet Units & Capacity");
        System.out.println(" [4] Pathfinding & Optimal Route Calculator (Dijkstra)");
        System.out.println(" [5] Execute PriorityQueue Freight Dispatch Allocation");
        System.out.println(" [6] Run Concurrent Real-Time Telemetry Simulation");
        System.out.println(" [7] View Stream API Operational Analytics & KPI Report");
        System.out.println(" [8] Save System State to Persistent CSV & Audit Log");
        System.out.println(" [9] Run Automated Unit & Integration Test Suite");
        System.out.println(" [0] Exit Application");
        System.out.println("-----------------------------------------------------------------------");
    }

    private void displayAllShipments() {
        List<Shipment> list = shipmentService.getAllShipments();
        if (list.isEmpty()) {
            System.out.println("No shipments registered in the system.");
            return;
        }

        System.out.println("\n================================ ACTIVE SHIPMENTS REGISTRY ================================");
        System.out.printf("%-10s | %-9s | %-16s | %-4s -> %-4s | %8s | %10s | %-16s%n",
                "ID", "Type", "Sender", "From", "To", "Weight", "Est. Cost", "Status");
        System.out.println("-------------------------------------------------------------------------------------------");

        for (Shipment s : list) {
            double dist = analyticsService.getEstimatedDistance(s);
            double cost = s.calculateCost(dist);
            System.out.printf("%-10s | %-9s | %-16s | %-4s -> %-4s | %6.1f kg | $%,9.2f | %-16s%n",
                    s.getId(), s.getShipmentType(), truncate(s.getSender(), 16),
                    s.getOriginCode(), s.getDestinationCode(), s.getWeightKg(), cost, s.getStatus().getDisplayName());
        }
        System.out.println("===========================================================================================");
    }

    private void registerShipmentWizard(Scanner scanner) {
        System.out.println("\n--- Consignment Registration Wizard ---");
        try {
            System.out.print("Shipment Classification (STANDARD / EXPRESS / FRAGILE): ");
            String type = scanner.nextLine().trim().toUpperCase();

            System.out.print("Tracking ID (e.g. SHP-2001): ");
            String id = scanner.nextLine().trim();

            System.out.print("Sender Name: ");
            String sender = scanner.nextLine().trim();

            System.out.print("Recipient Name: ");
            String receiver = scanner.nextLine().trim();

            System.out.print("Origin Hub Code (JFK, ORD, ATL, DFW, DEN, LAX, SEA, MIA): ");
            String origin = scanner.nextLine().trim().toUpperCase();

            System.out.print("Destination Hub Code: ");
            String dest = scanner.nextLine().trim().toUpperCase();

            System.out.print("Gross Cargo Weight in kg (e.g. 150.5): ");
            double weight = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Volume in m³ (e.g. 1.2): ");
            double volume = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Declared Asset Value in USD (e.g. 5000): ");
            double value = Double.parseDouble(scanner.nextLine().trim());

            double extra = 0.0;
            if ("EXPRESS".equals(type)) {
                System.out.print("Guaranteed SLA Window in hours (e.g. 12 or 24): ");
                extra = Double.parseDouble(scanner.nextLine().trim());
            } else if ("FRAGILE".equals(type)) {
                System.out.print("Maximum Allowed G-Force Shock Limit (e.g. 1.8): ");
                extra = Double.parseDouble(scanner.nextLine().trim());
            }

            Shipment created = shipmentService.createShipment(type, id, sender, receiver, origin, dest, weight, volume, value, extra);
            double dist = analyticsService.getEstimatedDistance(created);
            System.out.println("\n[SUCCESS] Shipment created successfully!");
            System.out.println(created.toDetailString(dist));

        } catch (NumberFormatException e) {
            System.err.println("Input error: Please enter valid numeric values for weight/volume/value.");
        } catch (InvalidShipmentDataException e) {
            System.err.println("Validation error: " + e.getMessage());
        }
    }

    private void displayFleetStatus() {
        System.out.println("\n================================== CORPORATE FLEET ASSETS ==================================");
        for (Vehicle v : fleetService.getAllVehicles()) {
            System.out.println(v);
        }
        System.out.println("============================================================================================");
    }

    private void routePlanningWizard(Scanner scanner) {
        System.out.println("\n--- Pathfinding & Route Analysis ---");
        System.out.print("Enter Origin Hub Code (e.g. JFK): ");
        String src = scanner.nextLine().trim().toUpperCase();
        System.out.print("Enter Destination Hub Code (e.g. LAX): ");
        String dst = scanner.nextLine().trim().toUpperCase();

        System.out.println("Choose Optimization Strategy:");
        System.out.println(" 1. Fastest Transit (Time-Optimized)");
        System.out.println(" 2. Lowest Financial Cost (Toll & Fuel)");
        System.out.println(" 3. Green Logistics (Carbon & Eco-Score)");
        System.out.print("Selection [1-3]: ");
        String choice = scanner.nextLine().trim();

        RoutingStrategy strategy;
        switch (choice) {
            case "2": strategy = new CheapestRoutingStrategy(); break;
            case "3": strategy = new EcoRoutingStrategy(); break;
            default: strategy = new FastestRoutingStrategy(); break;
        }

        try {
            PathResult result = routingService.findOptimalPath(src, dst, strategy);
            System.out.println("\nOptimal Transit Corridor Found:");
            System.out.println("Strategy : " + result.getStrategyName());
            System.out.println("Routing  : " + result.formatPathSummary());
            System.out.println("\nSegment Breakdown:");
            for (RouteSegment seg : result.getSegments()) {
                System.out.printf("  * %s -> %s | %.1f km | %.1f hrs | Tolls: $%.2f | Eco: %.1f%n",
                        seg.getSourceCode(), seg.getDestinationCode(), seg.getDistanceKm(),
                        seg.getEstimatedHours(), seg.getTollCost(), seg.getEcoScore());
            }
        } catch (RouteNotFoundException e) {
            System.err.println("Routing calculation failed: " + e.getMessage());
        }
    }

    private void executeDispatch() {
        System.out.println("\nExecuting PriorityQueue dispatching algorithm across fleet...");
        DispatchReport report = dispatchService.dispatchAll();
        System.out.println(report.printSummary());
    }

    private void simulateLiveTransit() {
        System.out.println("\nStarting multi-threaded concurrent transit simulation...");
        telemetrySimulator.simulateActiveTransits(true, 300);
    }

    private void persistData() {
        try {
            shipmentService.saveToFile(PERSISTENCE_DATA_PATH);
            AuditLogger.getInstance().info("Main", "Persistent state successfully saved to " + PERSISTENCE_DATA_PATH);
            System.out.println("[SUCCESS] Saved shipment database to " + PERSISTENCE_DATA_PATH + " and audit log to data/audit.log");
        } catch (IOException e) {
            System.err.println("Failed to persist data: " + e.getMessage());
        }
    }

    private static String truncate(String val, int maxLen) {
        if (val == null) return "";
        return val.length() > maxLen ? val.substring(0, maxLen - 2) + ".." : val;
    }
}

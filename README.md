# LogiFlow Pro: Intelligent Logistics & Freight Dispatching System

[![Java SE](https://img.shields.io/badge/Java_SE-17%20%7C%2021%20%7C%2025-orange.svg)](https://www.oracle.com/java/)
[![Build Status](https://img.shields.io/badge/Build-Passing-brightgreen.svg)]()
[![Tests](https://img.shields.io/badge/Tests-11%2F11%20Passed%20(100%25)-success.svg)]()
[![Platform](https://img.shields.io/badge/Platform-Windows%20%7C%20Linux%20%7C%20macOS-blue.svg)]()
[![Zero External Dependencies](https://img.shields.io/badge/Dependencies-Standard%20JDK%20Only-blueviolet.svg)]()

> **Course Evaluation Project — Programming in Java**  
> **Academic Session:** 2026 Evaluation Cycle  
> **Execution Mode:** 100% Terminal / CLI Executable (Zero GUI Requirement)

---

## 1. Project Overview

**LogiFlow Pro** is an enterprise-grade, high-performance freight logistics and fleet dispatching engine implemented in pure Java. Designed to model real-world supply chain logistics networks, LogiFlow Pro optimizes freight routing across major distribution hubs, enforces multi-constraint vehicle payload capacities, schedules priority shipments using greedy priority queues, and simulates real-time concurrent transit telemetry across worker thread pools.

The system requires **no third-party frameworks or external package managers** — it builds and executes natively on any standard Java Development Kit (JDK 17, 21, or 25).

---

## 2. Key Architecture & Features

### Core Modules
1. **Fleet & Consignment Management Module:**
   * Polymorphic cargo types (`StandardShipment`, `ExpressShipment`, `FragileShipment`) inheriting from abstract base `Shipment`.
   * Dynamic tariff cost calculations factoring in distance, weight, SLA guarantees, and fragile cargo shock insurance.
   * Multi-vehicle fleet tracking (`VAN`, `TRUCK`, `SEMI_TRAILER`, `ELECTRIC_CARGO`) enforcing payload and volumetric constraints.
2. **Graph-based Routing & Corridor Optimization Module:**
   * Models continental freight hubs (`JFK`, `ORD`, `ATL`, `DFW`, `DEN`, `LAX`, `SEA`, `MIA`) and interstate transit corridors.
   * Implements **Dijkstra's Shortest Path Algorithm** with a pluggable **Strategy Design Pattern**:
     * *Fastest Routing Strategy* (Transit time minimization)
     * *Cheapest Routing Strategy* (Fuel and toll cost minimization)
     * *Eco Routing Strategy* (Green corridor / low-carbon minimization)
3. **Priority Dispatch & Resource Matchmaking Module:**
   * `PriorityQueue`-backed scheduler ensuring urgent medical and express freight are dispatched ahead of standard bulk freight.
   * Greedy capacity-fit matching algorithm that pairs shipments with vehicles based on hub proximity and remaining payload.
4. **Concurrent Telemetry & Real-Time Simulation Engine:**
   * Multi-threaded simulation using Java `ExecutorService` and `CountDownLatch`.
   * Simulates asynchronous fleet vehicle progression across waypoints with randomized traffic congestion events and battery/fuel consumption tracking.
   * Observer Pattern notification listeners alerting dispatchers of milestone achievements in real time.
5. **Operational Analytics & Stream Processing Engine:**
   * Leverages modern Java Stream API for revenue summaries, cargo weight statistics, fleet utilization metrics, and carbon footprint computations.
6. **Persistence & Thread-Safe Audit Logging:**
   * CSV-based state export/import (`FilePersistenceManager`).
   * Thread-safe singleton `AuditLogger` writing operational history to `data/audit.log`.

---

## 3. Design Patterns Applied

| Pattern | Role in LogiFlow Pro | Class / Interface |
| :--- | :--- | :--- |
| **Factory Pattern** | Encapsulates validation and dynamic instantiation of shipment subtypes | `ShipmentFactory` |
| **Strategy Pattern** | Pluggable route optimization criteria (Fastest, Cheapest, Eco) | `RoutingStrategy`, `FastestRoutingStrategy`, `CheapestRoutingStrategy`, `EcoRoutingStrategy` |
| **Observer Pattern** | Decoupled event notification for shipment lifecycle transitions and telemetry | `Observer`, `Subject`, `ConsoleNotificationListener` |
| **Singleton Pattern** | Thread-safe single-instance audit logging with disk persistence | `AuditLogger` |

---

## 4. Technologies & Prerequisites

* **Language:** Java SE (version 17, 21, or 25 LTS recommended; strictly verified on Java 25)
* **Build System:** Standalone `javac` / scripts (`build.bat`, `build.sh`) — zero Maven/Gradle overhead
* **Testing:** Built-in zero-dependency test runner (`LogiFlowTestSuite`)
* **Environment:** Compatible with Windows PowerShell, Windows Command Prompt, Linux Terminal, and macOS Bash/Zsh

---

## 5. Step-by-Step Installation & Execution Guide

Assume the evaluator starts with a clean clone of the repository:

### Step 1: Clone the Repository
```bash
git clone https://github.com/{username}/{repo-name}.git
cd {repo-name}
```

### Step 2: Verify Java Environment
Verify that `javac` and `java` are present in your `PATH`:
```bash
java -version
javac -version
```
*(Any JDK version >= 17 is fully supported).*

---

### Step 3: Build the Project

#### On Windows:
```cmd
.\build.bat
```
*Or using PowerShell:*
```powershell
cmd /c .\build.bat
```

#### On Linux / macOS:
```bash
chmod +x build.sh run.sh test.sh
./build.sh
```

*(Output: `[BUILD SUCCESS] Project compiled into bin/`)*

---

### Step 4: Run the Application

The system supports **both** an automated demonstration mode (ideal for fast, automated evaluation) and a full interactive terminal interface.

#### Option A: Run Full Automated Demonstration (Recommended for Evaluators)
Executes end-to-end pathfinding, dispatching, multi-threaded telemetry simulation, analytics, and self-tests in ~2 seconds:
```bash
# Windows
.\run.bat --demo

# Linux / macOS
./run.sh --demo
```

#### Option B: Launch Interactive Terminal Console
```bash
# Windows
.\run.bat

# Linux / macOS
./run.sh
```
*Direct `java` invocation without scripts:*
```bash
java -cp bin com.logiflow.Main
```

#### CLI Command Flags Summary:
| Command | Action |
| :--- | :--- |
| `java -cp bin com.logiflow.Main --demo` | Executes non-interactive end-to-end evaluation demo |
| `java -cp bin com.logiflow.Main --test` | Executes automated unit & integration test suite |
| `java -cp bin com.logiflow.Main --analytics` | Directly outputs real-time operational analytics report |
| `java -cp bin com.logiflow.Main --help` | Displays command-line reference options |
| `java -cp bin com.logiflow.Main` | Launches interactive management menu |

---

## 6. Running Unit & Integration Tests

The project includes 11 automated test cases verifying polymorphism, capacity validation, graph pathfinding, thread synchronization, and file persistence:

```bash
# Windows
.\test.bat

# Linux / macOS
./test.sh
```
*Direct `java` invocation:*
```bash
java -cp bin com.logiflow.test.LogiFlowTestSuite
```

### Expected Test Output:
```text
================================================================
           LOGIFLOW PRO - AUTOMATED UNIT & INTEGRATION SUITE     
================================================================
  [PASS] testShipmentFactoryAndPolymorphism           
  [PASS] testPolymorphicTariffCalculations            
  [PASS] testVehicleCapacityConstraintEnforcement     
  [PASS] testDijkstraOptimalRoutingPath               
  [PASS] testAlternativeRoutingStrategies             
  [PASS] testPriorityQueueDispatchOrdering            
  [PASS] testObserverPatternNotifications             
  [PASS] testFilePersistenceCsvSerialization          
  [PASS] testStreamAnalyticsCalculations              
  [PASS] testCustomExceptionHandling                  
[Telemetry Engine] Initiating concurrent transit simulation for 1 fleet units...
[Telemetry Engine] All fleet missions completed successfully.
  [PASS] testMultithreadedTelemetrySimulation         
================================================================
 RESULTS: Total: 11 | Passed: 11 | Failed: 0 (in 140 ms)
 STATUS: ALL UNIT AND INTEGRATION TESTS PASSED (100% SUCCESS)
================================================================
```

---

## 7. Sample Terminal Demonstration Transcript

```text
=================================================================================
       INTELLIGENT LOGISTICS & MULTI-MODAL FREIGHT DISPATCH SYSTEM      
       Course: Programming in Java | Enterprise Evaluation Edition       
=================================================================================

>>> [MODE: AUTOMATED END-TO-END EVALUATION DEMONSTRATION] <<<

--- Step 1: Initial System State & Hub Network ---
Logistics Nodes Configured : 8 hubs
Fleet Transport Units      : 10 vehicles
Pending Freight Intake     : 8 shipments

--- Step 2: Dijkstra Optimal Route Calculation ---
[Fastest Strategy] Route from JFK (New York) to LAX (Los Angeles):
  Path: JFK -> ORD -> DEN -> LAX | Distance: 4550.0 km | Est. Time: 48.0 hrs | Tolls: $69.00

--- Step 3: Freight Tariff Calculation & Polymorphism ---
  * [FRAGILE ] ID: SHP-1006 | DEN -> LAX | Wt:   65.0 kg | Cost: $1,608.50
  * [EXPRESS ] ID: SHP-1004 | DFW -> LAX | Wt:  420.0 kg | Cost: $1,003.00
  * [STANDARD] ID: SHP-1003 | ORD -> ATL | Wt: 1850.0 kg | Cost: $1,645.00

--- Step 4: Intelligent PriorityQueue Fleet Dispatch ---
================ DISPATCH EXECUTION REPORT ================
Successfully Assigned: 8  |  Pending/Unassigned: 0
-----------------------------------------------------------
[SUCCESS] Shipment SHP-1001 (EXPRESS  | 120.5 kg) ==> FLT-VN102  (Driver: Elena Rostova)
[SUCCESS] Shipment SHP-1004 (EXPRESS  | 420.0 kg) ==> FLT-SM301  (Driver: Robert Kowalski)
[SUCCESS] Shipment SHP-1007 (EXPRESS  | 310.0 kg) ==> FLT-EV401  (Driver: Liam O'Connor)
[SUCCESS] Shipment SHP-1002 (FRAGILE  |  45.0 kg) ==> FLT-VN101  (Driver: Marcus Vance)
===========================================================

--- Step 5: Real-Time Concurrent Telemetry Simulation ---
Simulating multi-threaded transit across geographic waypoints...
[Telemetry Engine] Initiating concurrent transit simulation for 7 fleet units...
[TELEMETRY ] Fleet Vehicle FLT-VN102: Crossed checkpoint ORD -> DEN (Odometer: +1620.0 km)
[TELEMETRY ] Fleet Vehicle FLT-SM301: Crossed checkpoint DFW -> LAX (Odometer: +2300.0 km)
[EVENT] >>> Shipment SHP-1001: IN_TRANSIT -> DELIVERED | Note: Delivered at destination depot DEN
[Telemetry Engine] All fleet missions completed successfully.

--- Step 6: Stream API Operational Analytics Report ---
 Total Estimated Revenue     : $    9,234.20
 Mean Fleet Utilization      : 0.0% (Post Delivery)
 Carbon Footprint Logged     : 2,574.50 kg CO2
```

---

## 8. Directory Structure

```
LogiFlow-Pro/
├── src/
│   └── com/
│       └── logiflow/
│           ├── exception/
│           │   ├── LogisticsException.java
│           │   ├── ShipmentNotFoundException.java
│           │   ├── RouteNotFoundException.java
│           │   ├── InsufficientCapacityException.java
│           │   └── InvalidShipmentDataException.java
│           ├── model/
│           │   ├── Shipment.java
│           │   ├── StandardShipment.java
│           │   ├── ExpressShipment.java
│           │   ├── FragileShipment.java
│           │   ├── Vehicle.java
│           │   ├── LocationNode.java
│           │   ├── RouteSegment.java
│           │   ├── ShipmentStatus.java
│           │   └── VehicleType.java
│           ├── pattern/
│           │   ├── ShipmentFactory.java
│           │   ├── RoutingStrategy.java
│           │   ├── FastestRoutingStrategy.java
│           │   ├── CheapestRoutingStrategy.java
│           │   ├── EcoRoutingStrategy.java
│           │   ├── Observer.java
│           │   ├── Subject.java
│           │   └── ConsoleNotificationListener.java
│           ├── service/
│           │   ├── RoutingService.java
│           │   ├── FleetService.java
│           │   ├── ShipmentService.java
│           │   ├── DispatchService.java
│           │   ├── TelemetrySimulator.java
│           │   └── AnalyticsService.java
│           ├── storage/
│           │   ├── FilePersistenceManager.java
│           │   └── AuditLogger.java
│           ├── test/
│           │   └── LogiFlowTestSuite.java
│           └── Main.java
├── data/
│   ├── network_routes.csv
│   ├── sample_shipments.csv
│   └── audit.log
├── build.bat / build.sh
├── run.bat / run.sh
├── test.bat / test.sh
├── statement.md
├── README.md
├── PROJECT_REPORT.md
└── report.html
```

---

## 9. Submission Compliance Checklist

* [x] **Repository Visibility:** Public GitHub repository
* [x] **Repository URL Format:** `https://github.com/{username}/{repo-name}` (strict root URL)
* [x] **README.md:** At repository root with step-by-step setup and execution instructions
* [x] **statement.md:** Contains Problem Statement, Scope, Target Users, High-Level Features
* [x] **CLI Executability:** Fully executable from terminal without any GUI setup
* [x] **Course Relevance:** 100% focused on core and advanced Java concepts
* [x] **Project Report:** Complete 15-section report provided as both Markdown and styled printable HTML

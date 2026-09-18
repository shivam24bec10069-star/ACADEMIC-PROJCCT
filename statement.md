# Problem Statement & Project Scope

## Course Information
* **Course:** Programming in Java
* **Project Title:** LogiFlow Pro — Intelligent Logistics & Freight Dispatching System
* **Domain:** Supply Chain Operations, Multi-Modal Freight Routing & Distributed Telemetry Management

---

## 1. Problem Statement
Modern logistics and freight distribution networks face severe operational inefficiencies arising from fragmented consignment intake, sub-optimal route selection, mismatched vehicle payload allocation, and lack of real-time multi-threaded telemetry visibility. Traditional logistics software often relies on monolithic architectures that fail to dynamically prioritize expedited medical or perishable consignments over standard bulk cargo, leading to missed Service Level Agreements (SLAs), inflated fuel/toll expenditures, and excess carbon emissions.

There is a critical demand for an intelligent, modular, and resilient logistics execution engine capable of:
1. Automating polymorphic tariff computations across diverse freight classifications (Standard, Express, Fragile).
2. Dynamically planning optimal inter-hub routes using graph pathfinding algorithms with pluggable optimization criteria (time, cost, sustainability).
3. Utilizing priority-based scheduling to prevent shipment starvation and maximize fleet payload utilization.
4. Concurrently tracking and simulating vehicle movements across geographic waypoints under dynamic real-world operating conditions.

---

## 2. Scope of the Project
The **LogiFlow Pro** system provides an end-to-end command-line logistics management and execution platform developed in modern Java SE. The system models a multi-state freight transport network connecting major intermodal logistics hubs.

### In Scope:
* **Consignment Intake & Polymorphic Tariffs:** Ingesting and modeling multiple freight classifications with unique dimensional, SLA, and fragility handling rules.
* **Network Pathfinding & Corridor Optimization:** Graph-based topology connecting major continental distribution hubs, executing Dijkstra's algorithm with customizable strategy heuristics.
* **Resource Matching & Priority Scheduling:** PriorityQueue-backed dispatch scheduling that allocates shipments to available fleet vehicles based on payload weight, volumetric capacity, and origin location constraints.
* **Concurrent Real-Time Telemetry Simulation:** Multi-threaded simulation of vehicle transits across transit segments, handling simulated checkpoint events, delays, and state transitions.
* **Operational Analytics & Stream Processing:** Java Stream API aggregations for revenue analysis, cargo weight statistics, fleet utilization percentages, and carbon footprint tracking.
* **Data Persistence & Audit Logging:** Persistent state storage via structured CSV files and append-only thread-safe transaction audit logs.

### Out of Scope:
* Physical hardware GPS satellite transceiver integration.
* Commercial bank payment gateway integration (mock tariff billing utilized).

---

## 3. Target Users
1. **Logistics Dispatch Managers:** Supervise daily fleet availability, trigger automated dispatch runs, and monitor hub capacity.
2. **Freight Coordinators & Intake Clerks:** Ingest customer cargo, specify handling requirements, and obtain transparent polymorphic cost quotations.
3. **Operations Analysts:** Analyze network throughput, vehicle payload efficiency, revenue breakdowns, and carbon emissions.
4. **Autonomous Automated Systems & Regulators:** Monitor tamper-evident audit logs and trace consignment chain-of-custody milestones.

---

## 4. High-Level Features
* **Polymorphic Cargo Modeling:** Clean Object-Oriented inheritance hierarchy (`StandardShipment`, `ExpressShipment`, `FragileShipment`).
* **Dijkstra Multi-Criteria Routing:** Pluggable Strategy pattern supporting *Fastest Transit*, *Lowest Cost*, and *Eco-Optimized Green Logistics*.
* **Intelligent Capacity-Constrained Dispatch:** PriorityQueue scheduler with automated fleet matching and payload enforcement.
* **Multi-Threaded Telemetry Simulator:** Asynchronous transit simulation utilizing Java's `ExecutorService`, `CountDownLatch`, and thread-safe collections.
* **Observer Pattern Alerting:** Decoupled real-time notification engine alerting dispatchers of milestone checkpoints and traffic delays.
* **Functional Stream Analytics:** Real-time KPI dashboard powered by Java Stream API collectors, summaries, and grouping operations.
* **Robust Exception Architecture:** Granular custom exception handling preventing invalid operations or capacity overloads.
* **Zero-Dependency CLI & Automated Verification:** Fully functional via terminal commands with a built-in automated test suite.

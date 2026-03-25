# BookMyStay

BookMyStay is a Java console-based hotel booking system built incrementally across 8 use cases (UC1 to UC8).
Each use case demonstrates one stage of the domain and service design, from app startup to booking reports.

## Tech Stack

- Java (console application)
- Object-Oriented Programming (abstraction, inheritance, encapsulation, polymorphism)
- Java Collections (Map, List, Queue, Set)

## Project Structure

- UC1.java: App banner and startup flow
- UC2.java: Room hierarchy and static availability display
- UC3.java: Centralized room inventory and booking simulation
- UC4.java: Guest room search with availability filtering
- UC5.java: Reservation requests queued using FIFO
- UC6.java: Safe booking processing with unique room IDs and inventory reduction
- UC7.java: Add-on services management per reservation
- UC8.java: Booking history and reporting

## Important Note

These files are intentionally standalone use-case demos.
Several class names are reused across files (for example, Room and Reservation), so compile and run one use case at a time.

## Prerequisites

- JDK 8 or newer installed
- Java added to PATH

Check installation:

```bash
java -version
javac -version
```

## How To Run

Run commands from the project root.

### Windows (Command Prompt)

```cmd
javac UC1.java
java UC1
```

Replace UC1 with any use case class (UC2 to UC8).

### macOS/Linux

```bash
javac UC1.java
java UC1
```

Replace UC1 with any use case class (UC2 to UC8).

## Use Case Highlights

1. UC1 initializes the BookMyStay console application.
2. UC2 defines room categories and prints room details with availability.
3. UC3 introduces RoomInventory with controlled booking updates.
4. UC4 adds RoomSearchService for read-only available-room listing.
5. UC5 adds BookingQueue to handle requests in FIFO order.
6. UC6 processes queued reservations atomically and prevents double booking.
7. UC7 supports per-reservation add-on services and total add-on cost.
8. UC8 stores confirmed reservations and generates a booking report.

## Learning Outcomes

- Modeling domains with abstract and concrete classes
- Separating concerns between domain objects and services
- Managing state safely with inventory and queue patterns
- Building step-by-step use-case oriented Java programs

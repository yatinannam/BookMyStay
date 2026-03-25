import java.util.*;

// -------------------- DOMAIN --------------------

abstract class Room {
    private int beds;
    private double size;
    private double price;

    public Room(int beds, double size, double price) {
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    public abstract String getRoomType();
}

// Concrete Rooms
class SingleRoom extends Room {
    public SingleRoom() {
        super(1, 200, 1500);
    }

    public String getRoomType() {
        return "Single";
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super(2, 350, 2500);
    }

    public String getRoomType() {
        return "Double";
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super(3, 600, 5000);
    }

    public String getRoomType() {
        return "Suite";
    }
}

// -------------------- INVENTORY SERVICE --------------------

class RoomInventory {
    private HashMap<String, Integer> availability = new HashMap<>();

    public RoomInventory() {
        availability.put("Single", 2);
        availability.put("Double", 1);
        availability.put("Suite", 1);
    }

    public int getAvailability(String type) {
        return availability.getOrDefault(type, 0);
    }

    // 🔥 Immediate update after allocation
    public void reduceAvailability(String type) {
        int current = getAvailability(type);
        if (current > 0) {
            availability.put(type, current - 1);
        }
    }

    public void displayInventory() {
        System.out.println("\n=== Inventory ===");
        for (Map.Entry<String, Integer> e : availability.entrySet()) {
            System.out.println(e.getKey() + ": " + e.getValue());
        }
    }
}

// -------------------- RESERVATION --------------------

class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// -------------------- BOOKING QUEUE --------------------

class BookingQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll(); // FIFO
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// -------------------- BOOKING SERVICE (CORE UC6) --------------------

class BookingService {

    private RoomInventory inventory;

    // Track ALL allocated room IDs (global uniqueness)
    private Set<String> allocatedRoomIds = new HashSet<>();

    // Track roomType -> assigned room IDs
    private HashMap<String, Set<String>> roomAllocations = new HashMap<>();

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    // Generate unique room ID
    private String generateRoomId(String roomType) {
        String id;
        do {
            id = roomType.substring(0, 1).toUpperCase() + (int) (Math.random() * 1000);
        } while (allocatedRoomIds.contains(id)); // ensure uniqueness

        return id;
    }

    // 🔥 Process booking request (ATOMIC LOGIC)
    public void processReservation(Reservation r) {

        String type = r.getRoomType();

        System.out.println("\nProcessing request for " + r.getGuestName());

        // Check availability
        if (inventory.getAvailability(type) <= 0) {
            System.out.println("Booking FAILED for " + r.getGuestName() + " (No rooms available)");
            return;
        }

        // Generate unique ID
        String roomId = generateRoomId(type);

        // Store globally (Set prevents duplicates)
        allocatedRoomIds.add(roomId);

        // Store per room type
        roomAllocations.putIfAbsent(type, new HashSet<>());
        roomAllocations.get(type).add(roomId);

        // 🔥 Inventory update (IMMEDIATE)
        inventory.reduceAvailability(type);

        // Confirm booking
        System.out.println("Booking CONFIRMED for " + r.getGuestName());
        System.out.println("Assigned Room ID: " + roomId);
    }

    // Display allocations
    public void displayAllocations() {
        System.out.println("\n=== Allocated Rooms ===");

        for (Map.Entry<String, Set<String>> entry : roomAllocations.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}

// -------------------- MAIN APP --------------------

public class UC6 {

    public static void main(String[] args) {

        // Setup
        RoomInventory inventory = new RoomInventory();
        BookingQueue queue = new BookingQueue();
        BookingService bookingService = new BookingService(inventory);

        // Add requests (FIFO)
        queue.addRequest(new Reservation("Shagun", "Single"));
        queue.addRequest(new Reservation("Avani", "Single"));
        queue.addRequest(new Reservation("Shreya", "Single")); // should fail (only 2 available)
        queue.addRequest(new Reservation("Sahithi", "Suite"));

        // Process queue
        while (!queue.isEmpty()) {
            Reservation r = queue.getNextRequest();
            bookingService.processReservation(r);
        }

        // Final state
        bookingService.displayAllocations();
        inventory.displayInventory();

        System.out.println("\n(All bookings processed safely — no double booking)");
    }
}
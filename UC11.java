import java.util.*;

/**
 * ============================================================
 * CLASS - Reservation
 * ============================================================
 */
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

/**
 * ============================================================
 * CLASS - BookingRequestQueue
 * ============================================================
 */
class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.add(r);
    }

    public Reservation getNextRequest() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

/**
 * ============================================================
 * CLASS - RoomInventory
 * ============================================================
 */
class RoomInventory {
    private Map<String, Integer> rooms;

    public RoomInventory() {
        rooms = new HashMap<>();
        rooms.put("Single", 5);
        rooms.put("Double", 3);
        rooms.put("Suite", 2);
    }

    public boolean isAvailable(String roomType) {
        return rooms.get(roomType) > 0;
    }

    public String allocateRoom(String roomType) {
        int count = rooms.get(roomType);
        rooms.put(roomType, count - 1);
        return roomType + "-" + count;
    }

    public Map<String, Integer> getInventory() {
        return rooms;
    }
}

/**
 * ============================================================
 * CLASS - RoomAllocationService
 * ============================================================
 */
class RoomAllocationService {

    public void allocateRoom(Reservation r, RoomInventory inventory) {

        String roomType = r.getRoomType();

        if (inventory.isAvailable(roomType)) {
            String roomId = inventory.allocateRoom(roomType);

            System.out.println("Booking confirmed for Guest: "
                    + r.getGuestName() + ", Room ID: " + roomId);
        } else {
            System.out.println("Booking failed for Guest: "
                    + r.getGuestName() + " (No rooms available)");
        }
    }
}

/**
 * ============================================================
 * CLASS - ConcurrentBookingProcessor (THREAD)
 * ============================================================
 */
class ConcurrentBookingProcessor implements Runnable {

    private BookingRequestQueue bookingQueue;
    private RoomInventory inventory;
    private RoomAllocationService allocationService;

    public ConcurrentBookingProcessor(
            BookingRequestQueue bookingQueue,
            RoomInventory inventory,
            RoomAllocationService allocationService) {
        this.bookingQueue = bookingQueue;
        this.inventory = inventory;
        this.allocationService = allocationService;
    }

    @Override
    public void run() {

        while (true) {
            Reservation reservation;

            // 🔒 Critical Section 1: Queue access
            synchronized (bookingQueue) {
                if (bookingQueue.isEmpty()) {
                    break;
                }
                reservation = bookingQueue.getNextRequest();
            }

            // 🔒 Critical Section 2: Inventory access
            synchronized (inventory) {
                allocationService.allocateRoom(reservation, inventory);
            }
        }
    }
}

/**
 * ============================================================
 * MAIN CLASS - UseCase11ConcurrentBookingSimulation
 * ============================================================
 */
public class UC11 {

    public static void main(String[] args) {

        System.out.println("Concurrent Booking Simulation");

        // Shared resources
        BookingRequestQueue bookingQueue = new BookingRequestQueue();
        RoomInventory inventory = new RoomInventory();
        RoomAllocationService allocationService = new RoomAllocationService();

        // Add booking requests
        bookingQueue.addRequest(new Reservation("Abhi", "Single"));
        bookingQueue.addRequest(new Reservation("Vanmathi", "Double"));
        bookingQueue.addRequest(new Reservation("Kural", "Suite"));
        bookingQueue.addRequest(new Reservation("Subha", "Single"));

        // Create threads
        Thread t1 = new Thread(
                new ConcurrentBookingProcessor(bookingQueue, inventory, allocationService));

        Thread t2 = new Thread(
                new ConcurrentBookingProcessor(bookingQueue, inventory, allocationService));

        // Start threads
        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            System.out.println("Thread execution interrupted.");
        }

        // Final inventory
        System.out.println("\nRemaining Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.getInventory().entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
import java.util.*;

/**
 * ============================================================
 * CLASS - InvalidBookingException
 * ============================================================
 */
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

/**
 * ============================================================
 * CLASS - RoomInventory
 * ============================================================
 * Maintains available rooms
 */
class RoomInventory {
    private Map<String, Integer> rooms;

    public RoomInventory() {
        rooms = new HashMap<>();
        rooms.put("Single", 2);
        rooms.put("Double", 2);
        rooms.put("Suite", 1);
    }

    public boolean isValidRoomType(String roomType) {
        return rooms.containsKey(roomType);
    }

    public boolean isAvailable(String roomType) {
        return rooms.get(roomType) > 0;
    }
}

/**
 * ============================================================
 * CLASS - BookingRequestQueue
 * ============================================================
 * Simulates request handling
 */
class BookingRequestQueue {
    private Queue<String> queue = new LinkedList<>();

    public void addRequest(String request) {
        queue.add(request);
    }
}

/**
 * ============================================================
 * CLASS - ReservationValidator
 * ============================================================
 */
class ReservationValidator {

    public void validate(
            String guestName,
            String roomType,
            RoomInventory inventory
    ) throws InvalidBookingException {

        // Validate guest name
        if (guestName == null || guestName.trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        // Validate room type (CASE SENSITIVE as per note)
        if (!inventory.isValidRoomType(roomType)) {
            throw new InvalidBookingException("Invalid room type selected.");
        }

        // Check availability
        if (!inventory.isAvailable(roomType)) {
            throw new InvalidBookingException("Room not available.");
        }
    }
}

/**
 * ============================================================
 * MAIN CLASS - UseCase9ErrorHandlingValidation
 * ============================================================
 */
public class UC9 {

    public static void main(String[] args) {

        System.out.println("Booking Validation");

        Scanner scanner = new Scanner(System.in);

        // Initialize components
        RoomInventory inventory = new RoomInventory();
        ReservationValidator validator = new ReservationValidator();
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        try {
            // Input
            System.out.print("Enter guest name: ");
            String guestName = scanner.nextLine();

            System.out.print("Enter room type (Single/Double/Suite): ");
            String roomType = scanner.nextLine();

            // Validate input
            validator.validate(guestName, roomType, inventory);

            // If valid → add request
            bookingQueue.addRequest(guestName + " - " + roomType);

            System.out.println("Booking request accepted.");

        } catch (InvalidBookingException e) {
            // Graceful failure
            System.out.println("Booking failed: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
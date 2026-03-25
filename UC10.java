import java.util.*;

/**
 * ============================================================
 * CLASS - RoomInventory
 * ============================================================
 * Maintains room availability
 */
class RoomInventory {
    private Map<String, Integer> rooms;

    public RoomInventory() {
        rooms = new HashMap<>();
        rooms.put("Single", 5);
        rooms.put("Double", 3);
        rooms.put("Suite", 2);
    }

    public void increaseRoom(String roomType) {
        rooms.put(roomType, rooms.get(roomType) + 1);
    }

    public int getAvailableRooms(String roomType) {
        return rooms.get(roomType);
    }
}

/**
 * ============================================================
 * CLASS - CancellationService
 * ============================================================
 */
class CancellationService {

    // Stack for rollback tracking (LIFO)
    private Stack<String> releasedRoomIds;

    // Map reservationId -> roomType
    private Map<String, String> reservationRoomTypeMap;

    // Constructor
    public CancellationService() {
        releasedRoomIds = new Stack<>();
        reservationRoomTypeMap = new HashMap<>();
    }

    /**
     * Register confirmed booking
     */
    public void registerBooking(String reservationId, String roomType) {
        reservationRoomTypeMap.put(reservationId, roomType);
    }

    /**
     * Cancel booking + rollback
     */
    public void cancelBooking(String reservationId, RoomInventory inventory) {

        // Validate existence
        if (!reservationRoomTypeMap.containsKey(reservationId)) {
            System.out.println("Invalid cancellation. Reservation not found.");
            return;
        }

        // Get room type
        String roomType = reservationRoomTypeMap.get(reservationId);

        // Push into rollback stack
        releasedRoomIds.push(reservationId);

        // Restore inventory
        inventory.increaseRoom(roomType);

        // Remove booking
        reservationRoomTypeMap.remove(reservationId);

        System.out.println("Booking cancelled successfully. Inventory restored for room type: " + roomType);
    }

    /**
     * Show rollback history
     */
    public void showRollbackHistory() {
        System.out.println("\nRollback History (Most Recent First):");

        for (int i = releasedRoomIds.size() - 1; i >= 0; i--) {
            System.out.println("Released Reservation ID: " + releasedRoomIds.get(i));
        }
    }
}

/**
 * ============================================================
 * MAIN CLASS - UseCase10BookingCancellation
 * ============================================================
 */
public class UC10 {

    public static void main(String[] args) {

        System.out.println("Booking Cancellation");

        // Initialize
        RoomInventory inventory = new RoomInventory();
        CancellationService cancellationService = new CancellationService();

        // Simulate confirmed booking
        String reservationId = "Single-1";
        cancellationService.registerBooking(reservationId, "Single");

        // Cancel booking
        cancellationService.cancelBooking(reservationId, inventory);

        // Show rollback history
        cancellationService.showRollbackHistory();

        // Show updated inventory
        System.out.println("\nUpdated Single Room Availability: " +
                inventory.getAvailableRooms("Single"));
    }
}
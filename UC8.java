import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================
 * CLASS - Reservation
 * ============================================================
 * Represents a confirmed booking.
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
 * CLASS - BookingHistory
 * ============================================================
 * Stores confirmed reservations in order.
 */
class BookingHistory {

    // List to store reservations
    private List<Reservation> confirmedReservations;

    // Constructor
    public BookingHistory() {
        confirmedReservations = new ArrayList<>();
    }

    // Add reservation
    public void addReservation(Reservation reservation) {
        confirmedReservations.add(reservation);
    }

    // Get all reservations
    public List<Reservation> getConfirmedReservations() {
        return confirmedReservations;
    }
}

/**
 * ============================================================
 * CLASS - BookingReportService
 * ============================================================
 * Generates reports from booking history.
 */
class BookingReportService {

    // Generate report
    public void generateReport(BookingHistory history) {
        System.out.println("Booking History Report");

        for (Reservation r : history.getConfirmedReservations()) {
            System.out.println("Guest: " + r.getGuestName() +
                    ", Room Type: " + r.getRoomType());
        }
    }
}

/**
 * ============================================================
 * MAIN CLASS - UseCase8BookingHistoryReport
 * ============================================================
 */
public class UC8 {

    public static void main(String[] args) {

        System.out.println("Booking History and Reporting\n");

        // Create booking history
        BookingHistory history = new BookingHistory();

        // Add confirmed reservations
        history.addReservation(new Reservation("Abhi", "Single"));
        history.addReservation(new Reservation("Subha", "Double"));
        history.addReservation(new Reservation("Vanmathi", "Suite"));

        // Generate report
        BookingReportService reportService = new BookingReportService();
        reportService.generateReport(history);
    }
}
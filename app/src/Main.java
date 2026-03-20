import java.util.ArrayList;
import java.util.List;

public class Main {

    static class Reservation {
        private String reservationId;
        private String guestName;
        private String roomType;
        private String roomId;
        private int numberOfNights;
        private double pricePerNight;

        public Reservation(String reservationId, String guestName, String roomType,
                           String roomId, int numberOfNights, double pricePerNight) {
            this.reservationId  = reservationId;
            this.guestName      = guestName;
            this.roomType       = roomType;
            this.roomId         = roomId;
            this.numberOfNights = numberOfNights;
            this.pricePerNight  = pricePerNight;
        }

        public String getReservationId() { return reservationId; }
        public String getGuestName()     { return guestName; }
        public String getRoomType()      { return roomType; }
        public String getRoomId()        { return roomId; }
        public int getNumberOfNights()   { return numberOfNights; }
        public double getPricePerNight() { return pricePerNight; }
        public double getTotalCost()     { return numberOfNights * pricePerNight; }

        public void displayDetails() {
            System.out.println("Reservation ID : " + reservationId);
            System.out.println("Guest          : " + guestName);
            System.out.println("Room Type      : " + roomType);
            System.out.println("Room ID        : " + roomId);
            System.out.println("Nights         : " + numberOfNights);
            System.out.println("Price/Night    : $" + pricePerNight);
            System.out.println("Total Cost     : $" + getTotalCost());
        }
    }

    static class BookingHistory {
        private List<Reservation> history;

        public BookingHistory() {
            history = new ArrayList<>();
        }

        public void addBooking(Reservation reservation) {
            history.add(reservation);
            System.out.println("Booking recorded for: " + reservation.getGuestName()
                    + " [" + reservation.getReservationId() + "]");
        }

        public List<Reservation> getHistory() {
            return history;
        }

        public int getTotalBookings() {
            return history.size();
        }
    }

    static class BookingReportService {
        private BookingHistory bookingHistory;

        public BookingReportService(BookingHistory bookingHistory) {
            this.bookingHistory = bookingHistory;
        }

        public void displayFullHistory() {
            System.out.println("\n============================================");
            System.out.println("           Full Booking History             ");
            System.out.println("============================================");

            List<Reservation> history = bookingHistory.getHistory();

            if (history.isEmpty()) {
                System.out.println("  No bookings found.");
            } else {
                for (Reservation r : history) {
                    System.out.println();
                    r.displayDetails();
                    System.out.println("--------------------------------------------");
                }
            }

            System.out.println("Total Bookings : " + bookingHistory.getTotalBookings());
            System.out.println("============================================");
        }

        public void generateSummaryReport() {
            System.out.println("\n============================================");
            System.out.println("            Booking Summary Report          ");
            System.out.println("============================================");

            List<Reservation> history = bookingHistory.getHistory();

            if (history.isEmpty()) {
                System.out.println("  No data available for report.");
                System.out.println("============================================");
                return;
            }

            double totalRevenue = 0;

            for (Reservation r : history) {
                totalRevenue += r.getTotalCost();
            }

            System.out.println("Total Bookings  : " + history.size());
            System.out.println("Total Revenue   : $" + totalRevenue);
            System.out.println("============================================");
        }

        public void generateRoomTypeReport() {
            System.out.println("\n============================================");
            System.out.println("          Bookings by Room Type             ");
            System.out.println("============================================");

            List<Reservation> history = bookingHistory.getHistory();

            java.util.HashMap<String, Integer> roomTypeCount = new java.util.HashMap<>();

            for (Reservation r : history) {
                roomTypeCount.put(r.getRoomType(),
                        roomTypeCount.getOrDefault(r.getRoomType(), 0) + 1);
            }

            for (String roomType : roomTypeCount.keySet()) {
                System.out.println(roomType + " : " + roomTypeCount.get(roomType) + " booking(s)");
            }

            System.out.println("============================================");
        }
    }

    public static void main(String[] args) {

        BookingHistory bookingHistory       = new BookingHistory();
        BookingReportService reportService  = new BookingReportService(bookingHistory);

        System.out.println("============================================");
        System.out.println("       Booking History & Reporting          ");
        System.out.println("============================================");

        System.out.println("\nRecording Confirmed Bookings:");
        System.out.println("--------------------------------------------");

        bookingHistory.addBooking(new Reservation("RES-001", "Alice",   "Single Room", "Single-Room-001", 3, 80.00));
        bookingHistory.addBooking(new Reservation("RES-002", "Bob",     "Suite Room",  "Suite-Room-001",  2, 250.00));
        bookingHistory.addBooking(new Reservation("RES-003", "Charlie", "Single Room", "Single-Room-002", 1, 80.00));
        bookingHistory.addBooking(new Reservation("RES-004", "Diana",   "Suite Room",  "Suite-Room-002",  4, 250.00));
        bookingHistory.addBooking(new Reservation("RES-005", "Eve",     "Double Room", "Double-Room-001", 2, 120.00));

        reportService.displayFullHistory();
        reportService.generateSummaryReport();
        reportService.generateRoomTypeReport();
    }
}
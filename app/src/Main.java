import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Main {

    static class Reservation {
        private String reservationId;
        private String guestName;
        private String roomType;
        private String roomId;
        private int numberOfNights;
        private boolean cancelled;

        public Reservation(String reservationId, String guestName, String roomType,
                           String roomId, int numberOfNights) {
            this.reservationId  = reservationId;
            this.guestName      = guestName;
            this.roomType       = roomType;
            this.roomId         = roomId;
            this.numberOfNights = numberOfNights;
            this.cancelled      = false;
        }

        public String getReservationId() { return reservationId; }
        public String getGuestName()     { return guestName; }
        public String getRoomType()      { return roomType; }
        public String getRoomId()        { return roomId; }
        public int getNumberOfNights()   { return numberOfNights; }
        public boolean isCancelled()     { return cancelled; }
        public void setCancelled()       { this.cancelled = true; }

        public void displayDetails() {
            System.out.println("Reservation ID : " + reservationId);
            System.out.println("Guest          : " + guestName);
            System.out.println("Room Type      : " + roomType);
            System.out.println("Room ID        : " + roomId);
            System.out.println("Nights         : " + numberOfNights);
            System.out.println("Status         : " + (cancelled ? "CANCELLED" : "CONFIRMED"));
        }
    }

    static class RoomInventory {
        private HashMap<String, Integer> inventory;

        public RoomInventory() {
            inventory = new HashMap<>();
            inventory.put("Single Room", 2);
            inventory.put("Double Room", 1);
            inventory.put("Suite Room",  1);
        }

        public int getAvailability(String roomType) {
            return inventory.getOrDefault(roomType, 0);
        }

        public void decrementAvailability(String roomType) {
            if (inventory.containsKey(roomType)) {
                inventory.put(roomType, inventory.get(roomType) - 1);
            }
        }

        public void incrementAvailability(String roomType) {
            if (inventory.containsKey(roomType)) {
                inventory.put(roomType, inventory.get(roomType) + 1);
            }
        }

        public void displayInventory() {
            System.out.println("\n============================================");
            System.out.println("         Current Room Inventory             ");
            System.out.println("============================================");
            for (String roomType : inventory.keySet()) {
                System.out.println(roomType + " : " + inventory.get(roomType) + " available");
            }
            System.out.println("============================================");
        }
    }

    static class BookingHistory {
        private List<Reservation> history;

        public BookingHistory() {
            history = new ArrayList<>();
        }

        public void addBooking(Reservation reservation) {
            history.add(reservation);
        }

        public Reservation findById(String reservationId) {
            for (Reservation r : history) {
                if (r.getReservationId().equals(reservationId)) {
                    return r;
                }
            }
            return null;
        }

        public void displayHistory() {
            System.out.println("\n============================================");
            System.out.println("           Booking History                  ");
            System.out.println("============================================");
            if (history.isEmpty()) {
                System.out.println("  No bookings found.");
            } else {
                for (Reservation r : history) {
                    System.out.println();
                    r.displayDetails();
                    System.out.println("--------------------------------------------");
                }
            }
            System.out.println("============================================");
        }
    }

    static class CancellationService {
        private RoomInventory inventory;
        private BookingHistory bookingHistory;
        private Stack<String> rollbackStack;

        public CancellationService(RoomInventory inventory, BookingHistory bookingHistory) {
            this.inventory      = inventory;
            this.bookingHistory = bookingHistory;
            this.rollbackStack  = new Stack<>();
        }

        public void cancelBooking(String reservationId) {
            System.out.println("\n--------------------------------------------");
            System.out.println("Cancellation Request: " + reservationId);
            System.out.println("--------------------------------------------");

            Reservation reservation = bookingHistory.findById(reservationId);

            if (reservation == null) {
                System.out.println("ERROR: Reservation " + reservationId + " not found.");
                return;
            }

            if (reservation.isCancelled()) {
                System.out.println("ERROR: Reservation " + reservationId + " is already cancelled.");
                return;
            }

            rollbackStack.push(reservation.getRoomId());
            reservation.setCancelled();
            inventory.incrementAvailability(reservation.getRoomType());

            System.out.println("Cancellation SUCCESSFUL");
            System.out.println("Guest          : " + reservation.getGuestName());
            System.out.println("Room Type      : " + reservation.getRoomType());
            System.out.println("Room ID        : " + reservation.getRoomId());
            System.out.println("Rolled Back ID : " + rollbackStack.peek());
        }

        public void displayRollbackStack() {
            System.out.println("\n============================================");
            System.out.println("         Rollback Stack (LIFO)              ");
            System.out.println("============================================");
            if (rollbackStack.isEmpty()) {
                System.out.println("  No rollbacks recorded.");
            } else {
                for (String roomId : rollbackStack) {
                    System.out.println("Released Room ID : " + roomId);
                }
            }
            System.out.println("============================================");
        }
    }

    public static void main(String[] args) {

        RoomInventory inventory         = new RoomInventory();
        BookingHistory bookingHistory   = new BookingHistory();
        CancellationService cancService = new CancellationService(inventory, bookingHistory);

        System.out.println("============================================");
        System.out.println("   Booking Cancellation & Inventory Rollback");
        System.out.println("============================================");

        Reservation r1 = new Reservation("RES-001", "Alice",   "Single Room", "Single-Room-001", 3);
        Reservation r2 = new Reservation("RES-002", "Bob",     "Suite Room",  "Suite-Room-001",  2);
        Reservation r3 = new Reservation("RES-003", "Charlie", "Double Room", "Double-Room-001", 1);
        Reservation r4 = new Reservation("RES-004", "Diana",   "Single Room", "Single-Room-002", 4);

        bookingHistory.addBooking(r1);
        bookingHistory.addBooking(r2);
        bookingHistory.addBooking(r3);
        bookingHistory.addBooking(r4);

        inventory.decrementAvailability("Single Room");
        inventory.decrementAvailability("Single Room");
        inventory.decrementAvailability("Suite Room");
        inventory.decrementAvailability("Double Room");

        inventory.displayInventory();
        bookingHistory.displayHistory();

        // Valid cancellations
        cancService.cancelBooking("RES-002");
        cancService.cancelBooking("RES-001");

        // Already cancelled
        cancService.cancelBooking("RES-002");

        // Non-existent
        cancService.cancelBooking("RES-999");

        inventory.displayInventory();
        bookingHistory.displayHistory();
        cancService.displayRollbackStack();
    }
}
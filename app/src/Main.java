import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

public class Main {

    static class Reservation {
        private String reservationId;
        private String guestName;
        private String roomType;
        private String roomId;
        private int numberOfNights;

        public Reservation(String reservationId, String guestName, String roomType,
                           String roomId, int numberOfNights) {
            this.reservationId  = reservationId;
            this.guestName      = guestName;
            this.roomType       = roomType;
            this.roomId         = roomId;
            this.numberOfNights = numberOfNights;
        }

        public String getReservationId() { return reservationId; }
        public String getGuestName()     { return guestName; }
        public String getRoomType()      { return roomType; }
        public String getRoomId()        { return roomId; }
        public int getNumberOfNights()   { return numberOfNights; }

        // Serialize to CSV line
        public String serialize() {
            return reservationId + "," + guestName + "," + roomType + ","
                    + roomId + "," + numberOfNights;
        }

        // Deserialize from CSV line
        public static Reservation deserialize(String line) {
            String[] parts = line.split(",");
            return new Reservation(parts[0], parts[1], parts[2], parts[3],
                    Integer.parseInt(parts[4]));
        }

        public void displayDetails() {
            System.out.println("Reservation ID : " + reservationId);
            System.out.println("Guest          : " + guestName);
            System.out.println("Room Type      : " + roomType);
            System.out.println("Room ID        : " + roomId);
            System.out.println("Nights         : " + numberOfNights);
        }
    }

    static class RoomInventory {
        private HashMap<String, Integer> inventory;

        public RoomInventory() {
            inventory = new HashMap<>();
            inventory.put("Single Room", 5);
            inventory.put("Double Room", 3);
            inventory.put("Suite Room",  2);
        }

        public HashMap<String, Integer> getInventory() { return inventory; }

        public void setInventory(HashMap<String, Integer> inventory) {
            this.inventory = inventory;
        }

        public void decrementAvailability(String roomType) {
            if (inventory.containsKey(roomType)) {
                inventory.put(roomType, inventory.get(roomType) - 1);
            }
        }

        // Serialize to CSV lines
        public String serialize() {
            StringBuilder sb = new StringBuilder();
            for (String key : inventory.keySet()) {
                sb.append(key).append(",").append(inventory.get(key)).append("\n");
            }
            return sb.toString().trim();
        }

        // Deserialize from CSV lines
        public static HashMap<String, Integer> deserialize(List<String> lines) {
            HashMap<String, Integer> map = new HashMap<>();
            for (String line : lines) {
                String[] parts = line.split(",");
                map.put(parts[0], Integer.parseInt(parts[1]));
            }
            return map;
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

        public List<Reservation> getHistory() { return history; }

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

    static class PersistenceService {
        private static final String INVENTORY_FILE = "inventory.txt";
        private static final String BOOKINGS_FILE  = "bookings.txt";

        public void saveInventory(RoomInventory inventory) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(INVENTORY_FILE))) {
                writer.write(inventory.serialize());
                System.out.println("Inventory saved to " + INVENTORY_FILE);
            } catch (IOException e) {
                System.out.println("ERROR: Failed to save inventory - " + e.getMessage());
            }
        }

        public void saveBookings(BookingHistory bookingHistory) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKINGS_FILE))) {
                for (Reservation r : bookingHistory.getHistory()) {
                    writer.write(r.serialize());
                    writer.newLine();
                }
                System.out.println("Bookings saved to " + BOOKINGS_FILE);
            } catch (IOException e) {
                System.out.println("ERROR: Failed to save bookings - " + e.getMessage());
            }
        }

        public void restoreInventory(RoomInventory inventory) {
            File file = new File(INVENTORY_FILE);
            if (!file.exists()) {
                System.out.println("No inventory file found. Starting with default inventory.");
                return;
            }
            try (BufferedReader reader = new BufferedReader(new FileReader(INVENTORY_FILE))) {
                List<String> lines = new ArrayList<>();
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) lines.add(line.trim());
                }
                inventory.setInventory(RoomInventory.deserialize(lines));
                System.out.println("Inventory restored from " + INVENTORY_FILE);
            } catch (IOException e) {
                System.out.println("ERROR: Failed to restore inventory - " + e.getMessage());
            }
        }

        public void restoreBookings(BookingHistory bookingHistory) {
            File file = new File(BOOKINGS_FILE);
            if (!file.exists()) {
                System.out.println("No bookings file found. Starting with empty booking history.");
                return;
            }
            try (BufferedReader reader = new BufferedReader(new FileReader(BOOKINGS_FILE))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        bookingHistory.addBooking(Reservation.deserialize(line.trim()));
                    }
                }
                System.out.println("Bookings restored from " + BOOKINGS_FILE);
            } catch (IOException e) {
                System.out.println("ERROR: Failed to restore bookings - " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {

        PersistenceService persistenceService = new PersistenceService();
        RoomInventory inventory               = new RoomInventory();
        BookingHistory bookingHistory         = new BookingHistory();

        System.out.println("============================================");
        System.out.println("      Data Persistence & System Recovery    ");
        System.out.println("============================================");

        // Simulate bookings
        bookingHistory.addBooking(new Reservation("RES-001", "Alice",   "Single Room", "Single-Room-001", 3));
        bookingHistory.addBooking(new Reservation("RES-002", "Bob",     "Suite Room",  "Suite-Room-001",  2));
        bookingHistory.addBooking(new Reservation("RES-003", "Charlie", "Double Room", "Double-Room-001", 1));

        inventory.decrementAvailability("Single Room");
        inventory.decrementAvailability("Suite Room");
        inventory.decrementAvailability("Double Room");

        inventory.displayInventory();
        bookingHistory.displayHistory();

        // Save state
        System.out.println("\n============================================");
        System.out.println("           Saving System State              ");
        System.out.println("============================================");
        persistenceService.saveInventory(inventory);
        persistenceService.saveBookings(bookingHistory);

        // Simulate restart - fresh objects
        System.out.println("\n============================================");
        System.out.println("       System Restart - Recovering State    ");
        System.out.println("============================================");
        RoomInventory restoredInventory     = new RoomInventory();
        BookingHistory restoredHistory      = new BookingHistory();

        persistenceService.restoreInventory(restoredInventory);
        persistenceService.restoreBookings(restoredHistory);

        restoredInventory.displayInventory();
        restoredHistory.displayHistory();
    }
}
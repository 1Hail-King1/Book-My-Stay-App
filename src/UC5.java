import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.Queue;

public class UC5 {

    abstract static class Room {
        private String roomType;
        private int numberOfBeds;
        private double pricePerNight;
        private double roomSize;

        public Room(String roomType, int numberOfBeds, double pricePerNight, double roomSize) {
            this.roomType = roomType;
            this.numberOfBeds = numberOfBeds;
            this.pricePerNight = pricePerNight;
            this.roomSize = roomSize;
        }

        public String getRoomType() { return roomType; }
        public int getNumberOfBeds() { return numberOfBeds; }
        public double getPricePerNight() { return pricePerNight; }
        public double getRoomSize() { return roomSize; }

        public abstract void displayRoomDetails();
    }

    static class SingleRoom extends Room {
        public SingleRoom() { super("Single Room", 1, 80.00, 20.0); }

        @Override
        public void displayRoomDetails() {
            System.out.println("Room Type    : " + getRoomType());
            System.out.println("Beds         : " + getNumberOfBeds());
            System.out.println("Size         : " + getRoomSize() + " sqm");
            System.out.println("Price/Night  : $" + getPricePerNight());
        }
    }

    static class DoubleRoom extends Room {
        public DoubleRoom() { super("Double Room", 2, 120.00, 35.0); }

        @Override
        public void displayRoomDetails() {
            System.out.println("Room Type    : " + getRoomType());
            System.out.println("Beds         : " + getNumberOfBeds());
            System.out.println("Size         : " + getRoomSize() + " sqm");
            System.out.println("Price/Night  : $" + getPricePerNight());
        }
    }

    static class SuiteRoom extends Room {
        public SuiteRoom() { super("Suite Room", 3, 250.00, 75.0); }

        @Override
        public void displayRoomDetails() {
            System.out.println("Room Type    : " + getRoomType());
            System.out.println("Beds         : " + getNumberOfBeds());
            System.out.println("Size         : " + getRoomSize() + " sqm");
            System.out.println("Price/Night  : $" + getPricePerNight());
        }
    }

    static class RoomInventory {
        private HashMap<String, Integer> inventory;

        public RoomInventory() {
            inventory = new HashMap<>();
            inventory.put("Single Room", 5);
            inventory.put("Double Room", 0);
            inventory.put("Suite Room",  2);
        }

        public int getAvailability(String roomType) {
            return inventory.getOrDefault(roomType, 0);
        }

        public void updateAvailability(String roomType, int count) {
            if (inventory.containsKey(roomType)) {
                inventory.put(roomType, count);
            } else {
                System.out.println("Room type not found: " + roomType);
            }
        }

        public HashMap<String, Integer> getAllAvailability() {
            return inventory;
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

    static class SearchService {
        private RoomInventory inventory;
        private List<Room> roomCatalog;

        public SearchService(RoomInventory inventory, List<Room> roomCatalog) {
            this.inventory   = inventory;
            this.roomCatalog = roomCatalog;
        }

        public void searchAvailableRooms() {
            System.out.println("\n============================================");
            System.out.println("        Available Rooms for Booking         ");
            System.out.println("============================================");

            boolean anyAvailable = false;

            for (Room room : roomCatalog) {
                int availability = inventory.getAvailability(room.getRoomType());
                if (availability > 0) {
                    anyAvailable = true;
                    System.out.println("\n--- " + room.getRoomType() + " ---");
                    room.displayRoomDetails();
                    System.out.println("Available    : " + availability + " rooms");
                }
            }

            if (!anyAvailable) {
                System.out.println("\n  No rooms are currently available.");
            }

            System.out.println("\n============================================");
        }

        public void searchByRoomType(String roomType) {
            System.out.println("\n============================================");
            System.out.println("   Search Result for: " + roomType);
            System.out.println("============================================");

            int availability = inventory.getAvailability(roomType);

            if (availability == 0) {
                System.out.println("  No availability found for: " + roomType);
                System.out.println("============================================");
                return;
            }

            for (Room room : roomCatalog) {
                if (room.getRoomType().equalsIgnoreCase(roomType)) {
                    room.displayRoomDetails();
                    System.out.println("Available    : " + availability + " rooms");
                }
            }

            System.out.println("============================================");
        }
    }

    static class Reservation {
        private String guestName;
        private String roomType;
        private int numberOfNights;

        public Reservation(String guestName, String roomType, int numberOfNights) {
            this.guestName      = guestName;
            this.roomType       = roomType;
            this.numberOfNights = numberOfNights;
        }

        public String getGuestName() { return guestName; }
        public String getRoomType() { return roomType; }
        public int getNumberOfNights() { return numberOfNights; }

        public void displayReservationDetails() {
            System.out.println("Guest        : " + guestName);
            System.out.println("Room Type    : " + roomType);
            System.out.println("Nights       : " + numberOfNights);
        }
    }

    static class BookingRequestQueue {
        private Queue<Reservation> requestQueue;

        public BookingRequestQueue() {
            requestQueue = new LinkedList<>();
        }

        public void addRequest(Reservation reservation) {
            requestQueue.offer(reservation);
            System.out.println("Request queued for: " + reservation.getGuestName()
                    + " [" + reservation.getRoomType() + "]");
        }

        public Reservation peekNextRequest() {
            return requestQueue.peek();
        }

        public Reservation pollNextRequest() {
            return requestQueue.poll();
        }

        public boolean hasRequests() {
            return !requestQueue.isEmpty();
        }

        public int getQueueSize() {
            return requestQueue.size();
        }

        public void displayQueue() {
            System.out.println("\n============================================");
            System.out.println("       Pending Booking Requests (FIFO)      ");
            System.out.println("============================================");

            if (requestQueue.isEmpty()) {
                System.out.println("  No pending booking requests.");
            } else {
                int position = 1;
                for (Reservation r : requestQueue) {
                    System.out.println("\nPosition     : " + position++);
                    r.displayReservationDetails();
                }
            }

            System.out.println("\nTotal in Queue: " + requestQueue.size());
            System.out.println("============================================");
        }
    }

    public static void main(String[] args) {

        List<Room> roomCatalog = new ArrayList<>();
        roomCatalog.add(new SingleRoom());
        roomCatalog.add(new DoubleRoom());
        roomCatalog.add(new SuiteRoom());

        RoomInventory inventory       = new RoomInventory();
        SearchService searchService   = new SearchService(inventory, roomCatalog);
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        System.out.println("============================================");
        System.out.println("   Welcome to BookMyStay - Hotel Booking    ");
        System.out.println("============================================");

        inventory.displayInventory();

        searchService.searchAvailableRooms();

        System.out.println("\n============================================");
        System.out.println("         Submitting Booking Requests        ");
        System.out.println("============================================");

        bookingQueue.addRequest(new Reservation("Alice",   "Single Room", 3));
        bookingQueue.addRequest(new Reservation("Bob",     "Suite Room",  2));
        bookingQueue.addRequest(new Reservation("Charlie", "Single Room", 1));
        bookingQueue.addRequest(new Reservation("Diana",   "Suite Room",  4));
        bookingQueue.addRequest(new Reservation("Eve",     "Double Room", 2));

        bookingQueue.displayQueue();

        System.out.println("\nNext to be processed: "
                + bookingQueue.peekNextRequest().getGuestName());
    }
}
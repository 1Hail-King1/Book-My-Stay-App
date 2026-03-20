import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.HashSet;
import java.util.Set;

public class Main {

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
    }

    static class RoomInventory {
        private HashMap<String, Integer> inventory;

        public RoomInventory() {
            inventory = new HashMap<>();
            inventory.put("Single Room", 5);
            inventory.put("Double Room", 3);
            inventory.put("Suite Room",  2);
        }

        public int getAvailability(String roomType) {
            return inventory.getOrDefault(roomType, 0);
        }

        public void decrementAvailability(String roomType) {
            if (inventory.containsKey(roomType)) {
                inventory.put(roomType, inventory.get(roomType) - 1);
            }
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

        public Reservation pollNextRequest() { return requestQueue.poll(); }
        public boolean hasRequests() { return !requestQueue.isEmpty(); }
    }

    static class BookingService {
        private RoomInventory inventory;
        private HashMap<String, Set<String>> allocatedRooms;
        private Set<String> allRoomIds;
        private HashMap<String, Integer> roomCounter;

        public BookingService(RoomInventory inventory) {
            this.inventory      = inventory;
            this.allocatedRooms = new HashMap<>();
            this.allRoomIds     = new HashSet<>();
            this.roomCounter    = new HashMap<>();
        }

        public void processQueue(BookingRequestQueue queue) {
            System.out.println("\n============================================");
            System.out.println("     Processing Reservation Confirmations   ");
            System.out.println("============================================");

            while (queue.hasRequests()) {
                allocateRoom(queue.pollNextRequest());
            }
        }

        private void allocateRoom(Reservation r) {
            String roomType = r.getRoomType();

            if (inventory.getAvailability(roomType) <= 0) {
                System.out.println("\nBooking FAILED for " + r.getGuestName()
                        + " - No available " + roomType + " rooms.");
                return;
            }

            int count = roomCounter.getOrDefault(roomType, 0) + 1;
            roomCounter.put(roomType, count);
            String roomId = roomType.replace(" ", "-") + "-" + String.format("%03d", count);

            if (allRoomIds.contains(roomId)) {
                System.out.println("\nBooking FAILED for " + r.getGuestName()
                        + " - Room ID " + roomId + " already allocated.");
                return;
            }

            allRoomIds.add(roomId);
            allocatedRooms.computeIfAbsent(roomType, k -> new HashSet<>()).add(roomId);
            inventory.decrementAvailability(roomType);

            System.out.println("\nBooking CONFIRMED");
            System.out.println("Guest        : " + r.getGuestName());
            System.out.println("Room Type    : " + roomType);
            System.out.println("Room ID      : " + roomId);
            System.out.println("Nights       : " + r.getNumberOfNights());
        }
    }

    public static void main(String[] args) {

        RoomInventory inventory          = new RoomInventory();
        BookingRequestQueue bookingQueue = new BookingRequestQueue();
        BookingService bookingService    = new BookingService(inventory);

        System.out.println("============================================");
        System.out.println("         Submitting Booking Requests        ");
        System.out.println("============================================");

        bookingQueue.addRequest(new Reservation("Alice",   "Single Room", 3));
        bookingQueue.addRequest(new Reservation("Bob",     "Suite Room",  2));
        bookingQueue.addRequest(new Reservation("Charlie", "Single Room", 1));
        bookingQueue.addRequest(new Reservation("Diana",   "Suite Room",  4));
        bookingQueue.addRequest(new Reservation("Eve",     "Double Room", 2));

        bookingService.processQueue(bookingQueue);
    }
}
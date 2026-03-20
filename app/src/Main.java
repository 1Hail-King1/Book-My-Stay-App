import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.HashSet;
import java.util.Set;

public class Main {

    static class Reservation {
        private String guestName;
        private String roomType;

        public Reservation(String guestName, String roomType) {
            this.guestName = guestName;
            this.roomType  = roomType;
        }

        public String getGuestName() { return guestName; }
        public String getRoomType()  { return roomType; }
    }

    static class RoomInventory {
        private HashMap<String, Integer> inventory;

        public RoomInventory() {
            inventory = new HashMap<>();
            inventory.put("Single Room", 3);
            inventory.put("Double Room", 2);
            inventory.put("Suite Room",  1);
        }

        public synchronized int getAvailability(String roomType) {
            return inventory.getOrDefault(roomType, 0);
        }

        public synchronized boolean decrementAvailability(String roomType) {
            if (inventory.getOrDefault(roomType, 0) > 0) {
                inventory.put(roomType, inventory.get(roomType) - 1);
                return true;
            }
            return false;
        }

        public synchronized void displayInventory() {
            System.out.println("\n============================================");
            System.out.println("         Current Room Inventory             ");
            System.out.println("============================================");
            for (String roomType : inventory.keySet()) {
                System.out.println(roomType + " : " + inventory.get(roomType) + " available");
            }
            System.out.println("============================================");
        }
    }

    static class BookingRequestQueue {
        private Queue<Reservation> queue;

        public BookingRequestQueue() {
            queue = new LinkedList<>();
        }

        public synchronized void addRequest(Reservation reservation) {
            queue.offer(reservation);
        }

        public synchronized Reservation pollRequest() {
            return queue.poll();
        }

        public synchronized boolean hasRequests() {
            return !queue.isEmpty();
        }
    }

    static class ConcurrentBookingProcessor implements Runnable {
        private BookingRequestQueue bookingQueue;
        private RoomInventory inventory;
        private Set<String> allocatedRooms;
        private HashMap<String, Integer> roomCounter;

        public ConcurrentBookingProcessor(BookingRequestQueue bookingQueue,
                                          RoomInventory inventory,
                                          Set<String> allocatedRooms,
                                          HashMap<String, Integer> roomCounter) {
            this.bookingQueue  = bookingQueue;
            this.inventory     = inventory;
            this.allocatedRooms = allocatedRooms;
            this.roomCounter   = roomCounter;
        }

        @Override
        public void run() {
            while (bookingQueue.hasRequests()) {
                synchronized (this) {
                    Reservation r = bookingQueue.pollRequest();

                    if (r == null) return;

                    String roomType = r.getRoomType();

                    boolean allocated = inventory.decrementAvailability(roomType);

                    if (allocated) {
                        synchronized (roomCounter) {
                            int count = roomCounter.getOrDefault(roomType, 0) + 1;
                            roomCounter.put(roomType, count);
                            String roomId = roomType.replace(" ", "-") + "-"
                                    + String.format("%03d", count);
                            allocatedRooms.add(roomId);

                            System.out.println("[Thread: " + Thread.currentThread().getName() + "] "
                                    + "Booking CONFIRMED | Guest: " + r.getGuestName()
                                    + " | Room: " + roomType
                                    + " | Room ID: " + roomId);
                        }
                    } else {
                        System.out.println("[Thread: " + Thread.currentThread().getName() + "] "
                                + "Booking FAILED | Guest: " + r.getGuestName()
                                + " | No available " + roomType + " rooms.");
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        RoomInventory inventory          = new RoomInventory();
        BookingRequestQueue bookingQueue = new BookingRequestQueue();
        Set<String> allocatedRooms       = new HashSet<>();
        HashMap<String, Integer> roomCounter = new HashMap<>();

        System.out.println("============================================");
        System.out.println("   Concurrent Booking Simulation            ");
        System.out.println("============================================");

        bookingQueue.addRequest(new Reservation("Alice",   "Single Room"));
        bookingQueue.addRequest(new Reservation("Bob",     "Double Room"));
        bookingQueue.addRequest(new Reservation("Charlie", "Single Room"));
        bookingQueue.addRequest(new Reservation("Diana",   "Suite Room"));
        bookingQueue.addRequest(new Reservation("Eve",     "Single Room"));
        bookingQueue.addRequest(new Reservation("Frank",   "Double Room"));
        bookingQueue.addRequest(new Reservation("Grace",   "Single Room"));
        bookingQueue.addRequest(new Reservation("Henry",   "Suite Room"));

        inventory.displayInventory();

        System.out.println("\n============================================");
        System.out.println("       Processing Concurrent Requests        ");
        System.out.println("============================================");

        ConcurrentBookingProcessor processor = new ConcurrentBookingProcessor(
                bookingQueue, inventory, allocatedRooms, roomCounter);

        Thread t1 = new Thread(processor, "Guest-Thread-1");
        Thread t2 = new Thread(processor, "Guest-Thread-2");
        Thread t3 = new Thread(processor, "Guest-Thread-3");

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        inventory.displayInventory();
    }
}
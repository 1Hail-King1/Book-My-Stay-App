import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class UC4 {

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

    public static void main(String[] args) {

        List<Room> roomCatalog = new ArrayList<>();
        roomCatalog.add(new SingleRoom());
        roomCatalog.add(new DoubleRoom());
        roomCatalog.add(new SuiteRoom());

        RoomInventory inventory = new RoomInventory();

        SearchService searchService = new SearchService(inventory, roomCatalog);

        System.out.println("============================================");
        System.out.println("   Welcome to BookMyStay - Hotel Booking    ");
        System.out.println("============================================");

        inventory.displayInventory();

        searchService.searchAvailableRooms();

        searchService.searchByRoomType("Double Room");

        searchService.searchByRoomType("Suite Room");
    }
}
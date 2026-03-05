import java.util.HashMap;

public class UC3 {

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
            inventory.put("Double Room", 3);
            inventory.put("Suite Room",  2);
        }

        public int getAvailability(String roomType) {
            return inventory.getOrDefault(roomType, 0);
        }

        public void updateAvailability(String roomType, int count) {
            if (inventory.containsKey(roomType)) {
                inventory.put(roomType, count);
                System.out.println("Inventory updated: " + roomType + " -> " + count + " rooms");
            } else {
                System.out.println("Room type not found: " + roomType);
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

    public static void main(String[] args) {

        Room singleRoom = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suiteRoom  = new SuiteRoom();

        System.out.println("============================================");
        System.out.println("       BookMyStay - Room Availability       ");
        System.out.println("============================================");

        RoomInventory inventory = new RoomInventory();

        System.out.println("\n--- " + singleRoom.getRoomType() + " ---");
        singleRoom.displayRoomDetails();
        System.out.println("Available    : " + inventory.getAvailability("Single Room") + " rooms");

        System.out.println("\n--- " + doubleRoom.getRoomType() + " ---");
        doubleRoom.displayRoomDetails();
        System.out.println("Available    : " + inventory.getAvailability("Double Room") + " rooms");

        System.out.println("\n--- " + suiteRoom.getRoomType() + " ---");
        suiteRoom.displayRoomDetails();
        System.out.println("Available    : " + inventory.getAvailability("Suite Room") + " rooms");

        inventory.displayInventory();

        inventory.updateAvailability("Single Room", 4);
        inventory.updateAvailability("Double Room", 2);

        inventory.displayInventory();
    }
}

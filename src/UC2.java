public class UC2 {

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
        public SingleRoom() {
            super("Single Room", 1, 80.00, 20.0);
        }

        @Override
        public void displayRoomDetails() {
            System.out.println("Room Type    : " + getRoomType());
            System.out.println("Beds         : " + getNumberOfBeds());
            System.out.println("Size         : " + getRoomSize() + " sqm");
            System.out.println("Price/Night  : $" + getPricePerNight());
        }
    }

    static class DoubleRoom extends Room {
        public DoubleRoom() {
            super("Double Room", 2, 120.00, 35.0);
        }

        @Override
        public void displayRoomDetails() {
            System.out.println("Room Type    : " + getRoomType());
            System.out.println("Beds         : " + getNumberOfBeds());
            System.out.println("Size         : " + getRoomSize() + " sqm");
            System.out.println("Price/Night  : $" + getPricePerNight());
        }
    }

    static class SuiteRoom extends Room {
        public SuiteRoom() {
            super("Suite Room", 3, 250.00, 75.0);
        }

        @Override
        public void displayRoomDetails() {
            System.out.println("Room Type    : " + getRoomType());
            System.out.println("Beds         : " + getNumberOfBeds());
            System.out.println("Size         : " + getRoomSize() + " sqm");
            System.out.println("Price/Night  : $" + getPricePerNight());
        }
    }

    public static void main(String[] args) {

        Room singleRoom = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suiteRoom  = new SuiteRoom();

        int singleRoomAvailable = 5;
        int doubleRoomAvailable = 3;
        int suiteRoomAvailable  = 2;

        System.out.println("============================================");
        System.out.println("       BookMyStay - Room Availability       ");
        System.out.println("============================================");

        System.out.println("\n--- " + singleRoom.getRoomType() + " ---");
        singleRoom.displayRoomDetails();
        System.out.println("Available    : " + singleRoomAvailable + " rooms");

        System.out.println("\n--- " + doubleRoom.getRoomType() + " ---");
        doubleRoom.displayRoomDetails();
        System.out.println("Available    : " + doubleRoomAvailable + " rooms");

        System.out.println("\n--- " + suiteRoom.getRoomType() + " ---");
        suiteRoom.displayRoomDetails();
        System.out.println("Available    : " + suiteRoomAvailable + " rooms");

        System.out.println("\n============================================");
    }
}
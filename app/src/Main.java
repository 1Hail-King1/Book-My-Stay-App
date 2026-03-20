public class Main {

    // Custom Exceptions
    static class InvalidRoomTypeException extends Exception {
        public InvalidRoomTypeException(String message) {
            super(message);
        }
    }

    static class InvalidGuestNameException extends Exception {
        public InvalidGuestNameException(String message) {
            super(message);
        }
    }

    static class InvalidNightsException extends Exception {
        public InvalidNightsException(String message) {
            super(message);
        }
    }

    static class RoomNotAvailableException extends Exception {
        public RoomNotAvailableException(String message) {
            super(message);
        }
    }

    // Valid room types
    static final java.util.List<String> VALID_ROOM_TYPES = java.util.Arrays.asList(
            "Single Room", "Double Room", "Suite Room"
    );

    static class RoomInventory {
        private java.util.HashMap<String, Integer> inventory;

        public RoomInventory() {
            inventory = new java.util.HashMap<>();
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

    static class InvalidBookingValidator {

        public static void validateGuestName(String guestName) throws InvalidGuestNameException {
            if (guestName == null || guestName.trim().isEmpty()) {
                throw new InvalidGuestNameException(
                        "Validation Failed: Guest name cannot be null or empty.");
            }
        }

        public static void validateRoomType(String roomType) throws InvalidRoomTypeException {
            if (!VALID_ROOM_TYPES.contains(roomType)) {
                throw new InvalidRoomTypeException(
                        "Validation Failed: Invalid room type '" + roomType + "'. "
                                + "Valid types are: " + VALID_ROOM_TYPES);
            }
        }

        public static void validateNights(int nights) throws InvalidNightsException {
            if (nights <= 0) {
                throw new InvalidNightsException(
                        "Validation Failed: Number of nights must be greater than zero. Got: " + nights);
            }
        }

        public static void validateAvailability(String roomType, int availability)
                throws RoomNotAvailableException {
            if (availability <= 0) {
                throw new RoomNotAvailableException(
                        "Validation Failed: No rooms available for type '" + roomType + "'.");
            }
        }
    }

    static class BookingService {
        private RoomInventory inventory;

        public BookingService(RoomInventory inventory) {
            this.inventory = inventory;
        }

        public void processBooking(String guestName, String roomType, int nights) {
            System.out.println("\n--------------------------------------------");
            System.out.println("Processing booking for: " + guestName
                    + " | Room: " + roomType + " | Nights: " + nights);
            System.out.println("--------------------------------------------");

            try {
                InvalidBookingValidator.validateGuestName(guestName);
                InvalidBookingValidator.validateRoomType(roomType);
                InvalidBookingValidator.validateNights(nights);
                InvalidBookingValidator.validateAvailability(
                        roomType, inventory.getAvailability(roomType));

                inventory.decrementAvailability(roomType);
                System.out.println("Booking CONFIRMED for " + guestName
                        + " | Room: " + roomType + " | Nights: " + nights);

            } catch (InvalidGuestNameException e) {
                System.out.println("ERROR: " + e.getMessage());
            } catch (InvalidRoomTypeException e) {
                System.out.println("ERROR: " + e.getMessage());
            } catch (InvalidNightsException e) {
                System.out.println("ERROR: " + e.getMessage());
            } catch (RoomNotAvailableException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {

        RoomInventory inventory     = new RoomInventory();
        BookingService bookingService = new BookingService(inventory);

        System.out.println("============================================");
        System.out.println("       Error Handling & Validation          ");
        System.out.println("============================================");

        // Valid booking
        bookingService.processBooking("Alice",   "Single Room", 3);

        // Invalid guest name
        bookingService.processBooking("",        "Single Room", 2);

        // Invalid room type
        bookingService.processBooking("Bob",     "Penthouse",   2);

        // Invalid nights
        bookingService.processBooking("Charlie", "Double Room", 0);

        // Invalid nights negative
        bookingService.processBooking("Diana",   "Suite Room",  -1);

        // Valid bookings to exhaust Suite Room
        bookingService.processBooking("Eve",     "Suite Room",  2);
        bookingService.processBooking("Frank",   "Suite Room",  1);

        // No availability
        bookingService.processBooking("Grace",   "Suite Room",  1);
    }
}
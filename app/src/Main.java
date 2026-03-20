import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class Main{

    static class AddOnService {
        private String serviceName;
        private double serviceCost;

        public AddOnService(String serviceName, double serviceCost) {
            this.serviceName = serviceName;
            this.serviceCost = serviceCost;
        }

        public String getServiceName() { return serviceName; }
        public double getServiceCost() { return serviceCost; }
    }

    static class AddOnServiceManager {
        private HashMap<String, List<AddOnService>> reservationServices;

        public AddOnServiceManager() {
            reservationServices = new HashMap<>();
        }

        public void addService(String reservationId, AddOnService service) {
            reservationServices.computeIfAbsent(reservationId, k -> new ArrayList<>()).add(service);
            System.out.println("Service added for Reservation " + reservationId
                    + " : " + service.getServiceName()
                    + " ($" + service.getServiceCost() + ")");
        }

        public double calculateTotalCost(String reservationId) {
            List<AddOnService> services = reservationServices.getOrDefault(reservationId, new ArrayList<>());
            double total = 0;
            for (AddOnService s : services) {
                total += s.getServiceCost();
            }
            return total;
        }

        public void displayServicesForReservation(String reservationId) {
            System.out.println("\n============================================");
            System.out.println("   Add-On Services for Reservation: " + reservationId);
            System.out.println("============================================");

            List<AddOnService> services = reservationServices.getOrDefault(reservationId, new ArrayList<>());

            if (services.isEmpty()) {
                System.out.println("  No add-on services selected.");
            } else {
                for (AddOnService s : services) {
                    System.out.println("Service      : " + s.getServiceName());
                    System.out.println("Cost         : $" + s.getServiceCost());
                    System.out.println("--------------------------------------------");
                }
                System.out.println("Total Add-On Cost : $" + calculateTotalCost(reservationId));
            }

            System.out.println("============================================");
        }
    }

    public static void main(String[] args) {

        AddOnServiceManager serviceManager = new AddOnServiceManager();

        AddOnService breakfast  = new AddOnService("Breakfast",       15.00);
        AddOnService spa        = new AddOnService("Spa",             50.00);
        AddOnService airportPick = new AddOnService("Airport Pickup", 30.00);
        AddOnService laundry    = new AddOnService("Laundry",         10.00);
        AddOnService parking    = new AddOnService("Parking",         20.00);

        System.out.println("============================================");
        System.out.println("        Add-On Service Selection            ");
        System.out.println("============================================");

        serviceManager.addService("RES-001", breakfast);
        serviceManager.addService("RES-001", spa);
        serviceManager.addService("RES-001", airportPick);

        serviceManager.addService("RES-002", laundry);
        serviceManager.addService("RES-002", parking);

        serviceManager.displayServicesForReservation("RES-001");
        serviceManager.displayServicesForReservation("RES-002");
    }
}
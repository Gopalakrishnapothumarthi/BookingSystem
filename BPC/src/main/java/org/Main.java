package org;

import java.time.LocalDateTime;
import java.util.*;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static BookingSystem bookingSystem = new BookingSystem();
    static List<List<String>> physiotherapistsData = new ArrayList<>();

    public static void main(String[] args) {
        setupDemoData();
        while (true) {
            System.out.println("\n--- Boost Physio Clinic Booking System ---");
            System.out.println("1. Add Patient");
            System.out.println("2. Remove Patient");
            System.out.println("3. Book Appointment");
            System.out.println("4. Cancel Appointment");
            System.out.println("5. Attend Appointment");
            System.out.println("6. Search by Expertise");
            System.out.println("7. Search by Physiotherapist");
            System.out.println("8. Print Report");
            System.out.println("9. Run All Options Demo");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            switch (scanner.nextInt()) {
                case 1 -> addPatient();
                case 2 -> removePatient();
                case 3 -> bookAppointment();
                case 4 -> cancelAppointment();
                case 5 -> attendAppointment();
                case 6 -> searchByExpertise();
                case 7 -> searchByPhysio();
                case 8 -> bookingSystem.generateReport();
                case 9 -> runAllOptionsDemo();
                case 0 -> {
                    System.out.println("Exiting the system. Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }
    private static void addPhysiotherapistDetails(String id, String name, List<String> data) {
        List<String> physiotherapistDetails = new ArrayList<>();
        physiotherapistDetails.add(id);
        physiotherapistDetails.add(name);
        physiotherapistDetails.add(String.valueOf(data));
        physiotherapistsData.add(physiotherapistDetails);
    }
    private static void printPhysiotherapistDetails() {
        // Iterate through each physiotherapist's details stored in physiotherapistsData
        for (List<String> physiotherapist : physiotherapistsData) {
            // Print each physiotherapist's data (ID, Name, Expertise)
            System.out.println("ID: " + physiotherapist.get(0));
            System.out.println("Name: " + physiotherapist.get(1));
            System.out.println("Expertise: " + physiotherapist.get(2));
            System.out.println("---------------------------");
        }
    }


    private static void setupDemoData() {
        System.out.println("Setting up demo data...");
        Physiotherapist helen = new Physiotherapist(1, "Helen", "123 Street", "1234567890", List.of("Rehabilitation", "Massage"));
        Physiotherapist mark = new Physiotherapist(2, "Mark", "234 Road", "9876543210", List.of("Physiotherapy", "Osteopathy"));
        Physiotherapist john = new Physiotherapist(3, "John", "567 Avenue", "5554443333", List.of("Sports Therapy", "Massage"));
        Physiotherapist sara = new Physiotherapist(4, "Sara", "890 Boulevard", "6665554444", List.of("Rehabilitation", "Osteopathy"));
        Physiotherapist amy = new Physiotherapist(5, "Amy", "321 Lane", "7778889999", List.of("Physiotherapy", "Sports Therapy"));
        addPhysiotherapistDetails("1","Helen",List.of("Rehabilitation", "Massage"));
        addPhysiotherapistDetails("2", "Mark", List.of("Physiotherapy", "Osteopathy"));
        addPhysiotherapistDetails("3", "John", List.of("Sports Therapy", "Massage"));
        addPhysiotherapistDetails("4", "Sara", List.of("Rehabilitation", "Osteopathy"));
        addPhysiotherapistDetails("5", "Amy", List.of("Physiotherapy", "Sports Therapy"));

        bookingSystem.addPhysiotherapist(helen);
        bookingSystem.addPhysiotherapist(mark);
        bookingSystem.addPhysiotherapist(john);
        bookingSystem.addPhysiotherapist(sara);
        bookingSystem.addPhysiotherapist(amy);

        Patient[] patients = {
                new Patient(101, "Alice", "Main St", "111222333"),
                new Patient(102, "Bob", "Green Ave", "444555666"),
                new Patient(103, "Charlie", "Oak Rd", "555666777"),
                new Patient(104, "David", "Pine Ln", "666777888"),
                new Patient(105, "Eva", "Birch St", "777888999"),
                new Patient(106, "Fay", "Cedar Blvd", "888999000"),
                new Patient(107, "George", "Elm Ave", "999000111"),
                new Patient(108, "Hannah", "Maple Dr", "101112131"),
                new Patient(109, "Ivy", "Willow Way", "112233445"),
                new Patient(110, "Jack", "Redwood Rd", "223344556"),
                new Patient(111, "Kara", "Spruce St", "334455667"),
                new Patient(112, "Leo", "Fir Ave", "445566778"),
                new Patient(113, "Maya", "Hickory Ln", "556677889"),
                new Patient(114, "Nina", "Chestnut Blvd", "667788990"),
                new Patient(115, "Oscar", "Ash St", "778899001"),
                new Patient(116, "Paul", "Sycamore Dr", "889900112"),
                new Patient(117, "Quincy", "Poplar Way", "990011223")
        };

// Loop to add all patients
        for (Patient patient : patients) {
            bookingSystem.addPatient(patient);
        }
        for (int i = 0; i < 4; i++) {
            LocalDateTime dt = LocalDateTime.of(2025, 5, 1 + i * 7, 10, 0);
            Treatment t1 = new Treatment("Massage");
            Appointment a1 = new Appointment(t1, dt, patients[i], helen);
            helen.addAppointment(a1);
            bookingSystem.addAppointmentToSystem(a1);

            Treatment t2 = new Treatment("Osteopathy");
            Appointment a2 = new Appointment(t2, dt.plusHours(1), patients[4+i], mark);
            mark.addAppointment(a2);
            bookingSystem.addAppointmentToSystem(a2);
        }
        System.out.println("Demo data setup complete.");
    }

    private static void addPatient() {
        System.out.println("Adding a new patient...");
        System.out.print("Enter ID: ");
        int id = scanner.nextInt(); scanner.nextLine();
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Address: ");
        String address = scanner.nextLine();
        System.out.print("Phone: ");
        String phone = scanner.nextLine();
        bookingSystem.addPatient(new Patient(id, name, address, phone));
        System.out.println("Patient added successfully.");
    }

    private static void removePatient() {
        System.out.print("Enter patient ID to remove: ");
        int id = scanner.nextInt();
        bookingSystem.removePatient(id);
        System.out.println("Patient removal attempted.");
    }

    private static void bookAppointment() {
        printPhysiotherapistDetails();
        System.out.println("Booking an appointment...");
        System.out.print("Physiotherapist ID: ");
        int pid = scanner.nextInt();
        scanner.nextLine();
        System.out.print("DateTime (yyyy-MM-ddTHH:mm): ");
        String dtStr = scanner.nextLine();
        System.out.print("Treatment name: ");
        String tname = scanner.nextLine();
        System.out.print("Patient ID: ");
        int patId = scanner.nextInt();

        LocalDateTime dt = LocalDateTime.parse(dtStr);
        bookingSystem.bookAppointment(pid, dt, tname, patId);
        System.out.println("Appointment booking attempted.");
    }

    private static void cancelAppointment() {
        System.out.print("Enter Appointment ID to cancel: ");
        int id = scanner.nextInt();
        if (bookingSystem.cancelAppointment(id)) {
            System.out.println("Appointment cancelled successfully.");
        } else {
            System.out.println("Cancellation failed. Check if ID is correct.");
        }
    }

    private static void attendAppointment() {
        System.out.print("Enter Appointment ID to attend: ");
        int id = scanner.nextInt();
        if (bookingSystem.attendAppointment(id)) {
            System.out.println("Appointment marked as attended.");
        } else {
            System.out.println("Invalid appointment or already attended.");
        }
    }

    private static void searchByExpertise() {
        System.out.print("Enter area of expertise: ");
        scanner.nextLine();
        String area = scanner.nextLine();
        List<Appointment> result = bookingSystem.searchByExpertise(area);
        System.out.println("Appointments matching expertise:");
        result.forEach(a -> System.out.println(a.getDetails()));
    }

    private static void searchByPhysio() {
        System.out.print("Enter physiotherapist name: ");
        scanner.nextLine();
        String name = scanner.nextLine();
        List<Appointment> result = bookingSystem.searchByPhysiotherapistName(name);
        System.out.println("Appointments with " + name + ":");
        result.forEach(a -> System.out.println(a.getDetails()));
    }

    private static void runAllOptionsDemo() {
        System.out.println("Running full demo sequence...");

        // 1. Add Patient
        bookingSystem.addPatient(new Patient(200, "DemoUser", "DemoStreet", "000111222"));
        System.out.println("[1] Patient added.");

        // 2. Remove Patient
        bookingSystem.removePatient(200);
        System.out.println("[2] Patient removed.");

        // 3. Book Appointment
        LocalDateTime demoTime = LocalDateTime.of(2025, 5, 1, 10, 0);
        bookingSystem.bookAppointment(1, demoTime, "Massage", 101);
        System.out.println("[3] Appointment booked.");

        // 4. Cancel Appointment (assumes ID 1 exists)
        boolean cancelled = bookingSystem.cancelAppointment(1);
        System.out.println("[4] Appointment cancel status: " + cancelled);

        // 5. Attend Appointment (assumes ID 2 exists)
        boolean attended = bookingSystem.attendAppointment(2);
        System.out.println("[5] Appointment attend status: " + attended);

        // 6. Search by Expertise
        List<Appointment> byExpertise = bookingSystem.searchByExpertise("Massage");
        System.out.println("[6] Expertise search result:");
        byExpertise.forEach(a -> System.out.println(a.getDetails()));

        // 7. Search by Physio
        List<Appointment> byPhysio = bookingSystem.searchByPhysiotherapistName("Helen");
        System.out.println("[7] Physio search result:");
        byPhysio.forEach(a -> System.out.println(a.getDetails()));

        // 8. Report
        System.out.println("[8] Generating report:");
        bookingSystem.generateReport();

        System.out.println("--- Demo run complete ---");
    }
}

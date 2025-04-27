package org;
import java.time.LocalDateTime;
import java.util.*;
public class BookingSystem {
    List<Appointment> allAppointments = new ArrayList<>();
    public List<Patient> patients = new ArrayList<>();
    List<Physiotherapist> physiotherapists = new ArrayList<>();

    public void addPatient(Patient patient) {
        patients.add(patient);
    }

    public void removePatient(int patientId) {
        patients.removeIf(p -> p.getId() == patientId);
    }

    public void addPhysiotherapist(Physiotherapist physio) {
        physiotherapists.add(physio);
    }

    public boolean bookAppointment(int physioId, LocalDateTime dateTime, String treatmentName, int patientId) {
        Patient patient = patients.stream().filter(p -> p.getId() == patientId).findFirst().orElse(null);
        Physiotherapist physio = physiotherapists.stream().filter(p -> p.id == physioId).findFirst().orElse(null);

        if (patient == null || physio == null) return false;

        if (patient.hasConflict(dateTime)) {
            System.out.println("Error: Patient has a time conflict.");
            return false;
        }

        for (Appointment a : physio.getAppointments()) {
            if (a.getDateTime().equals(dateTime) && a.getStatus().equals("Scheduled") && a.getPatient() == null) {
                a.setPatient(patient);
                patient.bookAppointment(a);
                System.out.println("Appointment booked: " + a.getDetails());
                return true;
            }
        }

        // If no appointment exists at this time, create a new one
        Treatment treatment = new Treatment(treatmentName);
        Appointment newAppointment = new Appointment(treatment, dateTime, patient, physio);

        physio.addAppointment(newAppointment);
        patient.bookAppointment(newAppointment);
        addAppointmentToSystem(newAppointment);

        System.out.println("New appointment created and booked: " + newAppointment.getDetails());
        return true;
    }

    public boolean cancelAppointment(int appointmentId) {
        for (Appointment appointment : allAppointments) {
            if (appointment.getId() == appointmentId) {
                if (appointment.getStatus().equals("Scheduled")) {
                    appointment.setStatus("Cancelled");
                    if (appointment.getPatient() != null) {
                        appointment.setStatus("Cancelled");
                        appointment.setPatient(null);
                    }
                    return true;
                } else {
                    System.out.println("Appointment is not scheduled (maybe already attended or cancelled).");
                    return false;
                }
            }
        }
        System.out.println("Appointment ID not found.");
        return false;
    }


    public boolean attendAppointment(int appointmentId) {
        for (Appointment a : allAppointments) {
            if (a.getId() == appointmentId) {
                return a.attend();
            }
        }
        return false;
    }

    public void generateReport() {
        Map<Physiotherapist, Integer> attendanceCount = new HashMap<>();

        for (Physiotherapist physio : physiotherapists) {
            System.out.println("--- Appointments for " + physio.getName() + " ---");
            int attended = 0;
            for (Appointment a : physio.getAppointments()) {
                System.out.println(a.getDetails());
                if (a.getStatus().equals("Attended")) attended++;
            }
            attendanceCount.put(physio, attended);
        }

        System.out.println("--- Physiotherapist Attendance Ranking ---");
        attendanceCount.entrySet().stream()
                .sorted(Map.Entry.<Physiotherapist, Integer>comparingByValue().reversed())
                .forEach(e -> System.out.println(e.getKey().getName() + ": " + e.getValue()));
    }

    public void addAppointmentToSystem(Appointment appointment) {
        allAppointments.add(appointment);
    }

    public List<Appointment> searchByExpertise(String expertise) {
        List<Appointment> result = new ArrayList<>();
        for (Physiotherapist p : physiotherapists) {
            if (p.getAreasOfExpertise().contains(expertise)) {
                for (Appointment a : p.getAppointments()) {
                    if (a.isAvailable()) {
                        result.add(a);
                    }
                }
            }
        }
        return result;
    }

    public List<Appointment> searchByPhysiotherapistName(String name) {
        for (Physiotherapist p : physiotherapists) {
            if (p.getName().equalsIgnoreCase(name)) {
                return p.getAppointments();
            }
        }
        return Collections.emptyList();
    }
}

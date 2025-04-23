package org;
import java.util.*;
import java.time.LocalDateTime;
class Patient {
    private int id;
    private String name;
    private String address;
    private String contactInfo;
    private List<Appointment> appointments = new ArrayList<>();

    public Patient(int id, String name, String address, String contactInfo) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.contactInfo = contactInfo;
    }

    public void bookAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

    public void cancelAppointment(Appointment appointment) {
        appointments.remove(appointment);
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public boolean hasConflict(LocalDateTime dateTime) {
        return appointments.stream()
                .anyMatch(a -> a.getDateTime().equals(dateTime) && a.getStatus().equals("Scheduled"));
    }
}

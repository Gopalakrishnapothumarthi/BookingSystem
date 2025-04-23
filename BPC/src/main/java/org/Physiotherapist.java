
package org;

import java.time.LocalDateTime;
import java.util.*;

class Physiotherapist {
    private int id;
    private String name;
    private String address;
    private String contactInfo;
    private List<String> areasOfExpertise;
    private List<Appointment> appointments = new ArrayList<>();

    public Physiotherapist(int id, String name, String address, String contactInfo, List<String> expertise) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.contactInfo = contactInfo;
        this.areasOfExpertise = expertise;
    }

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public String getName() {
        return name;
    }

    public List<String> getAreasOfExpertise() {
        return areasOfExpertise;
    }
}
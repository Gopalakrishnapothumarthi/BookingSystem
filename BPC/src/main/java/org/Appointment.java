package org;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

public class Appointment {
    private static final AtomicInteger idGenerator = new AtomicInteger();
    private final int id;
    private Treatment treatment;
    private LocalDateTime dateTime;
    private Patient patient;
    private Physiotherapist physiotherapist;
    private String status; // Scheduled, Cancelled, Attended

    public Appointment(Treatment treatment, LocalDateTime dateTime, Patient patient, Physiotherapist physiotherapist) {
        this.id = idGenerator.incrementAndGet();
        this.treatment = treatment;
        this.dateTime = dateTime;
        this.patient = patient;
        this.physiotherapist = physiotherapist;
        this.status = "Scheduled";
    }

    public boolean isAvailable() {
        return status.equals("Scheduled") && patient == null;
    }

    public boolean cancel() {
        if (!status.equals("Cancelled")) {
            status = "Cancelled";
            return true;
        }
        return false;
    }

    public boolean attend() {
        if (status.equals("Scheduled")) {
            status = "Attended";
            return true;
        }
        return false;
    }

    public String getDetails() {
        String patientDetails = (patient == null) ? "None" : "ID: " + patient.getId() + ", Name: " + patient.getName();
        return "Appointment ID: " + id + ", Treatment: " + treatment.getName() + ", Date: " + dateTime + ", Patient: " + patientDetails + ", Physiotherapist: " + physiotherapist.getName() + ", Status: " + status;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Patient getPatient() {
        return patient;
    }

    public Physiotherapist getPhysiotherapist() {
        return physiotherapist;
    }

    public String getStatus() {
        return status;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public boolean conflictsWith(Appointment other) {
        return this.dateTime.equals(other.dateTime);
    }
    public void setStatus(String status) {
        this.status = status;
    }

}


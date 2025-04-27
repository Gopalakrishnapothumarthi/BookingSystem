package org;

import org.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BookingSystemTest_MultiWeekBooking {

    BookingSystem bookingSystem;
    Patient patient;
    Physiotherapist physio;
    Treatment treatment;

    @BeforeEach
    void setup() {
        bookingSystem = new BookingSystem();

        // Setup patient and physiotherapist
        patient = new Patient(200, "Jane Doe", "Elm Street", "999888777");
        physio = new Physiotherapist(300, "Dr. House", "Clinic Blvd", "123123123", List.of("Sports Therapy"));

        bookingSystem.addPatient(patient);
        bookingSystem.addPhysiotherapist(physio);

        treatment = new Treatment("Sports Therapy");

        // Add weekly appointments over 4 weeks
        for (int i = 0; i < 4; i++) {
            LocalDateTime dateTime = LocalDateTime.of(2025, 5, 1 + (i * 7), 9, 0);
            Appointment appointment = new Appointment(treatment, dateTime, null, physio);
            physio.addAppointment(appointment);
            bookingSystem.addAppointmentToSystem(appointment);
        }
    }

    @Test
    void testBookMultipleAppointmentsAcrossWeeks() {
        for (int i = 0; i < 4; i++) {
            LocalDateTime dateTime = LocalDateTime.of(2025, 5, 1 + (i * 7), 9, 0);
            boolean booked = bookingSystem.bookAppointment(physio.id, dateTime, "Sports Therapy", patient.getId());
            assertTrue(booked, "Appointment should be booked for week " + (i + 1));
        }

        // Ensure all 4 appointments are booked with this patient
        long bookedCount = physio.getAppointments().stream()
                .filter(a -> a.getPatient() == patient && a.getStatus().equals("Scheduled"))
                .count();
        assertEquals(4, bookedCount, "Patient should have 4 booked appointments.");
    }
}

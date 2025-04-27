package org;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BookingSystemTest {
    private BookingSystem bookingSystem;
    private Patient patient;
    private Physiotherapist physio;
    private Appointment appointment;
    private Treatment treatment;

    @BeforeEach
    public void setUp() {
        bookingSystem = new BookingSystem();
        patient = new Patient(1, "John Doe", "123 Main St", "1234567890");
        treatment = new Treatment("Massage");
        physio = new Physiotherapist(1, "Dr. Smith", "456 Clinic Rd", "0987654321", List.of("Massage", "Rehab"));
        appointment = new Appointment(treatment, LocalDateTime.of(2025, 5, 1, 10, 0), null, physio);

        physio.addAppointment(appointment);
        bookingSystem.addPatient(patient);
        bookingSystem.addPhysiotherapist(physio);
        bookingSystem.addAppointmentToSystem(appointment);
    }

    @Test
    public void testAddAndRemovePatient() {
        Patient newPatient = new Patient(2, "Jane Doe", "789 Lane", "5555555");
        bookingSystem.addPatient(newPatient);
        assertEquals(2, bookingSystem.patients.size());

        bookingSystem.removePatient(2);
        assertEquals(1, bookingSystem.patients.size());
    }

    @Test
    public void testBookAppointmentSuccessfully() {
        boolean booked = bookingSystem.bookAppointment(1, LocalDateTime.of(2025, 5, 1, 10, 0), "Massage", 1);
        assertTrue(booked);
        assertEquals(patient, appointment.getPatient());
    }

    @Test
    public void testBookAppointmentWithConflict() {
        // First appointment already booked for the same time
        bookingSystem.bookAppointment(1, LocalDateTime.of(2025, 5, 1, 10, 0), "Massage", 1);
        Appointment another = new Appointment(treatment, LocalDateTime.of(2025, 5, 1, 10, 0), null, physio);
        physio.addAppointment(another);
        bookingSystem.addAppointmentToSystem(another);

        // Attempt to book another appointment at the same time should fail
        boolean booked = bookingSystem.bookAppointment(1, LocalDateTime.of(2025, 5, 1, 10, 0), "Massage", 1);
        assertFalse(booked);
    }

    @Test
    public void testBookAppointmentForDifferentTreatmentAtSameTime() {
        // Book a massage appointment first
        bookingSystem.bookAppointment(1, LocalDateTime.of(2025, 5, 1, 10, 0), "Massage", 1);

        // Trying to book another treatment (Rehab) at the same time for the same patient
        Treatment rehabTreatment = new Treatment("Rehab");
        Appointment rehabAppointment = new Appointment(rehabTreatment, LocalDateTime.of(2025, 5, 1, 10, 0), null, physio);
        physio.addAppointment(rehabAppointment);
        bookingSystem.addAppointmentToSystem(rehabAppointment);

        // Attempt to book should fail since the physiotherapist is already booked
        boolean booked = bookingSystem.bookAppointment(1, LocalDateTime.of(2025, 5, 1, 10, 0), "Rehab", 1);
        assertFalse(booked);
    }

    @Test
    public void testCancelAppointment() {
        bookingSystem.bookAppointment(1, appointment.getDateTime(), "Massage", 1);
        boolean cancelled = bookingSystem.cancelAppointment(appointment.getId());
        assertTrue(cancelled);
        assertEquals("Cancelled", appointment.getStatus());
    }

    @Test
    public void testAttendAppointment() {
        bookingSystem.bookAppointment(1, appointment.getDateTime(), "Massage", 1);
        boolean attended = bookingSystem.attendAppointment(appointment.getId());
        assertTrue(attended);
        assertEquals("Attended", appointment.getStatus());
    }

    @Test
    public void testSearchByExpertise() {
        List<Appointment> result = bookingSystem.searchByExpertise("Massage");
        assertEquals(1, result.size());
        assertTrue(result.get(0).isAvailable());
    }

    @Test
    public void testSearchByPhysiotherapistName() {
        List<Appointment> result = bookingSystem.searchByPhysiotherapistName("Dr. Smith");
        assertEquals(1, result.size());
        assertEquals(appointment, result.get(0));
    }

    // New test case: Overlapping Appointments for the Same Patient and Physiotherapist
    @Test
    public void testBookOverlappingAppointmentForSamePatientAndPhysiotherapist() {
        // Patient already has an appointment with the physiotherapist at this time
        bookingSystem.bookAppointment(1, LocalDateTime.of(2025, 5, 1, 10, 0), "Massage", 1);

        // Try to book another appointment for the same patient and physiotherapist at the same time
        boolean booked = bookingSystem.bookAppointment(1, LocalDateTime.of(2025, 5, 1, 10, 0), "Rehab", 1);
        assertFalse(booked); // It should fail due to the time conflict
    }

    // New test case: Different Treatments at the Same Time
    @Test
    public void testBookDifferentTreatmentAtSameTime() {
        // First appointment is for Massage
        bookingSystem.bookAppointment(1, LocalDateTime.of(2025, 5, 1, 10, 0), "Massage", 1);

        // Attempt to book a different treatment (Rehab) at the same time
        boolean booked = bookingSystem.bookAppointment(1, LocalDateTime.of(2025, 5, 1, 10, 0), "Rehab", 1);
        assertFalse(booked); // This should fail due to overlapping time slots
    }
}

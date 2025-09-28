package main;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.Date;

class AppointmentTest {

    // Test to verify that the appointment ID is unique, not null, and no longer than 10 characters
    @Test
    void testValidAppointmentId() {
        Appointment appointment = new Appointment("1234567890", new Date(System.currentTimeMillis() + 100000), "Meeting with client");
        assertEquals("1234567890", appointment.getAppointmentId(), "The appointment ID should match the expected value.");
    }

    @Test
    void testNullAppointmentId() {
        assertThrows(IllegalArgumentException.class, () -> 
            new Appointment(null, new Date(System.currentTimeMillis() + 100000), "Team meeting"),
            "Creating an appointment with a null ID should throw an IllegalArgumentException.");
    }

    @Test
    void testLongAppointmentId() {
        assertThrows(IllegalArgumentException.class, () -> 
            new Appointment("12345678901", new Date(System.currentTimeMillis() + 100000), "Team meeting"),
            "Creating an appointment with an ID longer than 10 characters should throw an IllegalArgumentException.");
    }

    // Test to verify that the appointment date is not null and not in the past
    @Test
    void testValidAppointmentDate() {
        Appointment appointment = new Appointment("1234567890", new Date(System.currentTimeMillis() + 100000), "Meeting with client");
        assertNotNull(appointment.getAppointmentDate(), "The appointment date should not be null.");
    }

    @Test
    void testNullAppointmentDate() {
        assertThrows(IllegalArgumentException.class, () -> 
            new Appointment("1234567890", null, "Doctor appointment"),
            "Creating an appointment with a null date should throw an IllegalArgumentException.");
    }

    @Test
    void testPastAppointmentDate() {
        assertThrows(IllegalArgumentException.class, () -> 
            new Appointment("1234567890", new Date(System.currentTimeMillis() - 100000), "Doctor appointment"),
            "Creating an appointment with a past date should throw an IllegalArgumentException.");
    }

    // Test to verify that the description is not null and no longer than 50 characters
    @Test
    void testValidAppointmentDescription() {
        Appointment appointment = new Appointment("1234567890", new Date(System.currentTimeMillis() + 100000), "Appointment description");
        assertEquals("Appointment description", appointment.getDescription(), "The description should match the expected value.");
    }

    @Test
    void testNullAppointmentDescription() {
        assertThrows(IllegalArgumentException.class, () -> 
            new Appointment("1234567890", new Date(System.currentTimeMillis() + 100000), null),
            "Creating an appointment with a null description should throw an IllegalArgumentException.");
    }

    @Test
    void testLongAppointmentDescription() {
        assertThrows(IllegalArgumentException.class, () -> 
            new Appointment("1234567890", new Date(System.currentTimeMillis() + 100000), 
                "This description is way too long and exceeds the 50 character limit!"),
            "Creating an appointment with a description longer than 50 characters should throw an IllegalArgumentException.");
    }
}

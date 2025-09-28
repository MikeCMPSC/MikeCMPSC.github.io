package main;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class AppointmentServiceTest {
    private AppointmentService service;

    @BeforeEach
    public void setUp() {
        service = new AppointmentService();
    }

    @Test
    public void testAddAppointment() {
        Appointment appointment = new Appointment("001", new Date(System.currentTimeMillis() + 10000), "Doctor's appointment");
        service.addAppointment(appointment);
        
        Appointment retrievedAppointment = service.getAppointment("001");
        assertEquals(appointment, retrievedAppointment, "The retrieved appointment should match the added appointment.");
    }

    @Test
    public void testAddDuplicateAppointment() {
        Appointment appointment = new Appointment("001", new Date(System.currentTimeMillis() + 10000), "Doctor's appointment");
        service.addAppointment(appointment);

        assertThrows(IllegalArgumentException.class, () -> {
            service.addAppointment(appointment);
        }, "Adding a duplicate appointment should throw an IllegalArgumentException.");
    }

    @Test
    public void testDeleteAppointment() {
        Appointment appointment = new Appointment("001", new Date(System.currentTimeMillis() + 10000), "Doctor's appointment");
        service.addAppointment(appointment);

        service.deleteAppointment("001");

        Appointment retrievedAppointment = service.getAppointment("001");
        assertNull(retrievedAppointment, "The appointment should be null after deletion.");
    }

    @Test
    public void testDeleteNonExistentAppointment() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.deleteAppointment("non-existent-id");
        }, "Deleting a non-existent appointment should throw an IllegalArgumentException.");
    }

    @Test
    public void testGetNonExistentAppointment() {
        Appointment retrievedAppointment = service.getAppointment("non-existent-id");
        assertNull(retrievedAppointment, "Retrieving a non-existent appointment should return null.");
    }
}

package test;

import main.Appointment;
import main.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Date;
import java.util.Calendar;
import static org.junit.jupiter.api.Assertions.*;

public class AppointmentServiceTest {

    private AppointmentService appointmentService;

    @BeforeEach
    void setUp() {
        appointmentService = new AppointmentService();

        // Create dates in the future for valid appointments
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        Date tomorrow = cal.getTime();
        cal.add(Calendar.DATE, 1);
        Date dayAfterTomorrow = cal.getTime();

        appointmentService.addAppointment(new Appointment("A1", tomorrow, "Doctor appointment"));
        appointmentService.addAppointment(new Appointment("A2", dayAfterTomorrow, "Team meeting with HR"));
        appointmentService.addAppointment(new Appointment("A3", dayAfterTomorrow, "Project demo presentation"));
    }

    @Test
    void testSearchAppointmentsByDescription() {
        List<Appointment> results = appointmentService.searchAppointments("Doctor");
        assertEquals(1, results.size());
        assertEquals("A1", results.get(0).getAppointmentId());
    }

    @Test
    void testSearchAppointmentsCaseInsensitive() {
        List<Appointment> results = appointmentService.searchAppointments("MEETING");
        assertEquals(1, results.size());
        assertEquals("A2", results.get(0).getAppointmentId());
    }

    @Test
    void testSearchAppointmentsPartialMatch() {
        List<Appointment> results = appointmentService.searchAppointments("Project");
        assertEquals(1, results.size());
        assertEquals("A3", results.get(0).getAppointmentId());
    }

    @Test
    void testSearchAppointmentsNoResults() {
        List<Appointment> results = appointmentService.searchAppointments("Dentist");
        assertTrue(results.isEmpty());
    }
}

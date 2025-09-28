package main;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class AppointmentService {
    private Map<String, Appointment> appointments = new HashMap<>();

    public void addAppointment(Appointment appointment) {
        if (appointments.containsKey(appointment.getAppointmentId())) {
            throw new IllegalArgumentException("Appointment ID already exists");
        }
        appointments.put(appointment.getAppointmentId(), appointment);
    }

    public void deleteAppointment(String appointmentId) {
        if (!appointments.containsKey(appointmentId)) {
            throw new IllegalArgumentException("Appointment ID not found");
        }
        appointments.remove(appointmentId);
    }

    public Appointment getAppointment(String appointmentId) {
        return appointments.get(appointmentId);
    }

    // -------------------- Enhancement: Search & Filtering --------------------
    public List<Appointment> searchAppointments(String criteria) {
        List<Appointment> results = new ArrayList<>();
        for (Appointment appointment : appointments.values()) {
            if (appointment.getDescription().toLowerCase().contains(criteria.toLowerCase())) {
                results.add(appointment);
            }
        }
        return results;
    }
}

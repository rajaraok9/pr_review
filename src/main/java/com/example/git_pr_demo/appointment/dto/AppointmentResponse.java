package com.example.git_pr_demo.appointment.dto;

import com.example.git_pr_demo.appointment.entity.Appointment;
import com.example.git_pr_demo.appointment.entity.AppointmentStatus;

import java.time.LocalDateTime;

public class AppointmentResponse {

    private final Long id;
    private final String patientId;
    private final String doctorId;
    private final LocalDateTime appointmentDate;
    private final AppointmentStatus status;

    public AppointmentResponse(Long id, String patientId, String doctorId, LocalDateTime appointmentDate, AppointmentStatus status) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDate = appointmentDate;
        this.status = status;
    }

    public static AppointmentResponse from(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getId(),
                appointment.getPatientId(),
                appointment.getDoctorId(),
                appointment.getAppointmentDate(),
                appointment.getStatus()
        );
    }

    public Long getId() {
        return id;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public AppointmentStatus getStatus() {
        return status;
    }
}

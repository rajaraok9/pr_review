package com.example.git_pr_demo.appointment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class CreateAppointmentRequest {

    @NotBlank(message = "patientId is mandatory")
    private String patientId;

    @NotBlank(message = "doctorId is mandatory")
    private String doctorId;

    @NotNull(message = "appointmentDate is mandatory")
    private LocalDateTime appointmentDate;

    public CreateAppointmentRequest() {
    }

    public CreateAppointmentRequest(String patientId, String doctorId, LocalDateTime appointmentDate) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDate = appointmentDate;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
}

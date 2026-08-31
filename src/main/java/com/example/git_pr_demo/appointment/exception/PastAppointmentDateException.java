package com.example.git_pr_demo.appointment.exception;

public class PastAppointmentDateException extends RuntimeException {

    public PastAppointmentDateException() {
        super("appointmentDate cannot be in the past");
    }
}

package com.example.git_pr_demo.appointment.exception;

public class AppointmentAlreadyCancelledException extends RuntimeException {

    public AppointmentAlreadyCancelledException(Long id) {
        super("Appointment with id " + id + " is already cancelled");
    }
}

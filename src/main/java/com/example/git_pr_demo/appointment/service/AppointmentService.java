package com.example.git_pr_demo.appointment.service;

import com.example.git_pr_demo.appointment.dto.CreateAppointmentRequest;
import com.example.git_pr_demo.appointment.entity.Appointment;
import com.example.git_pr_demo.appointment.entity.AppointmentStatus;
import com.example.git_pr_demo.appointment.exception.AppointmentAlreadyCancelledException;
import com.example.git_pr_demo.appointment.exception.AppointmentNotFoundException;
import com.example.git_pr_demo.appointment.exception.PastAppointmentDateException;
import com.example.git_pr_demo.appointment.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public Appointment createAppointment(CreateAppointmentRequest request) {
        if (request.getAppointmentDate().isBefore(LocalDateTime.now())) {
            throw new PastAppointmentDateException();
        }

        Appointment appointment = new Appointment(
                request.getPatientId(),
                request.getDoctorId(),
                request.getAppointmentDate(),
                AppointmentStatus.SCHEDULED
        );

        return appointmentRepository.save(appointment);
    }

    public Appointment getAppointment(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(id));
    }

    public List<Appointment> listAppointments() {
        return appointmentRepository.findAll();
    }

    public Appointment cancelAppointment(Long id) {
        Appointment appointment = getAppointment(id);

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new AppointmentAlreadyCancelledException(id);
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        return appointmentRepository.save(appointment);
    }
}
//something is added here
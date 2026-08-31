package com.example.git_pr_demo.appointment.controller;

import com.example.git_pr_demo.appointment.dto.AppointmentResponse;
import com.example.git_pr_demo.appointment.dto.CreateAppointmentRequest;
import com.example.git_pr_demo.appointment.entity.Appointment;
import com.example.git_pr_demo.appointment.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<AppointmentResponse> createAppointment(@Valid @RequestBody CreateAppointmentRequest request,
                                                                   UriComponentsBuilder uriComponentsBuilder) {
        Appointment appointment = appointmentService.createAppointment(request);
        URI location = uriComponentsBuilder.path("/appointments/{id}").buildAndExpand(appointment.getId()).toUri();
        return ResponseEntity.created(location).body(AppointmentResponse.from(appointment));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponse> getAppointment(@PathVariable Long id) {
        Appointment appointment = appointmentService.getAppointment(id);
        return ResponseEntity.ok(AppointmentResponse.from(appointment));
    }

    @GetMapping
    public ResponseEntity<List<AppointmentResponse>> listAppointments() {
        List<AppointmentResponse> appointments = appointmentService.listAppointments()
                .stream()
                .map(AppointmentResponse::from)
                .toList();
        return ResponseEntity.ok(appointments);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<AppointmentResponse> cancelAppointment(@PathVariable Long id) {
        Appointment appointment = appointmentService.cancelAppointment(id);
        return ResponseEntity.ok(AppointmentResponse.from(appointment));
    }
}

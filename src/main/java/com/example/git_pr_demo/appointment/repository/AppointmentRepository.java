package com.example.git_pr_demo.appointment.repository;

import com.example.git_pr_demo.appointment.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}

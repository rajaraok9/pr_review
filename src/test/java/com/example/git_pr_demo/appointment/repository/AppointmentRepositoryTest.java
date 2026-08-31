package com.example.git_pr_demo.appointment.repository;

import com.example.git_pr_demo.appointment.entity.Appointment;
import com.example.git_pr_demo.appointment.entity.AppointmentStatus;
import org.junit.jupiter.api.Test;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AppointmentRepositoryTest {

    @org.springframework.beans.factory.annotation.Autowired
    private AppointmentRepository appointmentRepository;

    @Test
    void savesAndRetrievesAppointment() {
        Appointment appointment = new Appointment("patient-1", "doctor-1", LocalDateTime.now().plusDays(1), AppointmentStatus.SCHEDULED);

        Appointment saved = appointmentRepository.save(appointment);

        assertThat(saved.getId()).isNotNull();
        assertThat(appointmentRepository.findById(saved.getId())).isPresent();
    }
}

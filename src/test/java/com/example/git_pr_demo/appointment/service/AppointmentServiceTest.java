package com.example.git_pr_demo.appointment.service;

import com.example.git_pr_demo.appointment.dto.CreateAppointmentRequest;
import com.example.git_pr_demo.appointment.entity.Appointment;
import com.example.git_pr_demo.appointment.entity.AppointmentStatus;
import com.example.git_pr_demo.appointment.exception.AppointmentAlreadyCancelledException;
import com.example.git_pr_demo.appointment.exception.AppointmentNotFoundException;
import com.example.git_pr_demo.appointment.exception.PastAppointmentDateException;
import com.example.git_pr_demo.appointment.repository.AppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    private AppointmentService appointmentService;

    @BeforeEach
    void setUp() {
        appointmentService = new AppointmentService(appointmentRepository);
    }

    @Test
    void createAppointment_savesScheduledAppointment() {
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);
        CreateAppointmentRequest request = new CreateAppointmentRequest("patient-1", "doctor-1", futureDate);

        when(appointmentRepository.save(any(Appointment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Appointment result = appointmentService.createAppointment(request);

        assertThat(result.getPatientId()).isEqualTo("patient-1");
        assertThat(result.getDoctorId()).isEqualTo("doctor-1");
        assertThat(result.getAppointmentDate()).isEqualTo(futureDate);
        assertThat(result.getStatus()).isEqualTo(AppointmentStatus.SCHEDULED);

        ArgumentCaptor<Appointment> captor = ArgumentCaptor.forClass(Appointment.class);
        verify(appointmentRepository).save(captor.capture());
        assertThat(captor.getValue().getStatus()).isEqualTo(AppointmentStatus.SCHEDULED);
    }

    @Test
    void createAppointment_rejectsPastDate() {
        LocalDateTime pastDate = LocalDateTime.now().minusDays(1);
        CreateAppointmentRequest request = new CreateAppointmentRequest("patient-1", "doctor-1", pastDate);

        assertThatThrownBy(() -> appointmentService.createAppointment(request))
                .isInstanceOf(PastAppointmentDateException.class);

        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void getAppointment_returnsAppointmentWhenFound() {
        Appointment appointment = new Appointment("patient-1", "doctor-1", LocalDateTime.now().plusDays(1), AppointmentStatus.SCHEDULED);
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        Appointment result = appointmentService.getAppointment(1L);

        assertThat(result).isEqualTo(appointment);
    }

    @Test
    void getAppointment_throwsWhenNotFound() {
        when(appointmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> appointmentService.getAppointment(1L))
                .isInstanceOf(AppointmentNotFoundException.class);
    }

    @Test
    void listAppointments_returnsAllAppointments() {
        Appointment appointment = new Appointment("patient-1", "doctor-1", LocalDateTime.now().plusDays(1), AppointmentStatus.SCHEDULED);
        when(appointmentRepository.findAll()).thenReturn(List.of(appointment));

        List<Appointment> result = appointmentService.listAppointments();

        assertThat(result).containsExactly(appointment);
    }

    @Test
    void cancelAppointment_cancelsScheduledAppointment() {
        Appointment appointment = new Appointment("patient-1", "doctor-1", LocalDateTime.now().plusDays(1), AppointmentStatus.SCHEDULED);
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Appointment result = appointmentService.cancelAppointment(1L);

        assertThat(result.getStatus()).isEqualTo(AppointmentStatus.CANCELLED);
    }

    @Test
    void cancelAppointment_throwsWhenNotFound() {
        when(appointmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> appointmentService.cancelAppointment(1L))
                .isInstanceOf(AppointmentNotFoundException.class);
    }

    @Test
    void cancelAppointment_throwsWhenAlreadyCancelled() {
        Appointment appointment = new Appointment("patient-1", "doctor-1", LocalDateTime.now().plusDays(1), AppointmentStatus.CANCELLED);
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        assertThatThrownBy(() -> appointmentService.cancelAppointment(1L))
                .isInstanceOf(AppointmentAlreadyCancelledException.class);

        verify(appointmentRepository, never()).save(any());
    }
}

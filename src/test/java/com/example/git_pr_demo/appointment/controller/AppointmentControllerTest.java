package com.example.git_pr_demo.appointment.controller;

import com.example.git_pr_demo.appointment.entity.Appointment;
import com.example.git_pr_demo.appointment.entity.AppointmentStatus;
import com.example.git_pr_demo.appointment.exception.AppointmentAlreadyCancelledException;
import com.example.git_pr_demo.appointment.exception.AppointmentNotFoundException;
import com.example.git_pr_demo.appointment.exception.PastAppointmentDateException;
import com.example.git_pr_demo.appointment.service.AppointmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AppointmentController.class)
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AppointmentService appointmentService;

    @Test
    void createAppointment_returns201() throws Exception {
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);
        Appointment appointment = new Appointment("patient-1", "doctor-1", futureDate, AppointmentStatus.SCHEDULED);
        setId(appointment, 1L);

        when(appointmentService.createAppointment(any())).thenReturn(appointment);

        String body = """
                {"patientId":"patient-1","doctorId":"doctor-1","appointmentDate":"%s"}
                """.formatted(futureDate);

        mockMvc.perform(post("/appointments")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.patientId").value("patient-1"))
                .andExpect(jsonPath("$.status").value("SCHEDULED"));
    }

    @Test
    void createAppointment_missingPatientId_returns400() throws Exception {
        String body = """
                {"doctorId":"doctor-1","appointmentDate":"%s"}
                """.formatted(LocalDateTime.now().plusDays(1));

        mockMvc.perform(post("/appointments")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createAppointment_pastDate_returns400() throws Exception {
        when(appointmentService.createAppointment(any())).thenThrow(new PastAppointmentDateException());

        String body = """
                {"patientId":"patient-1","doctorId":"doctor-1","appointmentDate":"%s"}
                """.formatted(LocalDateTime.now().minusDays(1));

        mockMvc.perform(post("/appointments")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAppointment_returns200() throws Exception {
        Appointment appointment = new Appointment("patient-1", "doctor-1", LocalDateTime.now().plusDays(1), AppointmentStatus.SCHEDULED);
        setId(appointment, 1L);
        when(appointmentService.getAppointment(1L)).thenReturn(appointment);

        mockMvc.perform(get("/appointments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getAppointment_notFound_returns404() throws Exception {
        when(appointmentService.getAppointment(1L)).thenThrow(new AppointmentNotFoundException(1L));

        mockMvc.perform(get("/appointments/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void listAppointments_returns200() throws Exception {
        Appointment appointment = new Appointment("patient-1", "doctor-1", LocalDateTime.now().plusDays(1), AppointmentStatus.SCHEDULED);
        setId(appointment, 1L);
        when(appointmentService.listAppointments()).thenReturn(List.of(appointment));

        mockMvc.perform(get("/appointments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void cancelAppointment_returns200() throws Exception {
        Appointment appointment = new Appointment("patient-1", "doctor-1", LocalDateTime.now().plusDays(1), AppointmentStatus.CANCELLED);
        setId(appointment, 1L);
        when(appointmentService.cancelAppointment(1L)).thenReturn(appointment);

        mockMvc.perform(post("/appointments/1/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    @Test
    void cancelAppointment_alreadyCancelled_returns409() throws Exception {
        when(appointmentService.cancelAppointment(1L)).thenThrow(new AppointmentAlreadyCancelledException(1L));

        mockMvc.perform(post("/appointments/1/cancel"))
                .andExpect(status().isConflict());
    }

    private void setId(Appointment appointment, Long id) throws Exception {
        var field = Appointment.class.getDeclaredField("id");
        field.setAccessible(true);
        field.set(appointment, id);
    }
}

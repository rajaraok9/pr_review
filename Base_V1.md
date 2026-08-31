We need to build an Appointment Management Service.

Requirements:

1. Create an appointment
2. Retrieve an appointment by ID
3. List appointments
4. Cancel an appointment

Appointment contains:

- id
- patientId
- doctorId
- appointmentDate
- status

Statuses:
SCHEDULED
CANCELLED

Rules:
- patientId is mandatory
- doctorId is mandatory
- appointmentDate is mandatory
- appointmentDate cannot be in the past
- newly created appointments are SCHEDULED
- cancelled appointments cannot be cancelled again

Technical requirements:
- Java
- Spring Boot
- REST API
- JPA
- H2
- JUnit 5
- Maven

First understand the requirements.
Create an implementation plan.
Do not write code yet.
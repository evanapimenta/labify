package com.fatec.labify.api.dto.patient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fatec.labify.domain.Patient;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreatePatientResponseDTO {

    private String id;
    private LocalDateTime createdAt;

    public CreatePatientResponseDTO(Patient patient) {
        this.id = patient.getUser().getId();
        this.createdAt = patient.getUser().getCreatedAt();
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "CreatePatientResponseDTO{" +
                "id='" + id + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}

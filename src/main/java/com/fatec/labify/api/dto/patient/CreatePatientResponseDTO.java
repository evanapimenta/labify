package com.fatec.labify.api.dto.patient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fatec.labify.domain.Patient;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreatePatientResponseDTO {

    private String id;
    private LocalDateTime createdAt;

    public CreatePatientResponseDTO(Patient patient) {
        this.createdAt = patient.getCreatedAt();
    }

    public String getId() {
        return id;
    }

    public CreatePatientResponseDTO setId(String id) {
        this.id = id;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public CreatePatientResponseDTO setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    @Override
    public String toString() {
        return "CreatePatientResponseDTO{" +
                "id='" + id + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}

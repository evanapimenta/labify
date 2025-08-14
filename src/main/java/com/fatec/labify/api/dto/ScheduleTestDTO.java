package com.fatec.labify.api.dto;

import com.fatec.labify.domain.TestStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class ScheduleTestDTO {
    @NotBlank(message = "Exame não pode estar vazio")
    private Long testId;
    @NotBlank(message = "Paciente não pode estar vazio")
    private String patientId;
    @NotNull(message = "Exame deve ter uma data de realização")
    private LocalDateTime scheduledFor;
    private LocalDateTime scheduledAt;
    @NotBlank(message = "Exame deve ter estar vinculado a um laboratório")
    private String branchId;
}

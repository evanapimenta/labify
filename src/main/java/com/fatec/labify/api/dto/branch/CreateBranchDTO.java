package com.fatec.labify.api.dto.branch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fatec.labify.api.dto.patient.AddressDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateBranchDTO {
    @NotBlank(message = "O nome não pode estar em branco")
    private String name;

    private AddressDTO addressDTO;

    @Pattern(regexp = "\\d{10,11}", message = "Número de telefone inválido")
    private String phoneNumber;

    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "Horário de funcionamento não pode estar em branco")
    private String openingHours;

    @NotBlank(message = "Informe o id do laboratório")
    private String laboratoryId;
}

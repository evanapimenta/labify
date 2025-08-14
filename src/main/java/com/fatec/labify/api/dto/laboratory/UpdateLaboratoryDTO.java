package com.fatec.labify.api.dto.laboratory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fatec.labify.api.dto.patient.AddressDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateLaboratoryDTO {

    private String name;

    @Pattern(regexp = "\\d{10,11}", message = "Número de telefone inválido")
    private String phoneNumber;

    @Email(message = "Email inválido")
    private String email;

    private AddressDTO addressDTO;

}

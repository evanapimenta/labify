package com.fatec.labify.api.dto.laboratory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fatec.labify.api.dto.patient.AddressDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateLaboratoryDTO {

    private String name;

    @Pattern(regexp = "\\d{10,11}", message = "Número de telefone inválido")
    private String phoneNumber;

    @Email(message = "Email inválido")
    private String email;

    private AddressDTO addressDTO;

    public String getName() {
        return name;
    }

    public UpdateLaboratoryDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public UpdateLaboratoryDTO setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UpdateLaboratoryDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public AddressDTO getAddressDTO() {
        return addressDTO;
    }

    public UpdateLaboratoryDTO setAddressDTO(AddressDTO addressDTO) {
        this.addressDTO = addressDTO;
        return this;
    }
}

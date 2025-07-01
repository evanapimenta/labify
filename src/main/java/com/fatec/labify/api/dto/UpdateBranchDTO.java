package com.fatec.labify.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fatec.labify.api.dto.patient.AddressDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateBranchDTO {
    private String name;

    private AddressDTO addressDTO;

    @Pattern(regexp = "\\d{10,11}", message = "Número de telefone inválido")
    private String phoneNumber;

    @Email(message = "Email inválido")
    private String email;

    private String openingHours;


    public String getName() {
        return name;
    }

    public UpdateBranchDTO setName(String name) {
        this.name = name;
        return this;
    }

    public AddressDTO getAddressDTO() {
        return addressDTO;
    }

    public UpdateBranchDTO setAddressDTO(AddressDTO addressDTO) {
        this.addressDTO = addressDTO;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public UpdateBranchDTO setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UpdateBranchDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public UpdateBranchDTO setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
        return this;
    }
}

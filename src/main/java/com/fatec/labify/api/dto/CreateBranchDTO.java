package com.fatec.labify.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fatec.labify.api.dto.patient.AddressDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

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

    public String getName() {
        return name;
    }

    public CreateBranchDTO setName(String name) {
        this.name = name;
        return this;
    }

    public AddressDTO getAddressDTO() {
        return addressDTO;
    }

    public CreateBranchDTO setAddressDTO(AddressDTO addressDTO) {
        this.addressDTO = addressDTO;
        return this;
    }

    public String getLaboratoryId() {
        return laboratoryId;
    }

    public CreateBranchDTO setLaboratoryId(String laboratoryId) {
        this.laboratoryId = laboratoryId;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public CreateBranchDTO setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public CreateBranchDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public CreateBranchDTO setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
        return this;
    }
}

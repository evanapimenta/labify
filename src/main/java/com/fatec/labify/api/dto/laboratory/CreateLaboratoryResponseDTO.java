package com.fatec.labify.api.dto.laboratory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fatec.labify.api.dto.patient.AddressDTO;
import com.fatec.labify.domain.Laboratory;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)

public class CreateLaboratoryResponseDTO {
    private String id;

    @NotBlank(message = "Nome não pode estar vazio")
    private String name;

    @NotBlank(message = "CNPJ não pode estar vazio")
    private String cnpj;

    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "Número de telefone é obrigatório")
    private String phoneNumber;

    private AddressDTO address;

    public CreateLaboratoryResponseDTO(Laboratory laboratory) {
        this.id = laboratory.getId();
        this.address = laboratory.getAddress() != null ? new AddressDTO(laboratory.getAddress()) : null;
        this.name = laboratory.getName();
        this.cnpj = laboratory.getCnpj();
        this.email = laboratory.getEmail();
        this.phoneNumber = laboratory.getPhoneNumber();
    }

    public CreateLaboratoryResponseDTO() {}

    public String getId() {
        return id;
    }

    public CreateLaboratoryResponseDTO setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public CreateLaboratoryResponseDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getCnpj() {
        return cnpj;
    }

    public CreateLaboratoryResponseDTO setCnpj(String cnpj) {
        this.cnpj = cnpj;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public CreateLaboratoryResponseDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public CreateLaboratoryResponseDTO setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public CreateLaboratoryResponseDTO setAddress(AddressDTO address) {
        this.address = address;
        return this;
    }
}

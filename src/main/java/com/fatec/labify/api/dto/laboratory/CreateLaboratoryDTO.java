package com.fatec.labify.api.dto.laboratory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fatec.labify.api.dto.patient.AddressDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.br.CNPJ;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateLaboratoryDTO {

    @NotBlank(message = "Nome do laboratório é obrigatório")
    private String name;

    private AddressDTO addressDTO;

    @Pattern(regexp = "\\d{10,11}", message = "Número de telefone inválido")
    @NotBlank(message = "Telefone da matriz é obrigatório")
    private String phoneNumber;

    @Email(message = "Email inválido")
    @NotBlank(message = "Email do laboratório é obrigatório")
    private String email;

    @CNPJ
    private String cnpj;

    public String getName() {
        return name;
    }

    public CreateLaboratoryDTO setName(String name) {
        this.name = name;
        return this;
    }

    public AddressDTO getAddressDTO() {
        return addressDTO;
    }

    public CreateLaboratoryDTO setAddressDTO(AddressDTO addressDTO) {
        this.addressDTO = addressDTO;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public CreateLaboratoryDTO setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public CreateLaboratoryDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getCnpj() {
        return cnpj;
    }

    public CreateLaboratoryDTO setCnpj(String cnpj) {
        this.cnpj = cnpj;
        return this;
    }
}

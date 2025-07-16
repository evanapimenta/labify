package com.fatec.labify.api.dto.patient;

import com.fatec.labify.domain.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CreatePatientDTO {

    @CPF(message = "CPF inválido")
    @NotBlank(message = "O cpf não pode estar em branco")
    private String cpf;

    @Pattern(regexp = "\\d{10,11}", message = "Número de telefone inválido")
    private String phoneNumber;

    private String insuranceName;

    @NotNull
    private Gender gender;

    @NotNull
    private BigDecimal weight;

    @Past
    @NotNull(message = "A data de nascimento é obrigatória")
    private LocalDate birthDate;

    @NotBlank(message = "Nome de contato de emergência é obrigatório")
    private String emergencyContactName;

    @NotBlank(message = "Número de contato de emergência é obrigatório")
    private String emergencyContactNumber;

    private AddressDTO addressDTO;

    public String getCpf() {
        return cpf;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getInsuranceName() {
        return insuranceName;
    }

    public Gender getGender() {
        return gender;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public String getEmergencyContactName() {
        return emergencyContactName;
    }

    public String getEmergencyContactNumber() {
        return emergencyContactNumber;
    }

    public AddressDTO getAddressDTO() {
        return addressDTO;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }
}

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

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getInsuranceName() {
        return insuranceName;
    }

    public CreatePatientDTO setInsuranceName(String insuranceName) {
        this.insuranceName = insuranceName;
        return this;
    }

    public Gender getGender() {
        return gender;
    }

    public CreatePatientDTO setGender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public CreatePatientDTO setWeight(BigDecimal weight) {
        this.weight = weight;
        return this;
    }

    public String getEmergencyContactName() {
        return emergencyContactName;
    }

    public CreatePatientDTO setEmergencyContactName(String emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
        return this;
    }

    public String getEmergencyContactNumber() {
        return emergencyContactNumber;
    }

    public CreatePatientDTO setEmergencyContactNumber(String emergencyContactNumber) {
        this.emergencyContactNumber = emergencyContactNumber;
        return this;
    }

    public AddressDTO getAddressDTO() {
        return addressDTO;
    }

    public CreatePatientDTO setAddressDTO(AddressDTO addressDTO) {
        this.addressDTO = addressDTO;
        return this;
    }

    //
//    public String getStreet() {
//        return street;
//    }
//
//    public CreatePatientDTO setStreet(String street) {
//        this.street = street;
//        return this;
//    }
//
//    public String getNumber() {
//        return number;
//    }
//
//    public CreatePatientDTO setNumber(String number) {
//        this.number = number;
//        return this;
//    }
//
//    public String getComplement() {
//        return complement;
//    }
//
//    public CreatePatientDTO setComplement(String complement) {
//        this.complement = complement;
//        return this;
//    }
//
//    public String getNeighborhood() {
//        return neighborhood;
//    }
//
//    public CreatePatientDTO setNeighborhood(String neighborhood) {
//        this.neighborhood = neighborhood;
//        return this;
//    }
//
//    public String getCity() {
//        return city;
//    }
//
//    public CreatePatientDTO setCity(String city) {
//        this.city = city;
//        return this;
//    }
//
//    public String getState() {
//        return state;
//    }
//
//    public CreatePatientDTO setState(String state) {
//        this.state = state;
//        return this;
//    }
//
//    public String getCountry() {
//        return country;
//    }
//
//    public CreatePatientDTO setCountry(String country) {
//        this.country = country;
//        return this;
//    }
//
//    public String getZipCode() {
//        return zipCode;
//    }
//
//    public CreatePatientDTO setZipCode(String zipCode) {
//        this.zipCode = zipCode;
//        return this;
//    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public CreatePatientDTO setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }
}

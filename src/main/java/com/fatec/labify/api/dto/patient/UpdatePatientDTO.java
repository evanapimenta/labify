package com.fatec.labify.api.dto.patient;

import com.fatec.labify.domain.Gender;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public class UpdatePatientDTO {

    @Pattern(regexp = "\\d{10,11}", message = "Número de telefone inválido")
    private String phoneNumber;

    private String insuranceName;

    private Gender gender;

    private BigDecimal weight;

    private String emergencyContactName;

    private String emergencyContactNumber;

    private AddressDTO addressDTO;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public UpdatePatientDTO setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getInsuranceName() {
        return insuranceName;
    }

    public UpdatePatientDTO setInsuranceName(String insuranceName) {
        this.insuranceName = insuranceName;
        return this;
    }

    public Gender getGender() {
        return gender;
    }

    public UpdatePatientDTO setGender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public UpdatePatientDTO setWeight(BigDecimal weight) {
        this.weight = weight;
        return this;
    }

    public String getEmergencyContactName() {
        return emergencyContactName;
    }

    public UpdatePatientDTO setEmergencyContactName(String emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
        return this;
    }

    public String getEmergencyContactNumber() {
        return emergencyContactNumber;
    }

    public UpdatePatientDTO setEmergencyContactNumber(String emergencyContactNumber) {
        this.emergencyContactNumber = emergencyContactNumber;
        return this;
    }

    public com.fatec.labify.api.dto.patient.AddressDTO getAddressDTO() {
        return addressDTO;
    }

    public UpdatePatientDTO setAddressDTO(AddressDTO addressDTO) {
        this.addressDTO = addressDTO;
        return this;
    }
}

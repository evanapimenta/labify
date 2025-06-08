package com.fatec.labify.api.dto.patient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fatec.labify.api.dto.user.UserInfoDTO;
import com.fatec.labify.domain.Address;
import com.fatec.labify.domain.Gender;
import com.fatec.labify.domain.Patient;

import java.math.BigDecimal;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PatientResponseDTO {
    private String id;
    private String phoneNumber;
    private String cpf;
    private UserInfoDTO userInfoDTO;
    private Gender gender;
    private BigDecimal weight;
    private String emergencyContactName;
    private String emergencyContactNumber;
    private String insuranceName;
    private boolean insured;
    private LocalDate birthDate;
    private Address address;

    public PatientResponseDTO() {}

    public PatientResponseDTO(Patient patient) {
        this.id = patient.getId();
        this.cpf = patient.getCpf();
        this.phoneNumber = patient.getPhoneNumber();
        this.userInfoDTO = new UserInfoDTO(patient.getUser());
        this.gender = patient.getGender();
        this.address = patient.getAddress();
        this.weight = patient.getWeight();
        this.emergencyContactName = patient.getEmergencyContactName();
        this.emergencyContactNumber = patient.getEmergencyContactNumber();
        this.insured = patient.isInsured();
        this.insuranceName = patient.getInsuranceName();
        this.birthDate = patient.getBirthDate();
    }

    public String getId() {
        return id;
    }

    public PatientResponseDTO setId(String id) {
        this.id = id;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public PatientResponseDTO setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getCpf() {
        return cpf;
    }

    public PatientResponseDTO setCpf(String cpf) {
        this.cpf = cpf;
        return this;
    }

    public Gender getGender() {
        return gender;
    }

    public PatientResponseDTO setGender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public PatientResponseDTO setWeight(BigDecimal weight) {
        this.weight = weight;
        return this;
    }

    public String getEmergencyContactName() {
        return emergencyContactName;
    }

    public PatientResponseDTO setEmergencyContactName(String emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
        return this;
    }

    public String getEmergencyContactNumber() {
        return emergencyContactNumber;
    }

    public PatientResponseDTO setEmergencyContactNumber(String emergencyContactNumber) {
        this.emergencyContactNumber = emergencyContactNumber;
        return this;
    }

    public String getInsuranceName() {
        return (insuranceName != null && insuranceName.trim().isEmpty()) ? null : insuranceName;
    }

    public PatientResponseDTO setInsuranceName(String insuranceName) {
        this.insuranceName = insuranceName;
        return this;
    }

    public UserInfoDTO getUserInfoDTO() {
        return userInfoDTO;
    }

    public PatientResponseDTO setUserInfoDTO(UserInfoDTO userInfoDTO) {
        this.userInfoDTO = userInfoDTO;
        return this;
    }

    public boolean isInsured() {
        return insured;
    }

    public PatientResponseDTO setInsured(boolean insured) {
        this.insured = insured;
        return this;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public PatientResponseDTO setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public Address getAddress() {
        return address;
    }

    public PatientResponseDTO setAddress(Address address) {
        this.address = address;
        return this;
    }

    @Override
    public String toString() {
        return "PatientResponseDTO{" +
                "id='" + id + '\'' +
                ", gender=" + gender +
                ", weight=" + weight +
                ", emergencyContactName='" + emergencyContactName + '\'' +
                ", getEmergencyContactNumber='" + emergencyContactNumber + '\'' +
                ", insured=" + insured +
                ", birthDate=" + birthDate +
                ", address=" + address +
                '}';
    }
}
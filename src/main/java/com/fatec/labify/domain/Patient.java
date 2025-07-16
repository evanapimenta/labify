package com.fatec.labify.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "patients")
public class Patient {
    @Id
    private String id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    @JsonBackReference
    private User user;

    private LocalDate birthDate;

    private String cpf;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(precision = 5, scale = 2)
    private BigDecimal weight;

    @Embedded
    private Address address;

    private boolean insured;

    private String insuranceName;

    private String emergencyContactName;

    private String emergencyContactNumber;

    private LocalDateTime createdAt;

    public Patient() {}

    public Patient(String cpf, String insuranceName, Gender gender, String phoneNumber, BigDecimal weight, String emergencyContactName, String emergencyContactNumber, boolean insured, LocalDate birthDate, Address address, User user) {
        this.cpf = cpf;
        this.insuranceName = insuranceName;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.weight = weight;
        this.emergencyContactName = emergencyContactName;
        this.emergencyContactNumber = emergencyContactNumber;
        this.insured = insured;
        this.birthDate = birthDate;
        this.address = address;
        this.user = user;
        this.createdAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public Patient setId(String id) {
        this.id = id;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Patient setUser(User user) {
        this.user = user;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Patient setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getCpf() {
        return cpf;
    }

    public Patient setCpf(String cpf) {
        this.cpf = cpf;
        return this;
    }

    public boolean isInsured() {
        return insured;
    }

    public Patient setInsured(boolean insured) {
        this.insured = insured;
        return this;
    }

    public String getInsuranceName() {
        return insuranceName;
    }

    public Patient setInsuranceName(String insuranceName) {
        this.insuranceName = insuranceName;
        return this;
    }

    public Gender getGender() {
        return gender;
    }

    public Patient setGender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public Patient setWeight(BigDecimal weight) {
        this.weight = weight;
        return this;
    }

    public String getEmergencyContactName() {
        return emergencyContactName;
    }

    public Patient setEmergencyContactName(String emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
        return this;
    }

    public String getEmergencyContactNumber() {
        return emergencyContactNumber;
    }

    public Patient setEmergencyContactNumber(String emergencyContactNumber) {
        this.emergencyContactNumber = emergencyContactNumber;
        return this;
    }

    public Address getAddress() {
        return address;
    }

    public Patient setAddress(Address address) {
        this.address = address;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "user=" + user +
                ", birthDate=" + birthDate +
                ", gender=" + gender +
                ", weight=" + weight +
                ", address=" + address +
                ", insured=" + insured +
                ", insuranceName='" + insuranceName + '\'' +
                ", emergencyContactName='" + emergencyContactName + '\'' +
                ", emergencyContactNumber='" + emergencyContactNumber + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}

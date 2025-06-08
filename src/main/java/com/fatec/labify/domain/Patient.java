package com.fatec.labify.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

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

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

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

    public Patient setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
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

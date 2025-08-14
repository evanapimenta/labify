package com.fatec.labify.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
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
}

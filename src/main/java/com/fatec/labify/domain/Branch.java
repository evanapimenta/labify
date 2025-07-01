package com.fatec.labify.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "branches")
public class Branch {

    @Id
    private String id;

    private String name;

    @Embedded
    private Address address;

    private String phoneNumber;

    private String email;

    private String openingHours;

    @ManyToOne
    @JoinColumn(name = "laboratory_id", nullable = false)
    private Laboratory laboratory;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private User admin;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public String getId() {
        return id;
    }

    public Branch setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Branch setName(String name) {
        this.name = name;
        return this;
    }

    public Address getAddress() {
        return address;
    }

    public Branch setAddress(Address address) {
        this.address = address;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Branch setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Branch setEmail(String email) {
        this.email = email;
        return this;
    }

    public User getAdmin() {
        return admin;
    }

    public Branch setAdmin(User admin) {
        this.admin = admin;
        return this;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public Branch setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
        return this;
    }

    public Laboratory getLaboratory() {
        return laboratory;
    }

    public Branch setLaboratory(Laboratory laboratory) {
        this.laboratory = laboratory;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Branch setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Branch setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}
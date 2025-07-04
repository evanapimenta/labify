package com.fatec.labify.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "laboratories")
public class Laboratory {

    @Id
    private String id;

    private String name;

    @Embedded
    private Address address;

    private String phoneNumber;

    private boolean active;

    private String email;

    private String cnpj;

    @OneToMany(mappedBy = "laboratory")
    private List<Branch> branches;

    @ManyToOne
    @JoinColumn(name = "super_admin_id")
    private User superAdmin;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public String getId() {
        return id;
    }

    public Laboratory setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Laboratory setName(String name) {
        this.name = name;
        return this;
    }

    public Address getAddress() {
        return address;
    }

    public Laboratory setAddress(Address address) {
        this.address = address;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    public Laboratory setActive(boolean active) {
        this.active = active;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Laboratory setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public User getSuperAdmin() {
        return superAdmin;
    }

    public Laboratory setSuperAdmin(User superAdmin) {
        this.superAdmin = superAdmin;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Laboratory setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getCnpj() {
        return cnpj;
    }

    public Laboratory setCnpj(String cnpj) {
        this.cnpj = cnpj;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Laboratory setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Laboratory setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public List<Branch> getBranches() {
        return branches;
    }

    public Laboratory setBranches(List<Branch> branches) {
        this.branches = branches;
        return this;
    }
}

package com.fatec.labify.api.dto.branch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fatec.labify.api.dto.patient.AddressDTO;
import com.fatec.labify.domain.Branch;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BranchDTO {
    private String id;
    private String name;
    private AddressDTO address;
    private String phoneNumber;
    private String email;
    private String openingHours;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BranchDTO(Branch branch) {
        this.id = branch.getId();
        this.name = branch.getName();
        this.email = branch.getEmail();
        this.phoneNumber = branch.getPhoneNumber();
        this.address = branch.getAddress() != null ? new AddressDTO(branch.getAddress()) : null;
        this.createdAt = branch.getCreatedAt();
        this.updatedAt = branch.getUpdatedAt();
    }

    public BranchDTO(String id, String name, AddressDTO address, String phoneNumber,
                     String email, String openingHours, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.openingHours = openingHours;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

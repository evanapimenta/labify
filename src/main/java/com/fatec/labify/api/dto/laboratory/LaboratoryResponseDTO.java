package com.fatec.labify.api.dto.laboratory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fatec.labify.api.dto.branch.BranchDTO;
import com.fatec.labify.api.dto.patient.AddressDTO;
import com.fatec.labify.domain.Address;
import com.fatec.labify.domain.Laboratory;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LaboratoryResponseDTO {
    private String id;
    private String name;
    private AddressDTO address;
    private String cnpj;
    private String phoneNumber;
    private String email;
    private String createdAt;
    private String updatedAt;
    private List<BranchDTO> branches;

    public LaboratoryResponseDTO(Laboratory lab) {
        this.id = lab.getId();
        this.name = lab.getName();
        this.address = lab.getAddress() != null ? new AddressDTO(lab.getAddress()) : null;
        this.cnpj = lab.getCnpj();
        this.phoneNumber = lab.getPhoneNumber();
        this.email = lab.getEmail();
        this.createdAt = lab.getCreatedAt() != null ? lab.getCreatedAt().toString() : null;
        this.updatedAt = lab.getUpdatedAt() != null ? lab.getUpdatedAt().toString() : null;
        this.branches = lab.getBranches() != null ? lab.getBranches().stream().map(BranchDTO::new).toList() : null;
    }

    public LaboratoryResponseDTO(String id, String name, AddressDTO address, String cnpj, String phoneNumber,
                                 String email, String createdAt, String updatedAt, List<BranchDTO> branches) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.cnpj = cnpj;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.branches = branches;
    }

    public String getId() {
        return id;
    }

    public LaboratoryResponseDTO setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public LaboratoryResponseDTO setName(String name) {
        this.name = name;
        return this;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public LaboratoryResponseDTO setAddress(AddressDTO address) {
        this.address = address;
        return this;
    }

    public String getCnpj() {
        return cnpj;
    }

    public LaboratoryResponseDTO setCnpj(String cnpj) {
        this.cnpj = cnpj;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LaboratoryResponseDTO setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public LaboratoryResponseDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public LaboratoryResponseDTO setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public LaboratoryResponseDTO setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public List<BranchDTO> getBranches() {
        return branches;
    }

    public LaboratoryResponseDTO setBranches(List<BranchDTO> branches) {
        this.branches = branches;
        return this;
    }
}

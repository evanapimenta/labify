package com.fatec.labify.api.dto.laboratory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fatec.labify.api.dto.patient.AddressDTO;
import com.fatec.labify.domain.Laboratory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateLaboratoryResponseDTO {

    private String id;

    private String name;

    private String cnpj;

    private String email;

    private String phoneNumber;

    private AddressDTO address;

    public CreateLaboratoryResponseDTO(Laboratory laboratory) {
        this.id = laboratory.getId();
        this.address = laboratory.getAddress() != null ? new AddressDTO(laboratory.getAddress()) : null;
        this.name = laboratory.getName();
        this.cnpj = laboratory.getCnpj();
        this.email = laboratory.getEmail();
        this.phoneNumber = laboratory.getPhoneNumber();
    }

}

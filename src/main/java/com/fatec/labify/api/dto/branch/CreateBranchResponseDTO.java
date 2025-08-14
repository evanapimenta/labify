package com.fatec.labify.api.dto.branch;

import com.fatec.labify.api.dto.patient.AddressDTO;
import com.fatec.labify.domain.Branch;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class CreateBranchResponseDTO {
    private String id;
    private String name;
    private AddressDTO address;
    private String phoneNumber;
    private String email;
    private String openingHours;
    private String laboratoryId;
    private LocalDateTime createdAt;

    public CreateBranchResponseDTO(Branch branch) {
        this.id = branch.getId();
        this.name = branch.getName();
        this.address = branch.getAddress() != null ? new AddressDTO(branch.getAddress()) : null;
        this.phoneNumber = branch.getPhoneNumber();
        this.openingHours = branch.getOpeningHours();
        this.laboratoryId = branch.getLaboratory().getId();
        this.createdAt = branch.getCreatedAt();
    }
}

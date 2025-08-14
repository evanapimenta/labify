package com.fatec.labify.api.service;

import com.fatec.labify.api.dto.branch.CreateBranchDTO;
import com.fatec.labify.api.dto.branch.CreateBranchResponseDTO;
import com.fatec.labify.api.dto.branch.UpdateBranchDTO;
import com.fatec.labify.api.dto.branch.BranchResponseDTO;
import com.fatec.labify.domain.*;
import com.fatec.labify.exception.NotFoundException;
import com.fatec.labify.repository.BranchRepository;
import com.fatec.labify.repository.LaboratoryRepository;
import com.fatec.labify.repository.UserRepository;
import com.fatec.labify.util.AddressUtils;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BranchService {
    private final AccessControlService accessControlService;
    private final BranchRepository branchRepository;
    private final LaboratoryRepository laboratoryRepository;
    private final UserRepository userRepository;

    public BranchService(BranchRepository branchRepository,
                         LaboratoryRepository laboratoryRepository,
                         UserRepository userRepository,
                         AccessControlService accessControlService) {
        this.branchRepository = branchRepository;
        this.laboratoryRepository = laboratoryRepository;
        this.userRepository = userRepository;
        this.accessControlService = accessControlService;
    }

    public BranchResponseDTO findById(String username, String id) {
        User user =  userRepository.findByEmailIgnoreCase(username).orElseThrow(() -> new NotFoundException("Usuário", username));
        Branch branch = branchRepository.findById(id).orElseThrow(() -> new NotFoundException("Filial", id));
        accessControlService.canManageBranch(user, branch);
        return new BranchResponseDTO(branch);
    }

    public Page<BranchResponseDTO> findByLabId(String username, String id, Pageable pageable) {
        User user =  userRepository.findByEmailIgnoreCase(username).orElseThrow(() -> new NotFoundException("Usuário", username));
        Laboratory lab = laboratoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Laboratório", id));
        accessControlService.canManageLab(user, lab);
        return branchRepository.findBranchByLaboratory_Id(id, pageable).map(BranchResponseDTO::new);
    }

    @Transactional
    public CreateBranchResponseDTO create(CreateBranchDTO dto) {
        Laboratory lab = laboratoryRepository.findById(dto.getLaboratoryId()).orElseThrow(() -> new NotFoundException("Laboratório", dto.getLaboratoryId()));

        Address address = new Address(dto.getAddressDTO().getStreet(), dto.getAddressDTO().getNumber(),
                dto.getAddressDTO().getNeighborhood(), dto.getAddressDTO().getCity(), dto.getAddressDTO().getState(),
                dto.getAddressDTO().getZipCode(), dto.getAddressDTO().getCountry());
        Branch branch = new Branch(dto.getName(), dto.getEmail(), dto.getPhoneNumber(), dto.getOpeningHours(),
                lab, address);

        branchRepository.save(branch);
        return new CreateBranchResponseDTO(branch);
    }

    @Transactional
    public BranchResponseDTO update(String username, String id, UpdateBranchDTO dto) {
        User user =  userRepository.findByEmailIgnoreCase(username).orElseThrow(() -> new NotFoundException("Usuário", username));
        Branch branch = branchRepository.findById(id).orElseThrow(() -> new NotFoundException("Filial", id));
        accessControlService.validateCanManageBranch(user, branch);

        Optional.ofNullable(dto.getName()).ifPresent(branch::setName);
        Optional.ofNullable(dto.getPhoneNumber()).ifPresent(branch::setPhoneNumber);
        Optional.ofNullable(dto.getEmail()).ifPresent(branch::setEmail);
        Optional.ofNullable(dto.getOpeningHours()).ifPresent(branch::setOpeningHours);

        Optional.ofNullable(dto.getAddressDTO()).ifPresent(addressDTO -> {
            Address address = AddressUtils.updateAddress(branch.getAddress(), addressDTO);
            branch.setAddress(address);
        });

        branchRepository.save(branch);
        return new BranchResponseDTO(branch);
    }

    @Transactional
    public void activate(String username, String id) {
        User user =  userRepository.findByEmailIgnoreCase(username).orElseThrow(() -> new NotFoundException("Usuário", username));
        Branch branch = branchRepository.findById(id).orElseThrow(() -> new NotFoundException("Filial", id));
        accessControlService.validateCanManageLab(user, branch.getLaboratory());
        branch.activate();
        branchRepository.save(branch);
    }

    @Transactional
    public void delete(String id, String username) {
        User user =  userRepository.findByEmailIgnoreCase(username).orElseThrow(() -> new NotFoundException("Usuário", username));
        Branch branch = branchRepository.findById(id).orElseThrow(() -> new NotFoundException("Laboratório", id));
        accessControlService.validateCanManageLab(user, branch.getLaboratory());
        branch.deactivate();
        branchRepository.save(branch);
    }

}

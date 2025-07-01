package com.fatec.labify.api.service;

import com.fatec.labify.api.dto.CreateBranchDTO;
import com.fatec.labify.api.dto.UpdateBranchDTO;
import com.fatec.labify.api.dto.branch.BranchDTO;
import com.fatec.labify.api.dto.patient.AddressDTO;
import com.fatec.labify.domain.Address;
import com.fatec.labify.domain.Branch;
import com.fatec.labify.domain.Laboratory;
import com.fatec.labify.exception.BranchNotFoundException;
import com.fatec.labify.exception.ForbiddenOperationException;
import com.fatec.labify.exception.LaboratoryNotFoundException;
import com.fatec.labify.repository.BranchRepository;
import com.fatec.labify.repository.LaboratoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BranchService {
    private final BranchRepository branchRepository;
    private final UserRoleService userRoleService;
    private final LaboratoryRepository laboratoryRepository;

    public BranchService(BranchRepository branchRepository,
                         UserRoleService userRoleService,
                         LaboratoryRepository laboratoryRepository) {
        this.branchRepository = branchRepository;
        this.userRoleService = userRoleService;
        this.laboratoryRepository = laboratoryRepository;
    }

    public BranchDTO findById(String username, String id) {
        if (userRoleService.hasBranchAccess(username, id)) {
            throw new ForbiddenOperationException("Acesso não autorizado");
        }

        return branchRepository.findById(id).map(BranchDTO::new).orElseThrow(() -> new BranchNotFoundException(id));

    }

    public Page<BranchDTO> findAll(String username, Pageable pageable) {
        if (userRoleService.hasBranchAccess(username, null)) {
            throw new ForbiddenOperationException("Acesso não autorizado");
        }

        return branchRepository.findAll(pageable).map(BranchDTO::new);
    }

    @Transactional
    public Branch create(String username, CreateBranchDTO createBranchDTO) {
        if (userRoleService.hasBranchAccess(username, null)) {
            throw new ForbiddenOperationException("Acesso não autorizado");
        }

        Laboratory laboratory = laboratoryRepository.findById(createBranchDTO.getLaboratoryId()).orElseThrow(() ->
                new LaboratoryNotFoundException(createBranchDTO.getLaboratoryId()));

        Branch branch = new Branch()
                .setId(UUID.randomUUID().toString())
                .setName(createBranchDTO.getName())
                .setEmail(createBranchDTO.getEmail())
                .setLaboratory(laboratory)
                .setPhoneNumber(createBranchDTO.getPhoneNumber())
                .setOpeningHours(createBranchDTO.getOpeningHours())
                .setAddress(setCreateAddress(createBranchDTO));

        return branchRepository.save(branch);
    }

    @Transactional
    public void update(String username, String id, UpdateBranchDTO updateBranchDTO) {
        if (userRoleService.hasBranchAccess(username, null)) {
            throw new ForbiddenOperationException("Acesso não autorizado");
        }

        Branch branch = branchRepository.findById(id).orElseThrow(() -> new BranchNotFoundException(id));

        if (updateBranchDTO.getName() != null) branch.setName(updateBranchDTO.getName());
        if (updateBranchDTO.getPhoneNumber() != null) branch.setPhoneNumber(updateBranchDTO.getPhoneNumber());
        if (updateBranchDTO.getEmail() != null) branch.setEmail(updateBranchDTO.getEmail());
        if (updateBranchDTO.getOpeningHours() != null) branch.setOpeningHours(updateBranchDTO.getOpeningHours());

        if (updateBranchDTO.getAddressDTO() != null) {
            setUpdateAddress(branch, updateBranchDTO.getAddressDTO());
        }

        branchRepository.save(branch);
    }

    @Transactional
    public void delete(String username, String id) {
        if (userRoleService.hasBranchAccess(username, null)) {
            throw new ForbiddenOperationException("Acesso não autorizado");
        }

        Branch branch = branchRepository.findById(id).orElseThrow(() -> new BranchNotFoundException(id));
        branchRepository.delete(branch);
    }

    private void setUpdateAddress(Branch branch, AddressDTO dto) {
        Address address = branch.getAddress();

        if (dto.getStreet() != null) address.setStreet(dto.getStreet());
        if (dto.getNumber() != null) address.setNumber(dto.getNumber());
        if (dto.getComplement() != null) address.setComplement(dto.getComplement());
        if (dto.getNeighborhood() != null) address.setNeighborhood(dto.getNeighborhood());
        if (dto.getCity() != null) address.setCity(dto.getCity());
        if (dto.getState() != null) address.setState(dto.getState());
        if (dto.getCountry() != null) address.setCountry(dto.getCountry());
        if (dto.getZipCode() != null) address.setZipCode(dto.getZipCode());

        branch.setAddress(address);
    }

    private Address setCreateAddress(CreateBranchDTO createBranchDTO) {
        return new Address()
                .setStreet(createBranchDTO.getAddressDTO().getStreet())
                .setNumber(createBranchDTO.getAddressDTO().getNumber())
                .setComplement(createBranchDTO.getAddressDTO().getComplement())
                .setNeighborhood(createBranchDTO.getAddressDTO().getNeighborhood())
                .setCity(createBranchDTO.getAddressDTO().getCity())
                .setState(createBranchDTO.getAddressDTO().getState())
                .setZipCode(createBranchDTO.getAddressDTO().getZipCode())
                .setCountry(createBranchDTO.getAddressDTO().getCountry());
    }

}

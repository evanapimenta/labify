package com.fatec.labify.api.service;

import com.fatec.labify.api.dto.laboratory.CreateLaboratoryDTO;
import com.fatec.labify.api.dto.laboratory.CreateLaboratoryResponseDTO;
import com.fatec.labify.api.dto.laboratory.LaboratoryResponseDTO;
import com.fatec.labify.api.dto.laboratory.UpdateLaboratoryDTO;
import com.fatec.labify.domain.Address;
import com.fatec.labify.domain.Laboratory;
import com.fatec.labify.domain.User;
import com.fatec.labify.exception.AlreadyExistsException;
import com.fatec.labify.exception.NotFoundException;
import com.fatec.labify.repository.LaboratoryRepository;
import com.fatec.labify.repository.UserRepository;
import com.fatec.labify.util.AddressUtils;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LaboratoryService {
    private final UserRoleService userRoleService;
    private final AccessControlService accessControlService;
    private final LaboratoryRepository laboratoryRepository;
    private final UserRepository userRepository;

    public LaboratoryService(LaboratoryRepository laboratoryRepository, UserRoleService userRoleService, AccessControlService accessControlService, UserRepository userRepository) {
        this.laboratoryRepository = laboratoryRepository;
        this.userRoleService = userRoleService;
        this.accessControlService = accessControlService;
        this.userRepository = userRepository;
    }

    public Page<LaboratoryResponseDTO> findAll(Pageable pageable) {
        return laboratoryRepository.findAll(pageable).map(LaboratoryResponseDTO::new);
    }

    public LaboratoryResponseDTO findById(String id, String username) {
        User user =  userRepository.findByEmailIgnoreCase(username).orElseThrow(() -> new NotFoundException("Usuário", username));
        Laboratory lab = laboratoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Laboratório", id));
        accessControlService.validateCanManageLab(user, lab);
        return new LaboratoryResponseDTO(lab);
    }

    @Transactional
    public CreateLaboratoryResponseDTO create(CreateLaboratoryDTO dto) {
        validateCnpj(dto.getCnpj());

        Address address = new Address(dto.getAddressDTO().getStreet(), dto.getAddressDTO().getNumber(),
                dto.getAddressDTO().getNeighborhood(), dto.getAddressDTO().getCity(), dto.getAddressDTO().getState(),
                dto.getAddressDTO().getZipCode(), dto.getAddressDTO().getCountry());


        Laboratory lab = new Laboratory(dto.getName(), address, dto.getPhoneNumber(),
                dto.getEmail(), dto.getCnpj());

        laboratoryRepository.save(lab);
        return new CreateLaboratoryResponseDTO(lab);
    }

    @Transactional
    public LaboratoryResponseDTO update(String id, UpdateLaboratoryDTO dto, String username) {
        User user =  userRepository.findByEmailIgnoreCase(username).orElseThrow(() -> new NotFoundException("Usuário", username));
        Laboratory lab = laboratoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Laboratório", id));
        accessControlService.validateCanManageLab(user, lab);

        Optional.ofNullable(dto.getName()).ifPresent(lab::setName);
        Optional.ofNullable(dto.getPhoneNumber()).ifPresent(lab::setPhoneNumber);
        Optional.ofNullable(dto.getEmail()).ifPresent(lab::setEmail);

        Optional.ofNullable(dto.getAddressDTO()).ifPresent(addressDTO -> {
            Address address = AddressUtils.updateAddress(lab.getAddress(), addressDTO);
            lab.setAddress(address);
        });

        laboratoryRepository.save(lab);
        return new LaboratoryResponseDTO(lab);
    }

    @Transactional
    public void activate(String id, String username) {
        User user =  userRepository.findByEmailIgnoreCase(username).orElseThrow(() -> new NotFoundException("Usuário", username));
        Laboratory lab = laboratoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Laboratório", id));
        accessControlService.validateCanManageLab(user, lab);
        lab.activate();
        laboratoryRepository.save(lab);
    }

    @Transactional
    public void deactivate(String id, String username) {
        User user =  userRepository.findByEmailIgnoreCase(username).orElseThrow(() -> new NotFoundException("Usuário", username));
        Laboratory lab = laboratoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Laboratório", id));
        accessControlService.validateCanManageLab(user, lab);
        lab.deactivate();
        laboratoryRepository.save(lab);
    }

    private void validateCnpj(String cnpj) {
        if (laboratoryRepository.existsByCnpj(cnpj)) {
            throw new AlreadyExistsException("Laboratório", "CNPJ", cnpj);
        }
    }
}

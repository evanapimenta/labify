package com.fatec.labify.api.service;

import com.fatec.labify.api.dto.laboratory.CreateLaboratoryDTO;
import com.fatec.labify.api.dto.laboratory.LaboratoryResponseDTO;
import com.fatec.labify.api.dto.laboratory.UpdateLaboratoryDTO;
import com.fatec.labify.api.dto.patient.AddressDTO;
import com.fatec.labify.domain.Address;
import com.fatec.labify.domain.Laboratory;
import com.fatec.labify.exception.LaboratoryAlreadyExistsException;
import com.fatec.labify.exception.LaboratoryNotFoundException;
import com.fatec.labify.repository.LaboratoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class LaboratoryService {
    private final LaboratoryRepository laboratoryRepository;

    public LaboratoryService(LaboratoryRepository laboratoryRepository) {
        this.laboratoryRepository = laboratoryRepository;
    }

    public Page<LaboratoryResponseDTO> findAll(Pageable pageable) {
        return laboratoryRepository.findAll(pageable).map(LaboratoryResponseDTO::new);
    }

    public LaboratoryResponseDTO findById(String id) {
        return laboratoryRepository.findById(id).map(LaboratoryResponseDTO::new).orElseThrow(() -> new LaboratoryNotFoundException(id));
    }

    @Transactional
    public Laboratory create(CreateLaboratoryDTO createLaboratoryDTO) {
        Optional<Laboratory> existingByCnpj = laboratoryRepository.findByCnpj(createLaboratoryDTO.getCnpj());

        if (existingByCnpj.isPresent()) {
            throw new LaboratoryAlreadyExistsException("CNPJ", createLaboratoryDTO.getCnpj());
        }

        Address address = setCreateAddress(createLaboratoryDTO);
        Laboratory laboratory = setCreateLaboratory(createLaboratoryDTO, address);
        return laboratoryRepository.save(laboratory);
    }

    @Transactional
    public Laboratory update(String id, UpdateLaboratoryDTO updateLaboratoryDTO) {
        Laboratory lab = laboratoryRepository.findById(id).orElseThrow(() -> new LaboratoryNotFoundException(id));

        if (updateLaboratoryDTO.getName() != null) lab.setName(updateLaboratoryDTO.getName());
        if (updateLaboratoryDTO.getEmail() != null) lab.setEmail(updateLaboratoryDTO.getEmail());
        if (updateLaboratoryDTO.getPhoneNumber() != null) lab.setPhoneNumber(updateLaboratoryDTO.getPhoneNumber());
        if (updateLaboratoryDTO.getAddressDTO() != null) {
            setUpdateAddress(lab, updateLaboratoryDTO.getAddressDTO());
        }

        return laboratoryRepository.save(lab);
    }

    @Transactional
    public void delete(String id) {
        Laboratory lab = laboratoryRepository.findById(id).orElseThrow(() -> new LaboratoryNotFoundException(id));
        lab.setActive(false);

        laboratoryRepository.save(lab);
    }

    @Transactional
    public void changeActive(String id, boolean status) {
        Laboratory lab = laboratoryRepository.findById(id).orElseThrow(() -> new LaboratoryNotFoundException(id));

        lab.setActive(status);
        laboratoryRepository.save(lab);
    }


    private Laboratory setCreateLaboratory(CreateLaboratoryDTO createLaboratoryDTO, Address address) {
        return new Laboratory()
                .setId(UUID.randomUUID().toString())
                .setName(createLaboratoryDTO.getName())
                .setAddress(address)
                .setActive(true)
                .setPhoneNumber(createLaboratoryDTO.getPhoneNumber())
                .setEmail(createLaboratoryDTO.getEmail())
                .setCnpj(createLaboratoryDTO.getCnpj());
    }

    private void setUpdateAddress(Laboratory lab, AddressDTO dto) {
        Address address = lab.getAddress();

        if (dto.getStreet() != null) address.setStreet(dto.getStreet());
        if (dto.getNumber() != null) address.setNumber(dto.getNumber());
        if (dto.getComplement() != null) address.setComplement(dto.getComplement());
        if (dto.getNeighborhood() != null) address.setNeighborhood(dto.getNeighborhood());
        if (dto.getCity() != null) address.setCity(dto.getCity());
        if (dto.getState() != null) address.setState(dto.getState());
        if (dto.getCountry() != null) address.setCountry(dto.getCountry());
        if (dto.getZipCode() != null) address.setZipCode(dto.getZipCode());

        lab.setAddress(address);
    }

    private Address setCreateAddress(CreateLaboratoryDTO createLaboratoryDTO) {
        return new Address()
                .setStreet(createLaboratoryDTO.getAddressDTO().getStreet())
                .setNumber(createLaboratoryDTO.getAddressDTO().getNumber())
                .setComplement(createLaboratoryDTO.getAddressDTO().getComplement())
                .setNeighborhood(createLaboratoryDTO.getAddressDTO().getNeighborhood())
                .setZipCode(createLaboratoryDTO.getAddressDTO().getZipCode())
                .setCity(createLaboratoryDTO.getAddressDTO().getCity())
                .setState(createLaboratoryDTO.getAddressDTO().getState())
                .setCountry(createLaboratoryDTO.getAddressDTO().getCountry());
    }

}

package com.fatec.labify.api.service;

import com.fatec.labify.api.dto.patient.*;
import com.fatec.labify.domain.Address;
import com.fatec.labify.domain.Patient;
import com.fatec.labify.domain.Role;
import com.fatec.labify.domain.User;
import com.fatec.labify.exception.*;
import com.fatec.labify.repository.PatientRepository;
import com.fatec.labify.repository.UserRepository;
import com.fatec.labify.util.AddressUtils;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final UserService userService;
    private final UserRoleService userRoleService;

    public PatientService(UserRepository userRepository, PatientRepository patientRepository, UserService userService, UserRoleService userRoleService) {
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.userService = userService;
        this.userRoleService = userRoleService;
    }

    public Page<PatientResponseDTO> findAll(Pageable pageable) {
        return patientRepository.findAll(pageable).map(PatientResponseDTO::new);
    }

    public PatientResponseDTO findById(String id, String username) {
        return patientRepository.findById(userService.validateUser(id, username).getId()).map(PatientResponseDTO::new)
                .orElseThrow(() -> new PatientNotFoundException(id));
    }

    @Transactional
    public CreatePatientResponseDTO create(String email, CreatePatientDTO dto) {
        User user = userRepository.findByEmailIgnoreCase(email).orElseThrow(() -> new UserNotFoundException(email));

        validateCpf(dto.getCpf());

        Address address = new Address(dto.getAddressDTO().getStreet(), dto.getAddressDTO().getNumber(),
                dto.getAddressDTO().getNeighborhood(), dto.getAddressDTO().getCity(), dto.getAddressDTO().getState(),
                dto.getAddressDTO().getZipCode(), dto.getAddressDTO().getCountry());

        Patient patient = new Patient(dto.getCpf(), dto.getInsuranceName(), dto.getGender(), dto.getPhoneNumber(),
                dto.getWeight(), dto.getEmergencyContactName(), dto.getEmergencyContactNumber(),
                dto.getInsuranceName() != null, dto.getBirthDate(), address, user);

        userRoleService.setUserRole(user, Role.PATIENT);
        patientRepository.save(patient);
        return new CreatePatientResponseDTO(patient);
    }

    @Transactional
    public void update(String id, UpdatePatientDTO dto, String username) {
        User user = userService.validateUser(id, username);
        Patient patient = patientRepository.findById(user.getId())
                .orElseThrow(() -> new PatientNotFoundException(id));

        Optional.ofNullable(dto.getWeight()).ifPresent(patient::setWeight);
        Optional.ofNullable(dto.getPhoneNumber()).ifPresent(patient::setPhoneNumber);
        Optional.ofNullable(dto.getGender()).ifPresent(patient::setGender);
        Optional.ofNullable(dto.getEmergencyContactName()).ifPresent(patient::setEmergencyContactName);
        Optional.ofNullable(dto.getEmergencyContactNumber()).ifPresent(patient::setEmergencyContactNumber);

        Optional.ofNullable(dto.getInsuranceName()).ifPresent(insuranceName -> {
            patient.setInsuranceName(insuranceName.trim().isEmpty() ? null : insuranceName.trim());
            patient.setInsured(!insuranceName.trim().isEmpty());
        });

        Optional.ofNullable(dto.getAddressDTO()).ifPresent(addressDTO -> {
            Address address = AddressUtils.updateAddress(patient.getAddress(), addressDTO);
            patient.setAddress(address);
        });

        patientRepository.save(patient);
    }

    @Transactional
    public void delete(String id, String username) {
        User user = userService.validateUser(id, username);
        user.setPatient(null);
        userRepository.save(user);

        patientRepository.deleteById(id);
    }

    private void validateCpf(String cpf) {
        if (patientRepository.existsByCpf(cpf)) {
            throw new AlreadyExistsException("Paciente", "CPF", cpf);
        }
    }
}

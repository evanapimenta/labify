package com.fatec.labify.api.service;

import com.fatec.labify.api.dto.GeolocationDTO;
import com.fatec.labify.api.dto.patient.*;
import com.fatec.labify.client.NominatimClient;
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
    private final NominatimClient nominatimClient;

    public PatientService(UserRepository userRepository, PatientRepository patientRepository, UserService userService, UserRoleService userRoleService, NominatimClient nominatimClient) {
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.userService = userService;
        this.userRoleService = userRoleService;
        this.nominatimClient = nominatimClient;
    }

    public Page<PatientResponseDTO> findAll(Pageable pageable) {
        return patientRepository.findAll(pageable).map(PatientResponseDTO::new);
    }

    public PatientResponseDTO findById(String id, String username) {
        return patientRepository.findById(userService.validateUser(id, username).getId()).map(PatientResponseDTO::new)
                .orElseThrow(() -> new NotFoundException("Paciente", id));
    }

    @Transactional
    public CreatePatientResponseDTO create(String email, CreatePatientDTO dto) {
        User user = userRepository.findByEmailIgnoreCase(email).orElseThrow(() -> new NotFoundException("Usuário", email));
        validateCpf(dto.getCpf());

        GeolocationDTO geolocationDTO = nominatimClient.getGeolocation(dto.getAddressDTO());
        Address address = AddressUtils.setAddress(dto.getAddressDTO(), geolocationDTO);

        Patient patient = new Patient(
                dto.getCpf(),
                dto.getInsuranceName(),
                dto.getGender(),
                dto.getPhoneNumber(),
                dto.getWeight(),
                dto.getEmergencyContactName(),
                dto.getEmergencyContactNumber(),
                dto.getInsuranceName() != null,
                dto.getBirthDate(), address, user);

        user.setRole(Role.PATIENT);
        patientRepository.save(patient);
        return new CreatePatientResponseDTO(patient);
    }

    @Transactional
    public PatientResponseDTO update(String id, UpdatePatientDTO dto, String username) {
        User user = userService.validateUser(id, username);
        Patient patient = patientRepository.findById(user.getId())
                .orElseThrow(() -> new NotFoundException("Paciente", id));

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
        return new PatientResponseDTO(patient);
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

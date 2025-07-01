package com.fatec.labify.api.service;

import com.fatec.labify.api.dto.patient.AddressDTO;
import com.fatec.labify.api.dto.patient.CreatePatientDTO;
import com.fatec.labify.api.dto.patient.PatientResponseDTO;
import com.fatec.labify.api.dto.patient.UpdatePatientDTO;
import com.fatec.labify.domain.Address;
import com.fatec.labify.domain.Patient;
import com.fatec.labify.domain.User;
import com.fatec.labify.exception.PatientAlreadyExistsException;
import com.fatec.labify.exception.PatientNotFoundException;
import com.fatec.labify.exception.UserAlreadyExistsException;
import com.fatec.labify.exception.UserNotFoundException;
import com.fatec.labify.repository.PatientRepository;
import com.fatec.labify.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final UserRoleService userRoleService;

    public PatientService(UserRepository userRepository, PatientRepository patientRepository, UserRoleService userRoleService) {
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.userRoleService = userRoleService;
    }

    public Page<PatientResponseDTO> findAll(Pageable pageable) {
        return patientRepository.findAll(pageable).map(PatientResponseDTO::new);
    }

    public PatientResponseDTO findById(String id, String username) {
        userRoleService.validateUserAccess(id, username);
        return patientRepository.findById(id).map(PatientResponseDTO::new).orElseThrow(() -> new PatientNotFoundException(id));
    }

    @Transactional
    public Patient create(String userId, CreatePatientDTO createPatientDTO) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Paciente não encontrado"));
        Optional<Patient> existingByCpf = patientRepository.findByCpf(createPatientDTO.getCpf());

        if (existingByCpf.isPresent()) {
            throw new PatientAlreadyExistsException("CPF", createPatientDTO.getCpf());
        }

        Address address = setCreateAddress(createPatientDTO);
        Patient patient = setCreatePatient(createPatientDTO, user, address);

        return patientRepository.save(patient);
    }

    @Transactional
    public void update(String id, UpdatePatientDTO updatePatientDTO, String username) {
        User user = userRoleService.validateUserAccess(id, username);

        Patient patient = patientRepository.findById(user.getId()).orElseThrow(() -> new PatientNotFoundException(id));

        if (updatePatientDTO.getWeight() != null) patient.setWeight(updatePatientDTO.getWeight());
        if (updatePatientDTO.getPhoneNumber() != null) patient.setPhoneNumber(updatePatientDTO.getPhoneNumber());
        if (updatePatientDTO.getGender() != null) patient.setGender(updatePatientDTO.getGender());
        if (updatePatientDTO.getEmergencyContactName() != null) patient.setEmergencyContactName(updatePatientDTO.getEmergencyContactName());
        if (updatePatientDTO.getEmergencyContactNumber() != null) patient.setEmergencyContactNumber(updatePatientDTO.getEmergencyContactNumber());

        String insuranceName = updatePatientDTO.getInsuranceName();

        if (updatePatientDTO.getInsuranceName() != null) {
            patient.setInsuranceName(insuranceName);
            patient.setInsured(!insuranceName.trim().isEmpty());
        }

        if (updatePatientDTO.getAddressDTO() != null) {
            setUpdateAddress(patient, updatePatientDTO.getAddressDTO());
        }

        patientRepository.save(patient);
    }

    @Transactional
    public void delete(String id, String username) {
        User user = userRoleService.validateUserAccess(id, username);
        user.setPatient(null);
        userRepository.save(user);

        patientRepository.deleteById(id);
    }

    private Patient setCreatePatient(CreatePatientDTO createPatientDTO, User user, Address address) {
        Patient patient = new Patient()
                .setInsuranceName(createPatientDTO.getInsuranceName())
                .setCpf(createPatientDTO.getCpf())
                .setGender(createPatientDTO.getGender())
                .setPhoneNumber(createPatientDTO.getPhoneNumber())
                .setWeight(createPatientDTO.getWeight())
                .setEmergencyContactName(createPatientDTO.getEmergencyContactName())
                .setEmergencyContactNumber(createPatientDTO.getEmergencyContactNumber())
                .setInsured(createPatientDTO.getInsuranceName() != null)
                .setBirthDate(createPatientDTO.getBirthDate())
                .setAddress(address);

        patient.setUser(user);
        return patient;
    }

    private Address setCreateAddress(CreatePatientDTO createPatientDTO) {
        return new Address()
                .setStreet(createPatientDTO.getAddressDTO().getStreet())
                .setNumber(createPatientDTO.getAddressDTO().getNumber())
                .setComplement(createPatientDTO.getAddressDTO().getComplement())
                .setNeighborhood(createPatientDTO.getAddressDTO().getNeighborhood())
                .setCity(createPatientDTO.getAddressDTO().getCity())
                .setState(createPatientDTO.getAddressDTO().getState())
                .setZipCode(createPatientDTO.getAddressDTO().getZipCode())
                .setCountry(createPatientDTO.getAddressDTO().getCountry());
    }

    private void setUpdateAddress(Patient patient, AddressDTO dto) {
        Address address = patient.getAddress();

        if (dto.getStreet() != null) address.setStreet(dto.getStreet());
        if (dto.getNumber() != null) address.setNumber(dto.getNumber());
        if (dto.getComplement() != null) address.setComplement(dto.getComplement());
        if (dto.getNeighborhood() != null) address.setNeighborhood(dto.getNeighborhood());
        if (dto.getCity() != null) address.setCity(dto.getCity());
        if (dto.getState() != null) address.setState(dto.getState());
        if (dto.getCountry() != null) address.setCountry(dto.getCountry());
        if (dto.getZipCode() != null) address.setZipCode(dto.getZipCode());

        patient.setAddress(address);
    }
}

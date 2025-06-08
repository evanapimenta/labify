package com.fatec.labify.repository;

import com.fatec.labify.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, String> {
    Optional<Patient> findByCpf(String cpf);
}

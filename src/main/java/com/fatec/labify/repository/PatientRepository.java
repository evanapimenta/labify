package com.fatec.labify.repository;

import com.fatec.labify.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, String> {
    boolean existsByCpf(String cpf);
}

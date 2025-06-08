package com.fatec.labify.repository;

import com.fatec.labify.domain.Laboratory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LaboratoryRepository extends JpaRepository<Laboratory, String> {
    Optional<Laboratory> findByCnpj(String cnpj);
}

package com.fatec.labify.repository;

import com.fatec.labify.domain.Laboratory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LaboratoryRepository extends JpaRepository<Laboratory, String> {
    boolean existsByCnpj(String cnpj);
}

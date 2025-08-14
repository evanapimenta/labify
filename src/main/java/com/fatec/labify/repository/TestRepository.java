package com.fatec.labify.repository;

import com.fatec.labify.domain.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<Test, Long> {
    boolean existsByName(String name);
}

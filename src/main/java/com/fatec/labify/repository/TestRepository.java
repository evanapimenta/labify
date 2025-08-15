package com.fatec.labify.repository;

import com.fatec.labify.domain.ScheduledTest;
import com.fatec.labify.domain.Test;
import com.fatec.labify.domain.TestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TestRepository extends JpaRepository<Test, Long> {
    boolean existsByName(String name);
}

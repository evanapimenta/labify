package com.fatec.labify.repository;

import com.fatec.labify.domain.ScheduledTest;
import com.fatec.labify.domain.TestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleTestRepository extends JpaRepository<ScheduledTest, String> {
    List<ScheduledTest> findByStatusAndPatient_Id(TestStatus status, String patientId);
    List<ScheduledTest> findByStatusAndBranch_Id(TestStatus testStatus, String branchId);
}

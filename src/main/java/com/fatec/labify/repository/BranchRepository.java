package com.fatec.labify.repository;

import com.fatec.labify.domain.Branch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BranchRepository extends JpaRepository<Branch, String> {

    @Query(value = """
    SELECT CASE WHEN l.super_admin_id = :userId THEN true ELSE false END
    FROM branches b
    JOIN laboratories l ON b.laboratory_id = l.id
    WHERE b.id = :branchId
    """, nativeQuery = true)
    boolean isSuperAdmin(@Param("userId") String userId, @Param("branchId") String branchId);
    Optional<Branch> findById(String id);
    Optional<Branch> findByAdminId(String adminId);
    Page<Branch> findBranchByLaboratory_Id(String id, Pageable pageable);
}

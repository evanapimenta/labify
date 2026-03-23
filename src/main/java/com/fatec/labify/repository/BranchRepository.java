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
    Optional<Branch> findById(String id);
    Page<Branch> findBranchByLaboratory_Id(String id, Pageable pageable);
    @Query(
            value = "SELECT " +
                    "b.id, " +
                    "b.name, " +
                    "b.phone_number, " +
                    "b.email, " +
                    "b.opening_hours, " +
                    "b.created_at, " +
                    "b.updated_at, " +
                    "b.address_street, " +
                    "b.address_number, " +
                    "b.address_complement, " +
                    "b.address_neighborhood, " +
                    "b.address_city, " +
                    "b.address_state, " +
                    "b.address_postal_code, " +
                    "b.address_country, " +
                    "6371 * 2 * ASIN(SQRT( " +
                    "POWER(SIN(RADIANS(address_latitude - :latitude) / 2), 2) + " +
                    "COS(RADIANS(:latitude)) * COS(RADIANS(address_latitude)) * " +
                    "POWER(SIN(RADIANS(address_longitude - :longitude) / 2), 2) " +
                    ")) AS distance_km " +
                    "FROM branches b " +
                    "ORDER BY distance_km",
            nativeQuery = true
    )
    Page<Object[]> findClosestBranchesWithDistance(
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            Pageable pageable
    );
}

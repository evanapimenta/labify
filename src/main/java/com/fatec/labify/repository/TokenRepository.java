package com.fatec.labify.repository;

import com.fatec.labify.domain.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<UserToken, String> {
}

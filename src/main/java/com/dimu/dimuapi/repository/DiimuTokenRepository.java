package com.dimu.dimuapi.repository;

import com.dimu.dimuapi.model.DiimuToken;
import com.dimu.dimuapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiimuTokenRepository extends JpaRepository<DiimuToken, String> {

    Optional<DiimuToken> findByTokenAndUser(String token, User user);
}

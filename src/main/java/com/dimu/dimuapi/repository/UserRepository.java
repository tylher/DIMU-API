package com.dimu.dimuapi.repository;

import com.dimu.dimuapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    @Transactional
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}

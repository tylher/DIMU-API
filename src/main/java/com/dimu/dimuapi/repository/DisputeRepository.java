package com.dimu.dimuapi.repository;

import com.dimu.dimuapi.model.Agreement;
import com.dimu.dimuapi.model.Dispute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DisputeRepository extends JpaRepository<Dispute,String> {
    Optional<Dispute> findByAgreement(Agreement agreement);
}

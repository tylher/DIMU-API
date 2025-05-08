package com.dimu.dimuapi.repository;

import com.dimu.dimuapi.model.Agreement;
import com.dimu.dimuapi.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface AgreementRepository extends JpaRepository<Agreement,String> {
    List<Agreement> findAgreementsByBuyer(User user, Sort sort);

    Optional<Agreement> findByAgreementIdAndBuyer(String agreementId, User user);
}

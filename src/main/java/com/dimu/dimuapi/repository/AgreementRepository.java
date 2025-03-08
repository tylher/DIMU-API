package com.dimu.dimuapi.repository;

import com.dimu.dimuapi.model.Agreement;
import com.dimu.dimuapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgreementRepository extends JpaRepository<Agreement,String> {
    List<Agreement> findAgreementsByBuyer(User user);

}

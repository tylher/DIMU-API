package com.dimu.dimuapi.repository;

import com.dimu.dimuapi.model.Agreement;
import com.dimu.dimuapi.model.Transaction;
import com.dimu.dimuapi.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface AgreementRepository extends JpaRepository<Agreement,String> {
    List<Agreement> findByBuyerOrSeller(User buyer,User seller, Sort sort);

    @Query("SELECT a FROM Agreement a WHERE a.agreementId = :id AND (a.seller = :user OR a.buyer = :user)")
    Optional<Agreement> findByIdAndUserIsSellerOrBuyer(@Param("id") String id, @Param("user") User user);

    Optional<Agreement> findByTransaction(Transaction transaction);
}

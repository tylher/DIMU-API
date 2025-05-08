package com.dimu.dimuapi.repository;

import com.dimu.dimuapi.model.Agreement;
import com.dimu.dimuapi.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,String> {
    Optional<Transaction> findByReference(String reference);
    Optional<Transaction> findByTransactionId(String transactionId);
}

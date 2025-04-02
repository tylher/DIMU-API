package com.dimu.dimuapi.repository;

import com.dimu.dimuapi.model.Agreement;
import com.dimu.dimuapi.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,String> {
}

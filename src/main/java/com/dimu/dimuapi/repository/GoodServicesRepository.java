package com.dimu.dimuapi.repository;

import com.dimu.dimuapi.model.GoodServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodServicesRepository extends JpaRepository<GoodServices, String> {
}

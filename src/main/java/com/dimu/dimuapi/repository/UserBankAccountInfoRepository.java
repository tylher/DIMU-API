package com.dimu.dimuapi.repository;

import com.dimu.dimuapi.model.UserBankAccountInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBankAccountInfoRepository extends JpaRepository<UserBankAccountInfo, String> {
    Boolean existsByTransferRecipientCode(String transferRecipientCode);
}

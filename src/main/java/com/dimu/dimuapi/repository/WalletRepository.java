package com.dimu.dimuapi.repository;

import com.dimu.dimuapi.Enum.WalletType;
import com.dimu.dimuapi.model.DiimuWallet;
import com.dimu.dimuapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<DiimuWallet,String> {
    Optional<DiimuWallet> findByUserAndWalletType(User user, WalletType type);

    boolean existsByUserAndWalletType(User user, WalletType type);

    List<DiimuWallet> findByUser(User user);

    Optional<DiimuWallet> findByWalletId(String walletId);
}

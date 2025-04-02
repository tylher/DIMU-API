package com.dimu.dimuapi.service.wallet;

import com.dimu.dimuapi.Enum.WalletType;
import com.dimu.dimuapi.model.DiimuWallet;
import com.dimu.dimuapi.model.User;

import java.util.List;

public interface WalletService {
    public DiimuWallet createWallet(User user, WalletType type);

    public List<DiimuWallet> getWallets(User user);
}

package com.dimu.dimuapi.service.wallet;

import com.dimu.dimuapi.Enum.WalletType;
import com.dimu.dimuapi.exceptionshandling.CustomException;
import com.dimu.dimuapi.model.DiimuWallet;
import com.dimu.dimuapi.model.User;
import com.dimu.dimuapi.repository.WalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class WalletServicImpl implements WalletService{
    @Autowired
    WalletRepository walletRepository;

    @Override
    public DiimuWallet createWallet(User user, WalletType type) {
        try{
            if (walletRepository.existsByUserAndWalletType(user,type)){
                throw new CustomException("wallet with type, "
                        + type.toString().toLowerCase()+"has been created previously for user, "+
                        user.getFirstName());
            }else{
                DiimuWallet diimuWallet = new DiimuWallet();
                diimuWallet.setUser(user);
                diimuWallet.setWalletType(type);
              return  walletRepository.save(diimuWallet);
            }
        } catch (CustomException e) {
            log.error("Unable to create wallet: {}", e.getMessage());
            throw new CustomException("Unable to create wallet: " + e.getMessage() );

        }catch(Exception ex){
            log.error("An unknown error occured: {}", ex.getMessage());
            throw new CustomException("Unable to create wallet: " + ex.getMessage() );
        }
    }

    @Override
    public List<DiimuWallet> getWallets(User user) {
        return walletRepository.findByUser(user);
    }


    public void fundWallet(String walletId ){

    };
}

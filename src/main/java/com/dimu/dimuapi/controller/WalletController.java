package com.dimu.dimuapi.controller;

import com.dimu.dimuapi.dto.ApiResponseDto;
import com.dimu.dimuapi.dto.FundWalletDto;
import com.dimu.dimuapi.model.DiimuWallet;
import com.dimu.dimuapi.model.User;
import com.dimu.dimuapi.service.wallet.WalletService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {
    @Autowired
    WalletService walletService;


    @GetMapping("/get-wallets")
    public ResponseEntity<ApiResponseDto> getWallets(@AuthenticationPrincipal User user){
        List<DiimuWallet>  wallets= walletService.getWallets(user);
        return ResponseEntity.ok(new ApiResponseDto(true,"Wallets fetched successfully",wallets));
    }

    @PostMapping("/fund")
    public ResponseEntity<ApiResponseDto> fundWallet(@Valid @RequestBody FundWalletDto fundWalletDto){
        ApiResponseDto responseDto = walletService.fundWallet(fundWalletDto.walletId()
                , fundWalletDto.reference());
        return ResponseEntity.ok(responseDto);
    }
}

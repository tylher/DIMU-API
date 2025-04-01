package com.dimu.dimuapi.service.auth;

import com.dimu.dimuapi.Enum.WalletType;
import com.dimu.dimuapi.dto.LoginResponseDto;
import com.dimu.dimuapi.model.User;
import com.dimu.dimuapi.repository.UserRepository;
import com.dimu.dimuapi.repository.WalletRepository;
import com.dimu.dimuapi.service.wallet.WalletService;
import com.dimu.dimuapi.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService{

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepostory;

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    WalletService walletService;


    public LoginResponseDto login(String username, String password){
        Authentication authentication = authenticationManager.authenticate(UsernamePasswordAuthenticationToken
                .unauthenticated(username, password));

        if(authentication != null && authentication.isAuthenticated()) {
            String jwt = JwtUtils.generateToken(username, authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")));
            User user = userRepostory.findByEmail(username).get();
            if(!walletRepository.existsByUserAndWalletType(user, WalletType.CUSTOMER)) {
                walletService.createWallet(user, WalletType.CUSTOMER);
            }

            return new LoginResponseDto(jwt, user);
        } else {
            throw new BadCredentialsException("Invalid username or password");
        }

    }
}

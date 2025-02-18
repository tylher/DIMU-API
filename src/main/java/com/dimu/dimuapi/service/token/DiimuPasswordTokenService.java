package com.dimu.dimuapi.service.token;

import com.dimu.dimuapi.Enum.TokenFormat;
import com.dimu.dimuapi.Enum.TokenType;
import com.dimu.dimuapi.exceptionshandling.CustomException;
import com.dimu.dimuapi.exceptionshandling.ResourceNotFoundException;
import com.dimu.dimuapi.model.DiimuToken;
import com.dimu.dimuapi.model.Mail;
import com.dimu.dimuapi.model.User;
import com.dimu.dimuapi.repository.DiimuTokenRepository;
import com.dimu.dimuapi.repository.UserRepository;
import com.dimu.dimuapi.service.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service("DiimuPasswordTokenService")
public class DiimuPasswordTokenService implements DiimuTokenService{
    @Autowired
    UserRepository userRepository;

    @Autowired
    DiimuTokenRepository diimuTokenRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public String createToken(String email) {
        try{
            User user = getUserByEmail(email);
                String tokenStr = DiimuToken.generateRandomToken(TokenType.RESET_PASSWORD
                        , TokenFormat.NUMERIC);
                DiimuToken token = DiimuToken.builder()
                        .user(user)
                        .tokenType(TokenType.RESET_PASSWORD)
                        .token(tokenStr)
                        .used(false)
                        .createdAt(LocalDateTime.now())
                        .expiresAt(LocalDateTime.now().plusMinutes(TokenType.RESET_PASSWORD.getExpiry()))
                        .build();

                return diimuTokenRepository.save(token).getToken();

        }catch (Exception ex){
            throw new CustomException("Error creating token: "+ ex.getMessage());
        }

    }

    @Override
    public String verifyToken(String email, String token) {
        try{
            User user = getUserByEmail(email);
            DiimuToken diimuToken = diimuTokenRepository.findByTokenAndUser(token,user).orElseThrow(
                    ()-> new ResourceNotFoundException("Reset Password Token","code",token)
            );

            if(diimuToken.isUsed()){
                throw new CustomException("Token with code, "+token+ " has been used");
            }else if(diimuToken.getExpiresAt().isBefore(LocalDateTime.now())){
                throw new CustomException("Token with code, "+token+ " has expired" +
                        ", kindly request another");
            }else{
                diimuToken.setUsed(true);
                diimuTokenRepository.save(diimuToken);
                userRepository.save(user);
                return "Password reset code verified successfully";
            }

        }catch(Exception e){
            throw new CustomException("Unable to verify token: "+e.getMessage());
        }
    }

    @Override
    public String resendToken(String email) throws Exception {
        try{
            String content = "Kindly reset your password using the code below\n "
                    +createToken(email);
            Mail mail = new Mail(new String[]{email},"ayo@diimu.net"
                    ,"Reset Your Password");
            emailService.sendSimpleMail(mail,content);
            return "Password reset code sent successfully";
        }catch (Exception e){
            throw new Exception("Unable to send mail: " + e.getMessage());
        }

    }

    private User getUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(
                ()-> new ResourceNotFoundException("user","email",email)
        );
    }
}

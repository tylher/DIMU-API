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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DiimuVerificationTokenService implements DiimuTokenService{
    @Autowired
    UserRepository userRepository;

    @Autowired
    DiimuTokenRepository diimuTokenRepository;

    @Autowired
    EmailService emailService;

    @Override
    public String createToken(String email) {
        try{
            User user = getUserByEmail(email);

            if(user.isVerified()){
                throw new CustomException("User with email, "+email+" is already verified");
            }else{
                String tokenStr = DiimuToken.generateRandomToken(TokenType.VERIFICATION
                        , TokenFormat.NUMERIC);
                DiimuToken token = DiimuToken.builder()
                        .user(user)
                        .tokenType(TokenType.VERIFICATION)
                        .token(tokenStr)
                        .used(false)
                        .createdAt(LocalDateTime.now())
                        .expiresAt(LocalDateTime.now().plusMinutes(TokenType.VERIFICATION.getExpiry()))
                        .build();

                return diimuTokenRepository.save(token).getToken();
            }
        }catch (Exception ex){
            throw new CustomException("Error creating token: "+ ex.getMessage());
        }

    }

    @Override
    public String verifyToken(String email, String token) {
        try{
            User user = getUserByEmail(email);
            DiimuToken diimuToken = diimuTokenRepository.findByTokenAndUser(token,user).orElseThrow(
                    ()-> new ResourceNotFoundException("Verification Token","code",token)
            );

            if(diimuToken.isUsed()){
                throw new CustomException("Token with code, "+token+ " has been used");
            }else if(diimuToken.getExpiresAt().isBefore(LocalDateTime.now())){
                throw new CustomException("Token with code, "+token+ " has expired" +
                        ", kindly request another");
            }else{
                diimuToken.setUsed(true);
                user.setVerified(true);
                diimuTokenRepository.save(diimuToken);
                userRepository.save(user);
                return "Verification code verified successfully";
            }

        }catch(Exception e){
            throw new CustomException("Unable to verify token: "+e.getMessage());
        }
    }

    public String resendVerificationToken(String email) throws Exception {
        try{
            String content = "Kindly verify your account using the code below\n "
                    +createToken(email);
            Mail mail = new Mail(new String[]{email},"ayo@diimu.net"
                    ,"Welcome To Diimu");
            emailService.sendSimpleMail(mail,content);
            return "Verification code sent successfully";
        }catch (Exception e){
            throw new Exception("Unable to mail: " + e.getMessage());
        }

    }

    private User getUserByEmail(String email){
       return userRepository.findByEmail(email).orElseThrow(
                ()-> new ResourceNotFoundException("user","email",email)
        );
    }

}

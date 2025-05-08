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


@Service("DiimuSecurePinTokenService")
public class DiimuSecurePinTokenService implements DiimuTokenService{
    @Autowired
    DiimuTokenRepository diimuTokenRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @Override
    public String createToken(String email) {
        try{
            User user = getUserByEmail(email);
            String tokenStr = DiimuToken.generateRandomToken(TokenType.RESET_SECURE_PIN
                    , TokenFormat.NUMERIC);
            DiimuToken token = DiimuToken.builder()
                    .user(user)
                    .tokenType(TokenType.RESET_SECURE_PIN)
                    .token(tokenStr)
                    .used(false)
                    .createdAt(LocalDateTime.now())
                    .expiresAt(LocalDateTime.now().plusMinutes(TokenType.RESET_SECURE_PIN.getExpiry()))
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

        }catch (ResourceNotFoundException e){
            throw e;
        }catch(Exception e){
            throw new CustomException("Unable to verify token: "+e.getMessage());
        }
    }


    @Override
    public String sendToken(String email) throws Exception {
        try{
            String content = "Kindly reset your secure pin using the code below\n "
                    +createToken(email);
            Mail mail = new Mail(new String[]{email},"jummy@diimu.net"
                    ,"Reset Your Secure pin");
            emailService.sendSimpleMail(mail,content);
            return "Secure pin reset code sent successfully";
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

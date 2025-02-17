package com.dimu.dimuapi.controller;


import com.dimu.dimuapi.dto.SignupDto;
import com.dimu.dimuapi.model.Mail;
import com.dimu.dimuapi.service.email.EmailService;
import com.dimu.dimuapi.service.token.DiimuTokenService;
import com.dimu.dimuapi.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final EmailService emailService;
    private final DiimuTokenService diimuTokenService;

    @PostMapping("user/register")
    public ResponseEntity<String> registerUser(@RequestBody @Valid SignupDto signupDto) throws Exception {
        String response = userService.registerUser(signupDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("api/user/welcome")
    public ResponseEntity<String> welcomeUser(@AuthenticationPrincipal String email){
        String response = "welcome" + email+ " to diimu app " ;
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/test-mail")
    public ResponseEntity<String> sendMail(@RequestBody String content) throws Exception {
        try {
            Mail mail = new Mail();
            mail.setFrom("ayo@diimu.net");
            mail.setTo(new String[]{"tylher123@gmail.com","ayoadediran96@gmail.com"});
            mail.setSubject("test mail");
            emailService.sendSimpleMail(mail, content);
            return new ResponseEntity<>("email sent successfully",HttpStatus.OK);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }

    }

    @PostMapping("api/user/verify")
    public  ResponseEntity<String> verifyToken(@RequestParam String code, @AuthenticationPrincipal String email){
        String response = diimuTokenService.verifyToken(email,code);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

package com.dimu.dimuapi.controller;


import com.dimu.dimuapi.dto.*;
import com.dimu.dimuapi.model.Mail;
import com.dimu.dimuapi.model.User;
import com.dimu.dimuapi.service.S3Service;
import com.dimu.dimuapi.service.email.EmailService;
import com.dimu.dimuapi.service.token.DiimuTokenService;
import com.dimu.dimuapi.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final EmailService emailService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private S3Service s3Service;
    @Autowired
    @Qualifier("DiimuVerificationTokenService")
   DiimuTokenService diimuTokenService;

    @PostMapping("user/register")
    public ResponseEntity<ApiResponseDto> registerUser(@RequestBody @Valid SignupDto signupDto) throws Exception {
        ApiResponseDto response = userService.registerUser(signupDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("api/user/welcome")
    public ResponseEntity<String> welcomeUser(@AuthenticationPrincipal User user){
        String response = "welcome" + user.getEmail()+ " to diimu app " ;
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/test-mail")
    public ResponseEntity<String> sendMail(@RequestBody String content) throws Exception {
        try {
            Mail mail = new Mail();
            mail.setFrom("jummy@diimu.net");
            mail.setTo(new String[]{"tylher123@gmail.com"});
            mail.setSubject("test mail");
            emailService.sendSimpleMail(mail, content);
            return new ResponseEntity<>("email sent successfully",HttpStatus.OK);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }

    }

    @PostMapping("/user/verify")
    public  ResponseEntity<ApiResponseDto> verifyToken(@RequestParam String code,@RequestParam String email, @AuthenticationPrincipal User user){
        String response = diimuTokenService.verifyToken(email,code);
        return new ResponseEntity<>(new ApiResponseDto(true,response), HttpStatus.OK);
    }

    @PostMapping("api/user/onboard")
    public  ResponseEntity<ApiResponseDto> onboardUser(@Valid@RequestBody OnboardDto onboardDto,
                                    @AuthenticationPrincipal User user) throws Exception {
        ApiResponseDto response = userService.onBoardUser(onboardDto,user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("api/user/reset-password")
    public  ResponseEntity<ApiResponseDto> resetPassword(@Valid@RequestBody PasswordResetDto passwordResetDto,
                                                         @AuthenticationPrincipal User user) throws Exception {
        ApiResponseDto response = userService.resetPasswordAfterLogin(passwordResetDto.email(),passwordResetDto.password());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("api/test/send")
    public ResponseEntity<String> sendTestMessage(@RequestParam String username, @AuthenticationPrincipal User user) throws Exception {
        messagingTemplate.convertAndSendToUser( user.getUserId(),"/queue/notifications", "Test Notification");
        return ResponseEntity.ok("Message Sent");
    }

    @PatchMapping("api/user/edit")
    public  ResponseEntity<ApiResponseDto> editUser(@Valid @RequestBody EditProfileDto editProfileDto, @AuthenticationPrincipal User user) throws Exception {
        ApiResponseDto response = userService.editProfile(editProfileDto,user.getUserId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("api/user/upload-image")
    public ResponseEntity<ApiResponseDto> uploadUserImage(@AuthenticationPrincipal User user, @RequestPart(name = "profileImage") MultipartFile imageByte) throws IOException {

        ApiResponseDto responseDto = userService.updateProfileImage(imageByte,user.getUserId());
        return  new ResponseEntity<>(responseDto,HttpStatus.OK);
    }

    @PutMapping("api/user/create-recipient")
    public ResponseEntity<ApiResponseDto> createTransferRecipient(@Valid @RequestBody PaystackCreateTransferRecipientDto createTransferRecipientDto
            , @AuthenticationPrincipal User user) throws Exception {

        ApiResponseDto response = userService.createUserTransferRecipient(user,createTransferRecipientDto);
        return new ResponseEntity<>(response,HttpStatus.OK);

    }

    @PutMapping("api/user/add-secure-pin")
    public ResponseEntity<ApiResponseDto> addSecurePin(@Valid @RequestBody SecurePinDto securePinDto
            , @AuthenticationPrincipal User user) throws Exception {

        ApiResponseDto response = userService.addSecurePin(user,securePinDto);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}

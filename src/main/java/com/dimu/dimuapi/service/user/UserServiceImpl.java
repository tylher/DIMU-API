package com.dimu.dimuapi.service.user;

import com.dimu.dimuapi.Enum.WalletType;
import com.dimu.dimuapi.dto.*;
import com.dimu.dimuapi.exceptionshandling.CustomException;
import com.dimu.dimuapi.exceptionshandling.ResourceNotFoundException;
import com.dimu.dimuapi.model.*;
import com.dimu.dimuapi.repository.DiimuTokenRepository;
import com.dimu.dimuapi.repository.RoleRepository;
import com.dimu.dimuapi.repository.UserBankAccountInfoRepository;
import com.dimu.dimuapi.repository.UserRepository;
import com.dimu.dimuapi.service.S3Service;
import com.dimu.dimuapi.service.email.EmailService;
import com.dimu.dimuapi.service.payment.paystack.PaystackService;
import com.dimu.dimuapi.service.token.DiimuTokenService;
import com.dimu.dimuapi.service.wallet.WalletService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    EmailService emailService;

    @Autowired
    UserBankAccountInfoRepository userBankAccountInfoRepository;

    @Autowired
    PaystackService paystackService;

    @Autowired
    S3Service s3Service;

    @Autowired
    WalletService walletService;

    @Autowired
    DiimuTokenRepository diimuTokenRepository;

    @Autowired
    @Qualifier("DiimuVerificationTokenService")
    DiimuTokenService diimuTokenService;

    @Override
    public ApiResponseDto registerUser(SignupDto createUserDto) throws Exception {
        try {
            if (userRepository.existsByEmail(createUserDto.email())) {
                throw new RuntimeException("User with email " + createUserDto.email() + " already exists");
            } else {
                String encodedPassword = passwordEncoder.encode(createUserDto.password());
                List<Role> roles = createUserDto.roleId().stream().map(this::getRoleById).toList();
                User user = new User();
                user.setEmail(createUserDto.email());
                user.setPassword(encodedPassword);
                user.setRoles(roles);
                userRepository.save(user);
                walletService.createWallet(user, WalletType.CUSTOMER);
                String content = "Kindly verify your account using the code below\n "
                        + diimuTokenService.createToken(user.getEmail());
                Mail mail = new Mail(new String[]{user.getEmail()}, "ayo@diimu.net"
                        , "Welcome To Diimu");
                emailService.sendSimpleMail(mail, content);
                return new ApiResponseDto(true, "User registered successfully.");
            }
        } catch (Exception e) {
            if (e.getMessage().contains("email")) {
                throw new BadRequestException("Error creating user: " + e.getMessage());
            } else {
                throw new Exception(e.getMessage());
            }
        }
    }

    @Override
    public ApiResponseDto onBoardUser(OnboardDto onBoardDto, User user) throws Exception {
        try {
            if (!user.isVerified()) {
                throw new BadRequestException("User is not verified");
            }
            user.setFirstName(onBoardDto.firstName());
            user.setLastName(onBoardDto.lastName());
            user.setPhoneNumber(onBoardDto.phoneNumber());
            user.setDateOfBirth(onBoardDto.dateOfBirth());
            user.setGender(onBoardDto.gender());
            user.setOnboarded(true);
            user.setState(onBoardDto.state());
            return new ApiResponseDto(true, "User onboarded successfully", userRepository.save(user)
            );
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public String resetPassword(String email, String password, String code) {

        try {
            User user = userRepository.findByEmail(email).orElseThrow(
                    () -> new ResourceNotFoundException("User", "email", email)
            );

            DiimuToken passwordTokenOpt = diimuTokenRepository
                    .findByTokenAndUser(code, user).orElseThrow(() ->
                            new ResourceNotFoundException("token", "token", code));

            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);

            return "New password saved successfully";
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception ex) {
            throw new CustomException(ex.getMessage());
        }


    }

    @Override
    public ApiResponseDto resetPasswordAfterLogin(String email, String password) {
        try {
            User user = userRepository.findByEmail(email).orElseThrow(
                    () -> new ResourceNotFoundException("User", "email", email)
            );
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
            return new ApiResponseDto(true, "Password reset successfully");
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception ex) {
            throw new CustomException(ex.getMessage());
        }
    }

    @Override
    public ApiResponseDto editProfile(EditProfileDto editProfileDto, String userId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(
                    () -> new ResourceNotFoundException("User", "id", userId));
            user.setFirstName(editProfileDto.firstName() != null ? editProfileDto.firstName() : user.getFirstName());
            user.setLastName(editProfileDto.lastName() != null ? editProfileDto.lastName() : user.getLastName());
            user.setPhoneNumber(editProfileDto.phoneNumber() != null ? editProfileDto.phoneNumber() : user.getPhoneNumber());
            user.setDateOfBirth(editProfileDto.dateOfBirth() != null ? editProfileDto.dateOfBirth() : user.getDateOfBirth());
            user.setGender(editProfileDto.gender() != null ? editProfileDto.gender() : user.getGender());
            user.setCountry(editProfileDto.country()!=null? editProfileDto.country():user.getCountry());
            user.setCountryCode(editProfileDto.countryCode()!=null? editProfileDto.countryCode():user.getCountryCode());
            user.setState(editProfileDto.state() != null ? editProfileDto.state() : user.getState());
            return new ApiResponseDto(true, "Profile updated successfully", userRepository.save(user));
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception ex) {
            throw new CustomException(ex.getMessage());
        }
    }

    @Override
    public ApiResponseDto updateProfileImage(MultipartFile file, String userId){
        try{
            User savedUser  = userRepository.findById(userId).orElseThrow(
                    () -> new ResourceNotFoundException("User", "id", userId));
            if(savedUser.getProfileImageUrl()!=null){
                s3Service.deleteFile(savedUser.getProfileImageUrl());
            }
            String publicImage = s3Service.uploadFile(file);

            savedUser.setProfileImageUrl(publicImage);

            return new ApiResponseDto(true,"Profile image updated successfully"
                    ,userRepository.save(savedUser));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }catch (Exception ex){
            throw new CustomException("Unable to update profile image: "+ex.getMessage());
        }
    }

    @Override
    @Transactional
    public ApiResponseDto createUserTransferRecipient(User user, PaystackCreateTransferRecipientDto createTransferRecipientDto) {
        try {
            PaystackTransferRecipient recipient = paystackService.createTransferRecipient( createTransferRecipientDto);
            if(recipient!=null){
                UserBankAccountInfo info = getBankAccountInfo(user,recipient);
                userBankAccountInfoRepository.save(info);

                List<UserBankAccountInfo> accountInfos = user.getBankAccountInfoList() != null
                        ? user.getBankAccountInfoList() : new ArrayList<>();
                accountInfos.add(info);
                user.setBankAccountInfoList(accountInfos);
                userRepository.save(user);
                return new ApiResponseDto(true,"transfer recipient created successfully");
            }
            else{
                throw new CustomException("Unable to create transfer recipient");
            }
        } catch (CustomException e) {
            throw e;
        }
        catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
    }

    private UserBankAccountInfo getBankAccountInfo(User user, PaystackTransferRecipient recipient) {
        UserBankAccountInfo info =  new UserBankAccountInfo();
        info.setAccountName(recipient.getData().getDetails().getAccount_name());
        info.setBankName(recipient.getData().getDetails().getBank_name());
        info.setAccountNumber(recipient.getData().getDetails().getAccount_number());
        info.setTransferRecipientCode(recipient.getData().getRecipient_code());
        info.setUser(user);

        return  info;
    }

    private Role getRoleById(Integer roleId) {
        return roleRepository.findById(roleId).orElseThrow(
                () -> new ResourceNotFoundException("Role", "id", roleId.toString()));
    }
}

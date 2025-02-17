package com.dimu.dimuapi.service.user;

import com.dimu.dimuapi.dto.SignupDto;
import com.dimu.dimuapi.exceptionshandling.ResourceNotFoundException;
import com.dimu.dimuapi.model.Mail;
import com.dimu.dimuapi.model.Role;
import com.dimu.dimuapi.model.User;
import com.dimu.dimuapi.repository.RoleRepository;
import com.dimu.dimuapi.repository.UserRepository;
import com.dimu.dimuapi.service.email.EmailService;
import com.dimu.dimuapi.service.token.DiimuTokenService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    DiimuTokenService diimuTokenService;

    @Override
    public String registerUser(SignupDto createUserDto) throws Exception {
        try {
            if(userRepository.existsByEmail(createUserDto.email())) {
                throw new RuntimeException("User with email "+createUserDto.email()+" already exists");
            }
            else{
                String encodedPassword = passwordEncoder.encode(createUserDto.password());
                List<Role> roles = createUserDto.roleId().stream().map(this::getRoleById).toList();
                User user= new User();
                user.setEmail(createUserDto.email());
                user.setPassword(encodedPassword);
                user.setRoles(roles);
                userRepository.save(user);
                String content = "Kindly verify your account using the code below\n "
                        +diimuTokenService.createToken(user.getEmail());
                Mail mail = new Mail(new String[]{user.getEmail()},"ayo@diimu.net"
                        ,"Welcome To Diimu");
                emailService.sendSimpleMail(mail,content);
                return "User registered successfully.";
            }
        } catch (Exception e) {
            if(e.getMessage().contains("email")) {
                throw new BadRequestException("Error creating user: "+e.getMessage());
            }else {
                throw new Exception(e.getMessage());
            }
        }
    }

    @Override
    public String onBoardUser(String onBoardDto) throws Exception {
        return "";
    }


    private Role getRoleById(String roleId) {
        return roleRepository.findById(roleId).orElseThrow(
                () -> new ResourceNotFoundException("Role", "id", roleId));
    }
}

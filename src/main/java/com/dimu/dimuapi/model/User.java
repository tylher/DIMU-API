package com.dimu.dimuapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "users")
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String userId;

    private String email;

    @JsonIgnore
    private String password;

    @ManyToMany( fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles", // Join table name
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "userId"), // Foreign key in the join table referencing User
        inverseJoinColumns = @JoinColumn(name = "role_id",referencedColumnName = "roleId" ) // Foreign key in the join table referencing Role
    )
    private List<Role> roles;

    private String phoneNumber;

    private String gender;

    private LocalDate dateOfBirth;

    private String countryCode;

    private String firstName;

    private String lastName;

    private String profileImageUrl;

    private String state;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean onboarded = false;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean isVerified = false;
}

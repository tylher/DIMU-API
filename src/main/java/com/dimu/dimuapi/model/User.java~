package com.dimu.dimuapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Entity
@AllArgsConstructor
@Data
@Table(name = "users")
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String userId;

    private String email;

    @JsonIgnore
    private String password;

    @ManyToMany()
    @JoinTable(
        name = "user_roles", // Join table name
        joinColumns = @JoinColumn(name = "user_id"), // Foreign key in the join table referencing User
        inverseJoinColumns = @JoinColumn(name = "role_id") // Foreign key in the join table referencing Role
    )
    private List<Role> roles;

    private String phoneNumber;

    private String countryCode;

    private String fullName;

    private String gender;

    private String profileImageUrl;
}

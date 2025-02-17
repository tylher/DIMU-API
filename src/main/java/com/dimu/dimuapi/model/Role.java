package com.dimu.dimuapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
public class Role extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String roleId;

    @Column(unique = true, nullable = false)
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private List<User> users;
}

package com.kltn.authservice.business.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kltn.authservice.business.role.Role;
import com.kltn.authservice.utils.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "users")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 6, nullable = false)
    private String code;
    @Column(length = 255, nullable = false)
    private String firstname;
    @Column(length = 255, nullable = false)
    private String lastname;
    @Column(length = 12, nullable = false)
    private String phoneNumber;
    @Column(nullable = false)
    private Gender gender;
    @Column(length = 12, nullable = false)
    private LocalDate dateOfBirth;
    @Column(length = 1000, nullable = false)
    private String email;
    @Column(length = 2000)
    private String address;
    @Column(length = 5000, nullable = false)
    @JsonIgnore
    private String password;
    private String avatar;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "UserRoles",
            joinColumns = {@JoinColumn(name = "userId")},
            inverseJoinColumns = {@JoinColumn(name = "roleId")}
    )
    private Set<Role> roles;
}

enum Gender {
    MALE, FEMALE
}
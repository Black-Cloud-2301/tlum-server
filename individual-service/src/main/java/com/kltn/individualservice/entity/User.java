package com.kltn.individualservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kltn.individualservice.constant.Gender;
import com.kltn.individualservice.dto.request.UserRequestCRU;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 6, nullable = false, unique = true)
    private String code;
    @Column(nullable = false)
    private String firstname;
    @Column(nullable = false)
    private String lastname;
    @Column(length = 12, nullable = false)
    private String phoneNumber;
    @Column(nullable = false)
    private Gender gender;
    @Column(length = 12, nullable = false)
    private LocalDate dateOfBirth;
    @Column(length = 1023, nullable = false)
    private String email;
    @Column(length = 2047)
    private String address;
    @Column(length = 5000, nullable = false)
    @JsonIgnore
    private String password;
    private Long avatar;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private Set<Role> roles;

    public User(UserRequestCRU request) {
        this.id = request.getId();
        this.code = request.getCode();
        this.firstname = request.getFirstname();
        this.lastname = request.getLastname();
        this.phoneNumber = request.getPhoneNumber();
        this.email = request.getEmail();
        this.dateOfBirth = request.getDateOfBirth();
        this.gender = request.getGender();
        this.avatar = request.getAvatarId();
    }
}


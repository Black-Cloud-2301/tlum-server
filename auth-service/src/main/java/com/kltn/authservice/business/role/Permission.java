package com.kltn.authservice.business.role;

import jakarta.persistence.*;

@Entity
@Table(name = "permissions")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String function;
    private String action;
    private String module;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}

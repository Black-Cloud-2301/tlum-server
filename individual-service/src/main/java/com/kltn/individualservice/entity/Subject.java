package com.kltn.individualservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "subject")
public class Subject extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String code;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Integer credit;
    @Column
    private Integer requireCredit;
    @ManyToMany
    private List<Subject> requireSubjects;
    @Column
    private Integer hours;
    @Column
    private Double coefficient;
    @OneToMany
    private List<Major> majors;

    public Subject(Long id) {
        this.id = id;
    }
}
package com.kltn.individualservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "study_class")
public class StudyClass extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Integer semester;
    @Column(nullable = false)
    private Integer studentGroup;
    @Column(nullable = false)
    private Integer year;
    @Column(length = 1023)
    private String classesOfWeek;
    @ManyToOne
    private Teacher teacher;
    @Column
    private Integer totalStudent;
    @ManyToOne
    private Subject subject;
}

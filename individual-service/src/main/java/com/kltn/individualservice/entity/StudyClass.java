package com.kltn.individualservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "study_class")
public class StudyClass extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(length = 1023)
    private String classesOfWeek;
    @ManyToOne
    private Teacher teacher;
    @Column
    private Integer totalStudent;
    @ManyToOne
    private Subject subject;
    @ManyToOne
    private Semester semester;
}

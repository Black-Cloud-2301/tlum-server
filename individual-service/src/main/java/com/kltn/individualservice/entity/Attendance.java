package com.kltn.individualservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "attendance")
public class Attendance extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer weekNumber;

    @ManyToOne
    @JoinColumn(name = "student_study_class_id", nullable = false)
    private StudentStudyClass studentStudyClass;

    @Column(nullable = false)
    private Boolean attendance;
}
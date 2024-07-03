package com.kltn.individualservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "student_study_class")
public class StudentStudyClass extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Student studentId;

    @ManyToOne
    private StudyClass studyClassId;

    @Column
    private Double middleScore;

    @Column
    private Double finalScore;

}

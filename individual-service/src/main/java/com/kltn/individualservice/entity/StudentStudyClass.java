package com.kltn.individualservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "student_study_class")
public class StudentStudyClass extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Student student;

    @ManyToOne
    private StudyClass studyClass;

    @Column
    private Double middleScore;

    @Column
    private Double finalScore;

    public StudentStudyClass(Student student, StudyClass studyClass) {
        this.student = student;
        this.studyClass = studyClass;
    }
}

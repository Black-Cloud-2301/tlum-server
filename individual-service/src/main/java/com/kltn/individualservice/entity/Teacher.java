package com.kltn.individualservice.entity;

import com.kltn.individualservice.constant.EmployeeStatus;
import com.kltn.individualservice.constant.StudentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "teacher")
public class Teacher extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "id")
    private User user;

    @ManyToOne
    private StudyDepartment studyDepartment;

    @Column(nullable = false)
    private EmployeeStatus status;

    @Column
    private Long salary;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Subject> subjects;

    public Teacher(Long id) {
        this.id = id;
    }
}


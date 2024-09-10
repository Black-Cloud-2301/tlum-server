package com.kltn.individualservice.entity;

import com.kltn.individualservice.constant.StudentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "student")
public class Student extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    Long id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "id")
    private User user;

    @ManyToOne
    private Major majors;

    @Column(nullable = false)
    private StudentStatus status;

    @Column(nullable = false)
    private Integer schoolYear;

    public Student(User user) {
        this.user = user;
        this.status = StudentStatus.REGISTERED;
    }
}


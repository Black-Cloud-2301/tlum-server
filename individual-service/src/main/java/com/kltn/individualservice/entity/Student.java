package com.kltn.individualservice.entity;

import com.kltn.individualservice.constant.StudentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "student")
public class Student extends BaseEntity {
    @Id
    Long id;
    @MapsId
    @OneToOne
    @JoinColumn(name = "id")
    private User user;

    @ManyToOne
    private Major major;
    @Column(nullable = false)
    private StudentStatus status;

    public Student(User user) {
        this.user = user;
        this.status = StudentStatus.REGISTERED;
    }
}


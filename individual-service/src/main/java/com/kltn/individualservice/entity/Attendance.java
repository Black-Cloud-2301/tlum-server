package com.kltn.individualservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "attendance")
public class Attendance extends BaseEntity {
    @Id
    private Long id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "id")
    private StudentStudyClass studentStudyClass;

    @Column(nullable = false)
    private Integer weekNumber;

    @Column(nullable = false)
    private Boolean attendance;
}
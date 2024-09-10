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
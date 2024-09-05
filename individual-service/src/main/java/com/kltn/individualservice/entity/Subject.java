package com.kltn.individualservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "subject")
public class Subject extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String code;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Integer credit;
    @Column
    private Integer requireCredit;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Subject> requireSubjects;
    @Column
    private Integer hours;
    @Column
    private Double coefficient;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "subject_majors",
            joinColumns = @JoinColumn(name = "subject_id"),
            inverseJoinColumns = @JoinColumn(name = "majors_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"subject_id", "majors_id"})
    )
    private List<Major> majors;

    @Transient
    private List<Long> requireSubjectIds;
    @Transient
    private List<Long> majorIds;

    public Subject(Long id) {
        this.id = id;
    }
}
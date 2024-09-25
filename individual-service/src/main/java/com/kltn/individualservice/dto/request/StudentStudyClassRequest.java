package com.kltn.individualservice.dto.request;

import com.kltn.individualservice.constant.EntityStatus;
import com.kltn.individualservice.entity.Attendance;
import com.kltn.individualservice.entity.Student;
import com.kltn.individualservice.entity.StudyClass;
import com.kltn.individualservice.util.dto.PageableRequest;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class StudentStudyClassRequest {
    private Long id;

    private List<Attendance> attendances;

    private Double middleScore;

    private Double finalScore;
}

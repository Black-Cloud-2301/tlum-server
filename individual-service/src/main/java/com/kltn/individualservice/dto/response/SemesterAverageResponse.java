package com.kltn.individualservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SemesterAverageResponse {
    private String semester;
    private Double averageScore;
    private Long credits;
}

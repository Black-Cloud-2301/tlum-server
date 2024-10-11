package com.kltn.individualservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportCard {
    String subjectName;
    Integer attendance;
    Double middleScore;
    Double finalScore;
}

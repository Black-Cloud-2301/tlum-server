package com.kltn.individualservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentOptions implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String code;
    private String firstname;
    private String lastname;
}

package com.kltn.individualservice.graphql.dataFetcher;

import com.kltn.individualservice.constant.StudentStatus;
import com.kltn.individualservice.entity.Student;
import com.kltn.individualservice.service.StudentService;
import com.kltn.individualservice.service.UserService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import lombok.RequiredArgsConstructor;

import java.util.List;

@DgsComponent
@RequiredArgsConstructor
public class UserDataFetcher {

    private final UserService userService;
    private final StudentService studentService;

    @DgsQuery
    public List<Student> students(@InputArgument List<StudentStatus> studentStatuses) {
        return studentService.getStudents(studentStatuses);
    }
}

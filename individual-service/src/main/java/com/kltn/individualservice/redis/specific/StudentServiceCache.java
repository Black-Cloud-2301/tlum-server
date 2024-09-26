//package com.kltn.individualservice.redis.specific;
//
//import com.kltn.individualservice.redis.BaseRedisServiceImpl;
//import com.kltn.individualservice.service.StudentService;
//import com.kltn.individualservice.util.CommonUtil;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Service;
//
//@Service
//public class StudentServiceCache extends BaseRedisServiceImpl {
//    private static final String KEY = CacheKeys.STUDENT;
//
//    public StudentServiceCache(RedisTemplate<String, Object> redisTemplate, StudentService studentService, CommonUtil commonUtil) {
//        super(redisTemplate);
//    }
//
//    public String getKey() {
//        return KEY;
//    }
//
////    public void setStudent() {
////        this.hashSet(KEY, "ALL", getStudentResponses());
////    }
//
////    public List<StudentResponse> getStudentResponses() {
////        List<Student> students = studentService.getStudents(List.of(StudentStatus.values()));
////        return students.stream()
////                .map(StudentResponse::new)
////                .toList();
////    }
//
////    public void cacheStudentResponses(List<StudentResponse> studentResponses) {
////        this.hashSet(KEY, "ALL", studentResponses);
////    }
//
////    @SuppressWarnings("unchecked")
////    public List<StudentResponse> getStudents() {
////        return (List<StudentResponse>) this.hashGet(KEY, "ALL");
////    }
//}

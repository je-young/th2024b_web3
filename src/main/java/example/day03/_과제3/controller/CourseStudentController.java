package example.day03._과제3.controller;


import example.day03._과제3.model.dto.CourseDto;
import example.day03._과제3.model.dto.StudentDto;
import example.day03._과제3.service.CourseStudentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor // 생성자 주입
@RequestMapping("/api/courses") // http://localhost:8080/api/courses 공통 URL
public class CourseStudentController {

    private final CourseStudentService courseStudentService;

    // 1. 과정 등록 API
    @PostMapping
    public ResponseEntity<CourseDto> createCourse(@RequestBody CourseDto courseDto) {
        CourseDto createdCourse = courseStudentService.createCourse( courseDto );
        return new ResponseEntity<>(createdCourse, HttpStatus.CREATED); // 201 Created 상태 코드와 함께 반환
    } // end createCourse()

    // 2. 과정 전체 조회 API
    @GetMapping
    public ResponseEntity<List<CourseDto>> getAllCourses() {
        List<CourseDto> courses = courseStudentService.getAllCourses();
        return ResponseEntity.ok(courses); // 200 OK
    }  // end getAllCourses()

    // 3. 특정 과정에 수강생 등록 API
    @PostMapping("/{courseId}/students")
    public ResponseEntity<StudentDto> addStudentToCourse(@PathVariable Long courseId, @RequestBody StudentDto studentDto) {
        StudentDto addedStudent = courseStudentService.addStudentToCourse( courseId, studentDto );
        return new ResponseEntity<>(addedStudent, HttpStatus.CREATED); // 201 Created
    } // end addStudentToCourse()

    // 4. 특정 과정에 수강생 전체 조회 API
    @GetMapping("/{courseId}/students")
    public ResponseEntity<List<StudentDto>> getStudentsByCourse(@PathVariable Long courseId) {
        List<StudentDto> students = courseStudentService.getStudentsByCourse(courseId);
        return ResponseEntity.ok(students); // 200 OK
    } // end getStudentsByCourse()

    // ---- 예외 처리 핸들러 (간단 예시) ----
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFound(EntityNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND); // 404 Not Found
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        // 로깅 등 추가 처리 가능
        return new ResponseEntity<>("서버 처리 중 오류가 발생했습니다: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
    }

} // end class















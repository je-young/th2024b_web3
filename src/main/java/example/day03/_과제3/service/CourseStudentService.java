package example.day03._과제3.service;


import example.day03._과제3.model.dto.CourseDto;
import example.day03._과제3.model.dto.StudentDto;
import example.day03._과제3.model.entity.Course;
import example.day03._과제3.model.entity.Student;
import example.day03._과제3.model.repository.CourseRepository;
import example.day03._과제3.model.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional // 클래스 레벨 트랜젝션 적용
@RequiredArgsConstructor // final 필드 생성자 주입
public class CourseStudentService {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    // 1. 과정 등록
    public CourseDto createCourse( CourseDto courseDto ) {
        Course course = courseDto.toEntity();
        Course savedCourse = courseRepository.save( course );
        return savedCourse.toDto();
    } // end createCourse()

    // 2. 과정 전체 조회
    public List<CourseDto> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream()
                .map(Course::toDto)
                .collect(Collectors.toList());
    } // end getAllCourses()

    // 3. 특정 과정에 수강생 등록
    public StudentDto addStudentToCourse(Long courseId, StudentDto studentDto) {
        // 3-1 과정(Course) 엔티티 조회
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 과정을 찾을 수 없습니다:" + courseId));

        // 3-2 학생 DTO --> 학생 Entity 변환 (Course 정보는 아직 없음)
        Student student = studentDto.toEntity();

        // 3-3 관계 설정 : 학생 엔티티에 과정 엔티티 설정 (연관관계의 주인인 Student에서 설정)
        student.setCourse(course);

        // 3-4 과정 엔티티의 학생 목록에도 추가(양방향 평의 메소드)
        course.getStudents().add( student ); // cascade = PERSIST 또는 ALL 이면 save 시 자동 전파되지만 명시적 추가도 가능

        // 3-5 학생 엔티티 저장 (Cascade 설정에 따라 Course를 다시 save할 필요 없음)
        Student savedStudent = studentRepository.save( student );

        // 3-6 저장된 학생 엔티티를 DTO로 변환하여 반환
        return savedStudent.toDto();
    } // end addStudentToCourse()

    // 4. 특정 과정에 수강생 전체 조회
    public List<StudentDto> getStudentsByCourse(Long courseId) {
        // 4-1 과정(Course) 엔티티 조회
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 과정을 찾을 수 없습니다:" + courseId));

        // 4-2 Course 엔티티를 통해 연관된 Student 목록 가져오기(LAZY 로딩시 여기서 쿼리 발생)
        List<Student> students = course.getStudents();

        // 4-3 학생 엔티티 목록 --> 학생 DTO 목록 변환
        return students.stream()
                .map(Student::toDto) // .map(student --> student.toDto()) 와 동일
                .collect(Collectors.toList());
    } // end getStudentsCourse()

} // end class




















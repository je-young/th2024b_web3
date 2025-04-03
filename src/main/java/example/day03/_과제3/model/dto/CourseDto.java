package example.day03._과제3.model.dto;


import example.day03._과제3.model.entity.Course;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseDto {
    private Long courseId;
    private String courseName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // 학생 목록은 CourseDto 에 포함하지 않음 (필요 시 별도 DTO 설계 또는 List<StudentDto> 반환)

    // DTO --> Entity 변환 메서드
    public Course toEntity() {
        return Course.builder()
                .courseId( this.courseId ) // ID 는 보통 null 이거나 업데이트 시 사용
                .courseName( this.courseName )
                .build();
        // students 리스트는 Service 계층에서 관리
    } // end toEntity()
} // end class

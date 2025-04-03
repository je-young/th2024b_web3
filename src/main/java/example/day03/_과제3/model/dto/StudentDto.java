package example.day03._과제3.model.dto;


import example.day03._과제3.model.entity.Student;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDto {
    private Long studentId;
    private String studentName;
    private Long courseId; // 학생 등록시 또는 조회시 과정 ID를 포함
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // DTO --> Entity 변환 메서드
    public Student toEntity() {
        return Student.builder()
                .studentId( this.studentId )
                .studentName( this.studentName )
                .build();
    } // end toEntity()

} // end class

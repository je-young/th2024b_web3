package example.day03._과제3.model.entity;

import example.day03._과제3.config.BaseTime;
import example.day03._과제3.model.dto.CourseDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "course")
public class Course extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId; // 과정번호 [PK]

    @Column(nullable = false, length = 100)
    private String courseName; // 과정명

    // 양방향 관계 설정 (1:N 에서 '1' 쪽)
    @OneToMany(mappedBy = "course" , cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    @ToString.Exclude // 순환참조 방지
    @Builder.Default // 빌더패턴 사용시 리스트 초기화
    private List<Student> students = new ArrayList<>();

    // Entity --> DTO 변환 메서드
    public CourseDto toDto() {
        return CourseDto.builder()
                .courseId( this.courseId )
                .courseName( this.courseName )
                .createdAt( this.getCreatedAt() )
                .updatedAt( this.getUpdatedAt() )
                .build();
    } // end toDto()


} // end class

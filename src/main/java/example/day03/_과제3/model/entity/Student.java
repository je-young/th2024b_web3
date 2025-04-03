package example.day03._과제3.model.entity;


import example.day03._과제3.config.BaseTime;
import example.day03._과제3.model.dto.StudentDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "student")
public class Student extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentId; // 학생번호 [PK]

    @Column(nullable = false, length = 50)
    private String studentName; // 학생명

    // 단방향/양방향 관계 설정 (N:1 에서 'N' 쪽, 관계의 주인)
    @ManyToOne(fetch = FetchType.LAZY) // N:1 관계, 기본은 EAGER 이지만 LAZY 권장
    @JoinColumn(name = "course_Id") // 생성될 외래키 컬럼명 지정(기본값 : course_courseId)
    private Course course; // 과정번호(FK), 이 필드가 외래키를 관리함

    // Entity --> DTO 변환 메서드
    public StudentDto toDto() {
        return StudentDto.builder()
                .studentId( this.studentId )
                .studentName( this.studentName )
                // Course 정보가 필요하다면 DTO에 추가
                .courseId(this.course != null ? this.course.getCourseId() : null)
                .createdAt( this.getCreatedAt() )
                .updatedAt( this.getUpdatedAt() )
                .build();
    } // end toDto()

} // end class

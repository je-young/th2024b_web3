package example.day01._리포지토리;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity(name = "student1") // DB 테이블 매핑
@Data // 롬복
public class ExamEntity {
    @Id
    private String id; // 1. 학번 , primaryh key

    @Column(nullable = false)
    private String name; // 2. 이름 , not null

    @Column
    private int kor; // 3. 국어점수

    @Column
    private int eng; // 4. 영어점수

} // end class

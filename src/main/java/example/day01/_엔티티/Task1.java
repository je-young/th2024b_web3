package example.day01._엔티티;


import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity // 해당 클래스는 DB 테이블 매핑
@Table(name = "task1todo") // DB 테이블명 정의
public class Task1 {

    @Id // primary key 제약조건 정의
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private int id;

    @Column(nullable = false, length = 100) // not null 제약조건, 길이 100
    private String title;

    @Column(nullable = false) // not null 제약조건
    private boolean state;

    @Column(nullable = false) // not null 제약조건
    private LocalDate createat;

    @Column(nullable = false) // not null 제약조건
    private LocalDateTime updateat;

} // end class

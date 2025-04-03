package example.day03._JPA연관관계;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Entity // 해당 클래스는 데이터베이스 와 영속관계로 사용
@Table(name = "day03category") // DB 테이블명 정의
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private int cno; // 카테고리번호
    private String cname; // 카테고리명

    // + 양방향 , 게시물 여러개 참조
    // @OneToMany(mappedBy = "단방향의멤버변수명") // 양방향 참조테이블은 자바에서만 참조한다.
    @OneToMany(mappedBy = "category" , cascade = CascadeType.ALL , fetch = FetchType.LAZY ) // 제약조건옵션 : 만약에 PK가 삭제되면 FK????
    private List<Board> boardList = new ArrayList<>();

}

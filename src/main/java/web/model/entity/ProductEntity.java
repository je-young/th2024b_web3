package web.model.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pno; // 제품 식별번호

    @Column(nullable = false)
    private String pname; // 제품명

    @Column(columnDefinition = "longtext") // mysql 네이티브 타입
    private String pcontent; // 제품설명

    @Column(nullable = false)
    @ColumnDefault("0") // defalut 0
    private int pprice; // 제품가격

    @Column(nullable = false)
    @ColumnDefault("0") // defalut 0
    private int pview; // 조회수

    // * 단방향 : 참초할 PK 필드가 존재하는 엔티티 필드 생성, 자바 에서는 엔티티 이지만 db에서는 mno 표현
    @ManyToOne
    @JoinColumn(name = "mno") // fk 필드명 정의
    private MemberEntity memberEntity;

    @ManyToOne
    @JoinColumn(name = "cno") // fk 필드명 정의
    private CategoryEntity categoryEntity;

    // * 양방향 :
    @OneToMany(mappedBy = "productEntity , cascade = CascadeType.ALL , fetch = FetchType.LAZY)")
    @ToString.Exclude // 순환참조 방지
    @Builder.Default // 빌더패턴 사용시 리스트 초기화
    private List<ImgEntity> imgEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "productEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude // 순환참조 방지
    @Builder.Default // 빌더패턴 사용시 리스트 초기화
    private List<ReplyEntity> replyEntityList = new ArrayList<>();

}

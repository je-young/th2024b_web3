package web.model.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CategoryEntity extends BaseTime {
    @Id // premary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private long cno; // 카테고리 식별번호

    @Column(nullable = false , length = 100) // null 불가, 길이 100
    private String cname; // 카테고리 이름

    // * 양방향
    @OneToMany(mappedBy = "categoryEntity" , cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    @ToString.Exclude // 순환참조 방지
    @Builder.Default // 빌더패턴 사용시 리스트 초기화
    private List<ProductEntity> productEntityList = new ArrayList<>();
}

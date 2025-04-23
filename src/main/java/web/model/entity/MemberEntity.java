package web.model.entity;

import jakarta.persistence.*;
import lombok.*;
import web.model.dto.MemberDto;

import java.util.ArrayList;
import java.util.List;

@Entity // JPA 엔티티
@Table(name = "member") // DB 테이블명
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private int mno; // 회원번호
    private String memail; // email
    private String mpwd; // 비밀번호
    private String mname; // 이름

    // * 양방향 : FK 엔티티를 여러개 가지므로 List 타입으로 만든다.
    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude // 순환참조 방지
    @Builder.Default // 빌더패턴 사용시 리스트 초기화
    private List<ProductEntity> productEntityList = new ArrayList<>();

    // @OneToMany(mappedBy = "FK엔티티자바필드명")
    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude // 순환참조 방지
    @Builder.Default // 빌더패턴 사용시 리스트 초기화
    private List<ReplyEntity> replyEntityList = new ArrayList<>();

    // entity -> dto
    public MemberDto toDto() {
        return MemberDto.builder()
                .mno(this.mno)
                .memail(this.memail)
                .mname(this.mname)
                .build();
    } // end toDto
} // end MemberEntity

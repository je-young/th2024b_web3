// Review.java : Review Entity 클래스

package bookrecommend.model.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity // JPA을 활용한 Entity
@EntityListeners(AuditingEntityListener.class) // 엔티티에 이벤트 리스너를 등록하는 어노테이션
// JPA에서 제공하는 기본 엔티티 리스너로, 엔티티의 생성일자, 수정일자, 생성자, 수정자 등의 감사(auditing) 정보를 자동으로 관리
public class Review {

    @Id // Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
    private Long id; // PK

    @Column(nullable = false , length = 500) // NOT NULL, VARCHAR(500)
    private String content; // 책 리뷰

    @Column(nullable = false) // NOT NULL
    private String password; // 비밀번호

    @CreatedDate
    @Column(updatable = false) // NOT NULL
    private LocalDateTime createdAt; // 생성일

    // 다대일(N:1) 관계를 설정하는 어노테이션
    @ManyToOne(fetch = FetchType.LAZY) // 지연 로딩
    // 외래 키 컬럼을 지정하는 어노테이션
    // name = "book_id": 데이터베이스 테이블에서 외래 키 컬럼명이 "book_id". nullable = false: 이 컬럼은 NULL 값을 허용하지 않는다.
    @JoinColumn(name = "book_id" , nullable = false)
    private Book book;

    // 생성자 (Service 에서 사용)
    public Review(String content, String password, Book book) {
        this.content = content;
        this.password = password;
        this.book = book;
    } // end constructor

} // end class

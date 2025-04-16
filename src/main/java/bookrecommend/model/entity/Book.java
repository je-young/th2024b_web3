// Book.java : Book Entity 클래스

package bookrecommend.model.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
    private Long id;

    @Column(nullable = false) // NOT NULL
    private String title; // 책 제목

    @Column(nullable = false) // NOT NULL
    private String author; // 책 저자

    @Column(length = 1000) // VARCHAR(1000)
    private String description; // 책 소개

    @Column(nullable = false) // NOT NULL
    private String password; // 책 비밀번호

    @CreatedDate
    @Column(updatable = false) // NOT NULL
    private LocalDateTime createdAt; // 책 생성날짜

    @OneToMany(mappedBy = "book" , cascade = CascadeType.ALL , orphanRemoval = true , fetch = FetchType.LAZY)
        // mappedBy = "book": Review 엔티티의 'book' 필드에 의해 관리된다는 의미.
        // cascade = CascadeType.ALL: 책에 대한 모든 작업(저장, 삭제 등)이 연관된 리뷰들에도 자동으로 적용.
        // orphanRemoval = true: 책에서 리뷰가 제거되면 해당 리뷰는 데이터베이스에서도 삭제.
        //fetch = FetchType.LAZY: 책을 조회할 때 리뷰들은 실제로 필요할 때까지 로딩되지 않음.
    private List<Review> reviews = new ArrayList<>(); // 리뷰와의 일대다 관계

    public Book(String title, String author, String description, String password) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.password = password;
    } // end constructor

    public void addReview(Review review) {
        this.reviews.add(review);
        review.setBook(this);
    } // end addReview

} // end class

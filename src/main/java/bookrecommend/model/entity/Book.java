// Book.java : Book Entity 클래스

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

    public Book(String title, String author, String description, String password) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.password = password;

    } // end constructor

} // end class

package example.day01._과제;


import jakarta.persistence.*;
import lombok.Data;

@Entity // DB 테이블과 매핑
@Data // 롬복
public class BookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long bookId; // 1. 도서ID , primary key

    @Column(unique = false)
    private String bookName; // 2. 도서명 , not null

    @Column(unique = false)
    private String author; // 저자

    @Column(unique = false)
    private String publisher; // 출판사

    @Column(unique = false)
    private Integer publicationYear; // 출판년도

} // end class

// BookResponse.java : Book 생성 응답 DTO

package bookrecommend.model.dto;


import bookrecommend.model.entity.Book;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BookResponse {
    private Long id;
    private String title;
    private String author;
    private String description;
    private LocalDateTime createdAt;

    public BookResponse(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.description = book.getDescription();
        this.createdAt = book.getCreatedAt();
    } // end constructor
} // end class

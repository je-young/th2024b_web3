package example.day01._과제;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books") // 도서 API 공통 URL
public class BookController {

    private final BookService bookService;

    // 모든 도서 목록 조회
    @GetMapping // http://localhost:8080/api/books
    public List<BookEntity> getAllBooks() {
        return bookService.getAllBooks();
    } // end getAllBooks

    // 도서 ID로 도서 조회
    @GetMapping("/{bookId}") // http://localhost:8080/api/books/1
    public BookEntity getBookById(@PathVariable Long bookId) {
        return bookService.getBookById(bookId);
    } // end getBookById

    // 도서 등록
    @PostMapping // http://localhost:8080/api/books // {"bookName": "해리포터와 마법사의 돌","author": "J.K. 롤링","publisher": "스콜라스틱","publicationYear": 1997}
    public BookEntity createBook(@RequestBody BookEntity bookEntity) {
        return bookService.createBook(bookEntity);
    } // end createBook

    // 도서 수정
    @PutMapping("/{bookId}") // http://localhost:8080/api/books/1 // {"bookId": 1,"bookName": "해리포터와 비밀의 방","author": "J.K. 롤링","publisher": "스콜라스틱","publicationYear": 1998}
    public boolean updateBook(@PathVariable Long bookId,  @RequestBody BookEntity bookEntity) {
        return bookService.updateBook(bookEntity);
    } // end updateBook

    // 도서 삭제
    @DeleteMapping("/{bookId}") // http://localhost:8080/api/books/1
    public void deleteBook(@PathVariable Long bookId) {
        bookService.deleteBook(bookId);
    } // end deleteBook

} // end class

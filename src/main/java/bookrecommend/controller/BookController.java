// BookController.java :  Book Controller 클래스

package bookrecommend.controller;


import bookrecommend.model.dto.BookCreateRequest;
import bookrecommend.model.dto.BookResponse;
import bookrecommend.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    // [1] 책 추천 등록 기능
    // { "title": "모순", "author": "양귀자", "description": "안진진이라는 여주인공의 삶과 선택.", "password": "mypassword123" }
    @PostMapping("/books") // [POST] "http://localhost:8080/api/books"
    public ResponseEntity<BookResponse> createBook(@RequestBody BookCreateRequest request) {
    BookResponse response = bookService.createBook(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } // end createBook

    // [2] 책 목록 및 상세 조회 기능
        // [2-1] 책 목록 조회
    @GetMapping("/books") // [GET] "http://localhost:8080/api/books"
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        List<BookResponse> books = bookService.getAllBooks();
        return ResponseEntity.ok(books); // HTTP 200 OK 와 함께 책 목록 반환
    } // end getAllBooks

        // [2-2] 책 상세 조회
    @GetMapping("/books/{id}") // [GET] "http://localhost:8080/api/books/{id}"
    public ResponseEntity<BookResponse> getBookById(@PathVariable Long id) {
        BookResponse book = bookService.getBookById(id);
        return ResponseEntity.ok(book); // HTTP 200 OK 와 함께 책 상세 정보 반환
    } // end getBookById

    // BookService에서 IllegalArgumentException이 발생하는 경우를 처리하는 핸들러
    // 예를 들어, 책의 ID를 잘못 입력하여 찾을 수 없는 경우
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        // 404 NOT FOUND 상태 코드와 함께 예외 메시지를 반환
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("책을 찾을 수 없습니다. ID를 확인해주세요. " + e.getMessage());
    } // end handleIllegalArgumentException

} // end class

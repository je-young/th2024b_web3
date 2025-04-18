// BookController.java :  Book Controller 클래스

package bookrecommend.controller;


import bookrecommend.model.dto.BookCreateRequest;
import bookrecommend.model.dto.BookResponse;
import bookrecommend.model.dto.BookUpdateRequest;
import bookrecommend.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // CORS
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
        // [2-1] 전체 책 목록 조회
    @GetMapping("/books") // [GET] "http://localhost:8080/api/books"
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        List<BookResponse> books = bookService.getAllBooks();
        return ResponseEntity.ok(books); // HTTP 200 OK 와 함께 책 목록 반환
    } // end getAllBooks

        // [2-2] 특정 책 상세 조회
    @GetMapping("/books/{bookId}") // [GET] "http://localhost:8080/api/books/{bookId}"
    public ResponseEntity<BookResponse> getBookById(@PathVariable Long bookId) {
        BookResponse book = bookService.getBookById(bookId);
        return ResponseEntity.ok(book); // HTTP 200 OK 와 함께 책 상세 정보 반환
    } // end getBookById

    // [3] 책 수정 및 삭제 기능
        // [3-1] 책 수정
        // { "title": "모순 (수정판)", "author": "양귀자 작가님", "description": "안진진의 삶과 선택, 그리고 깊어진 이야기.", "password": "mypassword123" }
    @PutMapping("/books/{bookId}") // [PUT] "http://localhost:8080/api/books/{bookId}"
    public ResponseEntity<BookResponse> updateBook(@PathVariable Long bookId, @RequestBody BookUpdateRequest request) {
        BookResponse updatedBook = bookService.updateBook(bookId, request);
        return ResponseEntity.ok(updatedBook); // HTTP 200 OK 와 함께 수정된 정보 반환
    } // end updateBook

        // [3-2] 책 정보 삭제 : http://localhost:8080/api/books/1?password=mypassword123
        // { "password": "mypassword123" }
    @DeleteMapping("/books/{bookId}") // [DELETE] "http://localhost:8080/api/books/{bookId}"
    public ResponseEntity<Void> deleteBook(@PathVariable Long bookId, @RequestParam String password) {
        bookService.deleteBook(bookId, password); // 서비스 호출 시 password 직접 전달
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    } // end deleteBook

    // BookService에서 IllegalArgumentException이 발생하는 경우를 처리하는 핸들러
    // 예를 들어, 책의 ID를 잘못 입력하여 찾을 수 없는 경우
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        // 메시지 내용에 따라 상태 코드를 다르게 줄 수도 있음
        if (e.getMessage().contains("찾을 수 없습니다")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } else {
            // 비밀번호 불일치 또는 다른 IllegalArgumentException 의 경우
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    } // end handleIllegalArgumentException

} // end class

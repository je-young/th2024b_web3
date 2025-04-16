// BookController.java :  Book Controller 클래스

package bookrecommend.controller;


import bookrecommend.model.dto.BookCreateRequest;
import bookrecommend.model.dto.BookResponse;
import bookrecommend.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    } // end method
} // end class

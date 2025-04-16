// ReviewController.java : Review Controller 클래스

package bookrecommend.controller;


import bookrecommend.model.dto.ReviewCreateRequest;
import bookrecommend.model.dto.ReviewResponse;
import bookrecommend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api") // 기본 경로 URL : "http://localhost:8080/api"
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // [1] 특정 책에 리뷰 작성 API
    // { "content": "이 책 정말 감명 깊게 읽었습니다!", "password": "reviewpass1" }
    // { "content": "저는 조금 지루했어요.", "password": "reviewpass2" }
    @PostMapping("/books/{bookId}/reviews") // [POST] "http://localhost:8080/api/books/{bookId}/reviews"
    public ResponseEntity<ReviewResponse> createReview(@PathVariable Long bookId, @RequestBody ReviewCreateRequest request) {
        ReviewResponse cratedReview = reviewService.createReview(bookId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(cratedReview);
    } // end createReview


    // [2] 특정 책의 모든 리뷰 조회 API
    @GetMapping("/books/{bookId}/reviews") // [GET] "http://localhost:8080/api/books/{bookId}/reviews"
    public ResponseEntity<List<ReviewResponse>> getReviewsByBookId(@PathVariable Long bookId) {
        List<ReviewResponse> reviews = reviewService.getReviewsByBookId(bookId);
        return ResponseEntity.ok(reviews);
    } // end getReviewsByBookId

    // [3] ReviewService 에서 IllegalArgumentException 발생 시 처리할 핸들러
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        // 여기서는 책 ID 관련 예외만 발생 가능성이 높으므로 NOT_FOUND 로 처리
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } // end handleIllegalArgumentException

} // end class

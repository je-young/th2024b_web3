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
@CrossOrigin(origins = "*") // CORS
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

    // [3] 특정 리뷰 삭제 API (비밀번호는 Query Parameter 로 받음)
    @DeleteMapping("/reviews/{reviewId}") // [DELETE] "http://localhost:8080/api/reviews/1?password=reviewpass1 (ID 1번 리뷰 삭제)"
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long reviewId,
            @RequestParam String password) {
        reviewService.deleteReview(reviewId, password);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content 반환
    } // end deleteReview

    // [3] ReviewService 에서 IllegalArgumentException 발생 시 처리할 핸들러
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        // 메시지 내용에 따라 상태 코드를 다르게 설정
        if (e.getMessage().contains("찾을 수 없습니다")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } else if (e.getMessage().contains("비밀번호")) { // 비밀번호 관련 오류 메시지 포함 시
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } else {
            // 기타 IllegalArgumentException (예상치 못한 경우)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 요청입니다: " + e.getMessage());
        } // end if
    } // end handleIllegalArgumentException

} // end class

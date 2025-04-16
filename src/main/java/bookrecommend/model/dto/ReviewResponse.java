// ReviewResponse.java : Review 생성 응답 DTO

package bookrecommend.model.dto;


import bookrecommend.model.entity.Review;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReviewResponse {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    // private Long bookId; // 필요하다면 책 ID도 포함 가능

    public ReviewResponse(Review review) {
        this.id = review.getId();
        this.content = review.getContent();
        this.createdAt = review.getCreatedAt();
        // this.bookId = review.getBook().getId(); // 지연 로딩 주의
    } // end constructor

} // end class

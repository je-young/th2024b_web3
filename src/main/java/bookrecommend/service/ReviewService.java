// ReviewService.java : Review Service 클래스

package bookrecommend.service;


import bookrecommend.model.dto.ReviewCreateRequest;
import bookrecommend.model.dto.ReviewResponse;
import bookrecommend.model.entity.Book;
import bookrecommend.model.entity.Review;
import bookrecommend.model.repository.BookRepository;
import bookrecommend.model.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository; // 책을 찾기 위해 필요
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화을 위해 필요

    // [1] 특정 책에 리뷰를 작성
    @Transactional // 데이터 변경이 있으므로 @Transactional 필요
    public ReviewResponse createReview(Long bookId, ReviewCreateRequest request) {

        // 1. 책 조회 (없으면 예외 발생)
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 작성할 책을 찾을 수 없습니다: " + bookId));

        // 2. 비밀번호 암호화 및 Review 객체 생성
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        Review review = new Review(request.getContent(), encodedPassword, book);

        // 3. Review 저장 및 응답 반환
        Review savedReview = reviewRepository.save(review);
        // Book.addReview(review); // CascadeType.All 이므로 명시적 추가 불필요 or 연관관계 편의 메소드 사용 시 추가 가능
        return new ReviewResponse(savedReview);
    } // end createReview

    // [2] 특정 책에 달린 모든 리뷰를 조회
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByBookId(Long bookId) {

        // 1. 책 존재 여부 확인 (선택적이지만, 없는 책 ID 요청 시 빈 리스트 반환 또는 예외 발생 등 정책 결정 필요)
        if (!bookRepository.existsById(bookId)) {
            throw new IllegalArgumentException("리뷰를 조회할 책을 찾을 수 없습니다 : " + bookId);
        } // end if

        // 2. 해당 책 ID의 모든 리뷰 조회
        List<Review> reviews = reviewRepository.findAllByBookId(bookId);

        // 3. ReviewResponse 리스트로 변환하여 반환
        return reviews.stream()
                .map(ReviewResponse::new)
                .collect(Collectors.toList());
    } // end getReviewsByBookId

} // end class

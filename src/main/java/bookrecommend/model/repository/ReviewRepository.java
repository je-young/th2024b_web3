// ReviewRepository.java : Review Repository 인터페이스

package bookrecommend.model.repository;


import bookrecommend.model.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 특정 책 ID에 해당하는 모든 리뷰를 조회하는 메소드 (JPQL 또는 메소드 이름 규칙 사용)
    List<Review> findAllByBookId(Long bookId);

    // 또는 JPQL 사용 시
    // @Query("SELECT r FROM Review r WHERE r.book.id = :bookId")
    // List<Review> findReviewsByBookId(@Param("bookId") Long bookId);

} // end ReviewRepository

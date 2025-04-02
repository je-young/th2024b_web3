package example.day01._과제;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // @Repository : 데이터베이스 연동을 위한 인터페이스
public interface BookEntityRepository extends JpaRepository<BookEntity, Long> {
} // end interface

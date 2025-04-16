// BookRepository.java : Book Repository 인터페이스

package bookrecommend.model.repository;


import bookrecommend.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}

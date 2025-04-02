package example.day01._과제;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookEntityRepository bookEntityRepository;

    // 모든 도서 목록 조회
    public List<BookEntity> getAllBooks() {
        return bookEntityRepository.findAll();
    } // end getAllBooks

    // 도서 ID로 도서 조회
    public BookEntity getBookById(Long bookId) {
        return bookEntityRepository.findById(bookId).orElse(null);
    } // end getBookById

    // 도서 등록
    public BookEntity createBook(BookEntity bookEntity) {
        return bookEntityRepository.save(bookEntity);
    } // end createBook

    // 도서 수정
    @Transactional // 트랜잭션 처리를 위한 어노테이션
    public boolean updateBook(BookEntity bookEntity) {
        Optional<BookEntity> optionalBookEntity = bookEntityRepository.findById(bookEntity.getBookId());
        if (optionalBookEntity.isPresent()) {
            BookEntity entity = optionalBookEntity.get();
            entity.setBookName(bookEntity.getBookName());
            entity.setAuthor(bookEntity.getAuthor());
            entity.setPublisher(bookEntity.getPublisher());
            entity.setPublicationYear(bookEntity.getPublicationYear());
            return true;
        } // end if
        return false;
    } // end updateBook

    // 도서 삭제
    public void deleteBook(Long bookId) {
        bookEntityRepository.deleteById(bookId);
    } // end deleteBook

} // end class

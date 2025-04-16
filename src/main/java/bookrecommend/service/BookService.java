// BookService.java : Book Service 클래스

package bookrecommend.service;


import bookrecommend.model.dto.BookCreateRequest;
import bookrecommend.model.dto.BookResponse;
import bookrecommend.model.entity.Book;
import bookrecommend.model.repository.BookRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service // 스프링의 서비스 계층 컴포넌트임을 표시
@RequiredArgsConstructor // final 필드에 대한 생성자를 자동으로 생성 (Lombok)
public class BookService {

    // 도서 정보를 저장하고 조회하기 위한 리포지토리
    private final BookRepository bookRepository;
    // 비밀번호를 안전하게 암호화하기 위한 인코더
    private final PasswordEncoder passwordEncoder;

    // [1] 책 추천 등록 기능
    @Transactional
    public BookResponse createBook(BookCreateRequest request) {
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        // 새로운 도서 엔티티 생성
        Book book = new Book(
                request.getTitle(),
                request.getAuthor(),
                request.getDescription(),
                encodedPassword);
        // 도서 정보를 데이터베이스에 저장하고 저장된 결과를 반환
        Book savedBook = bookRepository.save(book);
        return new BookResponse(savedBook);
    } // end createBook

    // [2] 책 목록 및 상세 조회 기능
        // [2-1] 책 목록 조회
    @Transactional(readOnly = true) // 조회 기능이므로 readOnly=true (성능 향상)
    public List<BookResponse> getAllBooks() {
        return bookRepository.findAll().stream() // 모든 책을 조회
                .map(BookResponse::new) // Book 엔티티를 BookResponse DTO로 변환
                .collect(Collectors.toList()); // 리스트로 수집하여 반환
    } // end getAllBooks

        // [2-2] 책 상세 조회
    @Transactional(readOnly = true)
    public BookResponse getBookById(Long id) {
        Book book = bookRepository.findById(id) // ID로 책 조회
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 책을 찾을 수 없습니다." + id)); // 없으면 예외 발생
        return new BookResponse(book); // 찾았으면 BookResponse 로 변환하여 반환
    } // end getBookById

} // end class

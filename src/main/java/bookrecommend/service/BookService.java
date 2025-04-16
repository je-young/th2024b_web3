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

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public BookResponse createBook(BookCreateRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        Book book = new Book(
                request.getTitle(),
                request.getAuthor(),
                request.getDescription(),
                encodedPassword);
        Book savedBook = bookRepository.save(book);
        return new BookResponse(savedBook);
    } // end method

} // end class

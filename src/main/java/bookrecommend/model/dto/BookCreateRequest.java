// BookCreateRequest.java : Book 생성 요청 DTO

package bookrecommend.model.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookCreateRequest {
    private String title; // 책 제목
    private String author; // 책 작가
    private String description; // 책 소개
    private String password; // 책 비밀번호
} // end class

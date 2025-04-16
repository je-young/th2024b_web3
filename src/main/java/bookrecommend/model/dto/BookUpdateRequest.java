// BookUpdateRequest.java : 수정할 내용( 제목, 저자, 소개 )과 검증용 비밀번호를 포함

package bookrecommend.model.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookUpdateRequest {
    private String title; // 책 제목
    private String author; // 책 저자
    private String description; // 책 소개
    private String password; // 비밀번호 검증용
} // end class

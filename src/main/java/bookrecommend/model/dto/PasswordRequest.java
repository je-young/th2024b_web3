// PasswordRequest.java: 삭제 시 비밀번호만 전달받기 위한 간단한 DTO

package bookrecommend.model.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordRequest {
    private String password;
}

// ReviewCreateRequest.java : Review 생성 요청 DTO

package bookrecommend.model.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewCreateRequest {
    private  String content;
    private  String password;
}

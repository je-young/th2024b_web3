package example.day04.todo.model.entity;


import example.day04.todo.model.dto.TodoDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // 데이터베이스의 테이블과 영속 관계
@Table(name = "todo") // 데이터베이스의 테이블명 정의
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoEntity extends BaseTime{

    @Id // 테이블의 PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private int id; // 할일 식별번호
    private String title; // 할일 제목
    private String content; // 할일 내용
    private boolean done; // 할일 상태

    // entity --> dto 변환 함수.
    public TodoDto toDto() {
        // entity 에서 dto로 변환할 필드 선택하여 dto 객체 만들기
        return TodoDto.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .done(this.done)
                .createdAt(this.getCreatedAt()) // BaseTime에 있는 엔티티를 사용
                .build();
    } // end toDto
} // end class

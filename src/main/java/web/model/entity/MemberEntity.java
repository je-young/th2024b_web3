package web.model.entity;

import jakarta.persistence.*;
import lombok.*;
import web.model.dto.MemberDto;

@Entity // JPA 엔티티
@Table(name = "member") // DB 테이블명
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private int mno;
    private String memail; // email
    private String mpwd; // 비밀번호
    private String mname; // 이름
    // entity -> dto
    public MemberDto toDto() {
        return MemberDto.builder()
                .mno(this.mno)
                .memail(this.memail)
                .mpwd(this.mpwd)
                .mname(this.mname)
                .build();
    } // end toDto
} // end MemberEntity

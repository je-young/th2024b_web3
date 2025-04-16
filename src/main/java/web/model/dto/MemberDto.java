package web.model.dto;

import lombok.*;
import web.model.entity.MemberEntity;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class MemberDto {
    private int mno; // pk
    private String memail; // unique
    private String mpwd; // not null
    private String mname; // not null
    
    // Dto --> Entity
    public MemberEntity toEntity() {
        return MemberEntity.builder()
                .mno(this.mno)
                .memail(this.memail)
                .mpwd(this.mpwd)
                .mname(this.mname)
                .build(); // MemberEntity 객체 생성
    } // end toEntity
} // end MemberDto

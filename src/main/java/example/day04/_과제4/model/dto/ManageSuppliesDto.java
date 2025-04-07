package example.day04._과제4.model.dto;


import example.day04._과제4.model.entity.ManageSuppliesEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ManageSuppliesDto<ManageSupplies> {
    private Long id; // PK
    private String name; // 비품명
    private String description; // 비품 설명
    private int quantity; // 수량
    private LocalDateTime createdDate; // 생성날짜
    private LocalDateTime modifiedDate; // 수정날짜

    // DTO --> Entity 변환 메서드
    public ManageSuppliesEntity toEntity() {
        return ManageSuppliesEntity.builder()
                .id( this.id )
                .name( this.name )
                .description( this.description )
                .quantity( this.quantity )
                // createdDate, modifiedDate는 자동으로 생성됨
                .build();
    } // end toEntity

} // end class

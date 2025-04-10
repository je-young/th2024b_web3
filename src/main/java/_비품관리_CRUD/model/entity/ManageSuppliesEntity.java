package _비품관리_CRUD.model.entity;


import _비품관리_CRUD.model.dto.ManageSuppliesDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "manage_supplies")
public class ManageSuppliesEntity extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK : 비품ID

    @Column(nullable = false , length = 100) // 비품명, null 불가, 길이 100
    private String name;

    @Column(columnDefinition = "TEXT") // 비품 설명 (긴 텍스트 가능)
    private String description;

    @Column(nullable = false) // 수량, null 불가
    private int quantity;

    // Entity --> DTO 변환 메서드
    public ManageSuppliesDto toDto() {
        return ManageSuppliesDto.builder()
                .id(this.id)
                .name(this.name)
                .description(this.description)
                .quantity(this.quantity)
                .createdDate(this.getCreatedDate())
                .modifiedDate(this.getModifiedDate())
                .build();
    } // end toDto

} // end class

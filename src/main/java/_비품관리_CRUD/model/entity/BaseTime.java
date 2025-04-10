package _비품관리_CRUD.model.entity;


import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@MappedSuperclass // 이 클래스는 테이블과 직접 매핑되지 않고, 상속받은 자식 엔티티에게 필드만 물려줌.
@EntityListeners(AuditingEntityListener.class) // JPA Auditing 리스너 적용
public class BaseTime {

    @CreatedDate // 엔티티 생성시 시간 자동 저장
    @Column(updatable = false) // 생성 시간은 수정 불가
    private LocalDateTime createdDate;

    @LastModifiedDate // 엔티티 수정시 시간 자동 저장
    private LocalDateTime modifiedDate;

} // end class

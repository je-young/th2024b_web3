package CRUD.model.repository;


import CRUD.model.entity.ManageSuppliesEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ManageSuppliesRepository extends JpaRepository<ManageSuppliesEntity, Long> {
    // JpaRepository<엔티티클래스, PK타입>
    // 기본적인 CRUD 및 페이징 메소드 자동 제공


    // === 과제 5: 네이티브 쿼리 메소드 정의 ===
    // 1. 등록 (참고: save()는 ID 반환 등 JPA 이점이 많아 보통 그대로 사용하나, 요구사항에 따라 정의)
    //    네이티브 INSERT는 생성된 ID를 직접 반환하기 까다로움.
    //    여기서는 예시로 정의만 하고 Service에서는 save()를 유지하는 것을 권장할 수 있음.
    //    (또는 void 반환 후 별도 조회)
    /**
    @Modifying
    @Query(value = "INSERT INTO manage_supplies (name, description, quantity, created_date, modified_date) " +
                   "VALUES (:#{#entity.name}, :#{#entity.description}, :#{#entity.quantity}, :#{#entity.createdDate}, :#{#entity.modifiedDate})", nativeQuery = true)
    void insertSupplyNative(@Param("entity") ManageSuppliesEntity entity);
    */

    // 2. 전체 조회 (네이티브 쿼리) - ID 내림차순 정렬 예시
    @Query(value = "SELECT * FROM manage_supplies ORDER BY id DESC", nativeQuery = true)
    List<ManageSuppliesEntity> findAllNativeDescById();

    // 3. 개별 조회 (네이티브 쿼리)
    @Query(value = "SELECT * FROM manage_supplies WHERE id = :id", nativeQuery = true)
    Optional<ManageSuppliesEntity> findByIdNative(@Param("id") Long id); // Optional 반환 유지

    // 4. 수정 (네이티브 쿼리)
    @Modifying // INSERT, UPDATE, DELETE 쿼리 시 필수
    @Query(value = "UPDATE manage_supplies SET " +
            "name = :name, " +
            "description = :description, " +
            "quantity = :quantity, " +
            "modified_date = :modifiedDate " + // 수정 시간도 업데이트
            "WHERE id = :id", nativeQuery = true)
    int updateSupplyNative(@Param("id") Long id,
                           @Param("name") String name,
                           @Param("description") String description,
                           @Param("quantity") int quantity,
                           @Param("modifiedDate") LocalDateTime modifiedDate); // 수정 시간 파라미터 추가

    // 5. 삭제 (네이티브 쿼리)
    @Modifying
    @Query(value = "DELETE FROM manage_supplies WHERE id = :id", nativeQuery = true)
    int deleteByIdNative(@Param("id") Long id); // 영향받은 행 수 반환

    // 5-1. 존재 여부 확인 (네이티브 쿼리 - deleteSupply에서 사용 가능)
    @Query(value = "SELECT COUNT(*) > 0 FROM manage_supplies WHERE id = :id", nativeQuery = true)
    long countByIdNative(@Param("id") Long id);

    // 6. 페이징 조회 (네이티브 쿼리 - Pageable 지원 방식)
    //    Spring Data JPA는 네이티브 쿼리에서도 Pageable 파라미터를 인식하여 페이징 처리를 시도함.
    //    countQuery를 명시하여 전체 개수 조회 쿼리를 따로 지정하는 것이 좋음.
    @Query(value = "SELECT * FROM manage_supplies ORDER BY id DESC", // 데이터 조회 쿼리
            countQuery = "SELECT count(*) FROM manage_supplies", // 전체 개수 조회 쿼리
            nativeQuery = true)
    Page<ManageSuppliesEntity> findAllNativePage(Pageable pageable);


} // end interface

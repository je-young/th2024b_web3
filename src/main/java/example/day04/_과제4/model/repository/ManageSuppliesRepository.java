package example.day04._과제4.model.repository;


import example.day04._과제4.model.entity.ManageSuppliesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManageSuppliesRepository extends JpaRepository<ManageSuppliesEntity, Long> {
    // JpaRepository<엔티티클래스, PK타입>
    // 기본적인 CRUD 및 페이징 메소드 자동 제공
} // end interface

package example.day04._과제4.service;


import example.day04._과제4.model.dto.ManageSuppliesDto;
import example.day04._과제4.model.entity.ManageSuppliesEntity;
import example.day04._과제4.model.repository.ManageSuppliesRepository;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional // 서비스 메소드들을 하나의 트랜잭션으로 관리
@RequiredArgsConstructor // final 필드 생성자 자동 주입
public class ManageSuppliesService {

    private final ManageSuppliesRepository manageSuppliesRepository;

    // 1. 비품 등록 기능
    public ManageSuppliesDto createSupply(ManageSuppliesDto dto) {
        // (1) DTO --> Entity
        ManageSuppliesEntity entity = dto.toEntity();
        // (2) ID는 null 상태로 전달되어야 INSERT 발생
        entity.setId(null);
        // (3) Repository 를 통해 저정(INSERT) 및 영속화된 Entity 받기
        ManageSuppliesEntity savedEntity = manageSuppliesRepository.save(entity);
        // (4) 저장된 Entity --> DTO
        return savedEntity.toDto();
    } // end createSupplies

    // 2. 전체 비품 조회 기능
    public List<ManageSuppliesDto> getAllSupplies() {
        // (1) 모든 Entity 조회 (정렬 추가 가능 : fingAll(Sort.by(...))
        List<ManageSuppliesEntity> entityList = manageSuppliesRepository.findAll(Sort.by(Sort.Direction.DESC, "id")); // ID 역순 정렬
        // (2) Entity --> DTO List (Stream API 사용)
        return entityList.stream()
                .map(ManageSuppliesEntity::toDto)
                .collect(Collectors.toList());
    } // end getAllSupplies

    // 3. 개별 비품 조회 기능
    public ManageSuppliesDto getSupplyById(Long id) {
        // (1) ID로 Entity 조회, 없으면 EntityNotFoundException 발행 (Controller 에서 처리)
        ManageSuppliesEntity entity = manageSuppliesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ID : " + id + "에 해당하는 비품을 찾을 수 없습니다."));
        // (2) Entity --> DTO
        return entity.toDto();
    } // end getSupplyById

    // 4. 비품 수정 기능 (변경 감지 활용)
    public ManageSuppliesDto updateSupply(Long id, ManageSuppliesDto dto) {
        // (1) 수정할 대상 엔티티 조회 (영속 상태)
        ManageSuppliesEntity entity = manageSuppliesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ID " + id + " 에 해당하는 비품을 수정할 수 없습니다."));

        // (2) DTO 의 정보로 영속 엔티티의 필드 값 업데이트 (Setter 사용)
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setQuantity(dto.getQuantity());

        // (3) @Transactional 에 의해 메서드 종료 시 변경 감지(Dirty Checking) 후 UPDATE 쿼리 자동 실행
            // (BaseTime 의 modifiedDate 도 자동 업데이트 됨)
            // 별도의 save() 호출 불필요.

        // (4) 수정된 정보를 DTO 로 변환하여 반환
        return entity.toDto();
    } // end updateSupply

    // 5. 비품 삭제 기능
    public boolean deleteSupply(Long id){
        // (1) 삭제할 대상 엔티티 존재 여부 확인
        if (manageSuppliesRepository.existsById(id)) {
            manageSuppliesRepository.deleteById(id);
            return true; // 존재하면 삭제
        } // end if
        return false; // 삭제할 대상 없음
    } // end deletesupply

    // 6. 페이징 기능이 적용된 비품 목록 조회
    public Page<ManageSuppliesDto> getSuppliesByPage(int page, int size) {
        // (1) Pageable 객체 생성 (JPA 페이지는 0부터 시작, ID 내림차순 정렬 예시)
        Pageable pageable = PageRequest.of(page -1, size, Sort.by(Sort.Direction.DESC, "id"));

        // (2) Repository 의 findAll(Pageable) 호출
        Page<ManageSuppliesEntity> entityPage = manageSuppliesRepository.findAll(pageable);

        // (3) Page<Entity> -> Page<DTO> 변환
        // map() 메소드는 Page 내부의 내용물(List<Entity>)을 변환하는 기능을 제공
        Page<ManageSuppliesDto> dtoPage = entityPage.map(ManageSuppliesEntity::toDto);

        // 페이징 정보 로깅
        System.out.println("Total Pages: " + dtoPage.getTotalPages());
        System.out.println("Total Elements: " + dtoPage.getTotalElements());
        System.out.println("Current Page Number: " + (dtoPage.getNumber() + 1));
        System.out.println("Current Page Size: " + dtoPage.getSize());

        return dtoPage;
    } // end getSuppliesByPage

} // end class

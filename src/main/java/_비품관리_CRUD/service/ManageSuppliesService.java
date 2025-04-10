package _비품관리_CRUD.service;


import _비품관리_CRUD.model.dto.ManageSuppliesDto;
import _비품관리_CRUD.model.entity.ManageSuppliesEntity;
import _비품관리_CRUD.model.repository.ManageSuppliesRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional // 서비스 메소드들을 하나의 트랜잭션으로 관리
@RequiredArgsConstructor // final 필드 생성자 자동 주입
public class ManageSuppliesService {

    private final ManageSuppliesRepository manageSuppliesRepository;

    // 1. 비품 등록 기능
    public ManageSuppliesDto createSupply(ManageSuppliesDto dto) {
        // === 기존 코드 (save 사용) ===
        // (1) DTO --> Entity
        ManageSuppliesEntity entity = dto.toEntity();
        // (2) ID는 null 상태로 전달되어야 INSERT 발생
        entity.setId(null);
        // (3) Repository 를 통해 저정(INSERT) 및 영속화된 Entity 받기
        ManageSuppliesEntity savedEntity = manageSuppliesRepository.save(entity);
        // (4) 저장된 Entity --> DTO
        return savedEntity.toDto();


        // === 네이티브 쿼리 사용 시도 (참고) ===
        /**
         * 네이티브 INSERT 쿼리는 JPA의 save()와 달리 생성된 ID를 자동으로 반환받기 어렵습니다.
         * 또한, @CreatedDate, @LastModifiedDate 같은 Auditing 기능이
         * 네이티브 쿼리 실행 시점에는 적용되지 않을 수 있습니다. (Entity 객체 생성 시점에는 적용됨)
         * 따라서 INSERT 작업은 가급적 JPA의 save() 메소드를 사용하는 것이 일반적이고 편리합니다.
         * 꼭 네이티브 쿼리를 사용해야 한다면, Auditing 필드 값을 직접 설정하고,
         * ID를 별도로 조회하는 로직이 필요할 수 있습니다.

        ManageSuppliesEntity entityToInsert = dto.toEntity();
        // Auditing 필드 값 설정 (Entity 객체 생성 시 자동으로 설정될 수도 있으나 명시적으로)
        entityToInsert.setCreatedDate(LocalDateTime.now());
        entityToInsert.setModifiedDate(LocalDateTime.now());
        manageSuppliesRepository.insertSupplyNative(entityToInsert); // ID 반환 없음

        // ID를 알 수 없으므로 DTO를 완전하게 반환하기 어려움.
        // (예: 이름으로 다시 조회해서 ID를 얻어오는 등 추가 작업 필요)
        // return findByNameAndReturnDto(entityToInsert.getName()); // 예시적인 추가 로직
        return null; // 임시 반환
        */
    } // end createSupplies

    // 2. 전체 비품 조회 기능
    public List<ManageSuppliesDto> getAllSupplies() {
        // === 기존 코드 (findAll 사용) ===
        // (1) 모든 Entity 조회 (정렬 추가 가능 : fingAll(Sort.by(...))
 //       List<ManageSuppliesEntity> entityList = manageSuppliesRepository.findAll(Sort.by(Sort.Direction.DESC, "id")); // ID 역순 정렬
        // (2) Entity --> DTO List (Stream API 사용)
 //       return entityList.stream()
 //               .map(ManageSuppliesEntity::toDto)
 //               .collect(Collectors.toList());

        // === 네이티브 쿼리 사용 ===
                List<ManageSuppliesEntity> entityList = manageSuppliesRepository.findAllNativeDescById();
        return entityList.stream()
                .map(ManageSuppliesEntity::toDto)
                .collect(Collectors.toList());
    } // end getAllSupplies

    // 3. 개별 비품 조회 기능
    public ManageSuppliesDto getSupplyById(Long id) {
        // === 기존 코드 (findById 사용) ===
        // (1) ID로 Entity 조회, 없으면 EntityNotFoundException 발행 (Controller 에서 처리)
//        ManageSuppliesEntity entity = manageSuppliesRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("ID : " + id + "에 해당하는 비품을 찾을 수 없습니다."));
        // (2) Entity --> DTO
//        return entity.toDto();


        // === 네이티브 쿼리 사용 ===
        Optional<ManageSuppliesEntity> optionalEntity = manageSuppliesRepository.findByIdNative(id);
        ManageSuppliesEntity entity = optionalEntity
                .orElseThrow(() -> new EntityNotFoundException("ID " + id + " 에 해당하는 비품을 찾을 수 없습니다. (Native Query)"));
        return entity.toDto();
    } // end getSupplyById

    // 4. 비품 수정 기능 (변경 감지 활용)
    public ManageSuppliesDto updateSupply(Long id, ManageSuppliesDto dto) {
        // === 기존 코드 (변경 감지 사용) ===
        // (1) 수정할 대상 엔티티 조회 (영속 상태)
//        ManageSuppliesEntity entity = manageSuppliesRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("ID " + id + " 에 해당하는 비품을 수정할 수 없습니다."));

        // (2) DTO 의 정보로 영속 엔티티의 필드 값 업데이트 (Setter 사용)
//        entity.setName(dto.getName());
//        entity.setDescription(dto.getDescription());
//        entity.setQuantity(dto.getQuantity());

        // (3) @Transactional 에 의해 메서드 종료 시 변경 감지(Dirty Checking) 후 UPDATE 쿼리 자동 실행
            // (BaseTime 의 modifiedDate 도 자동 업데이트 됨)
            // 별도의 save() 호출 불필요.

        // (4) 수정된 정보를 DTO 로 변환하여 반환
//        return entity.toDto();


        // === 네이티브 쿼리 사용 ===
        // 1. 수정 전 존재 여부 확인 (선택적이지만 안전함)
        if (manageSuppliesRepository.countByIdNative(id) == 0) { // 네이티브 존재 확인 메소드 사용
            throw new EntityNotFoundException("ID " + id + " 에 해당하는 비품을 수정할 수 없습니다. (Native Query)");
        } // end if

        // 2. 네이티브 UPDATE 쿼리 실행
        //    수정 시간은 직접 설정하여 전달 (Auditing이 네이티브 쿼리에 적용 안 될 수 있음)
        LocalDateTime now = LocalDateTime.now();
        int updatedRows = manageSuppliesRepository.updateSupplyNative(
                id,
                dto.getName(),
                dto.getDescription(),
                dto.getQuantity(),
                now // 수정 시간 직접 전달
        );

        // 3. 업데이트 성공 여부 확인 및 결과 반환
        if (updatedRows > 0) {
            // 수정된 정보를 다시 조회하여 DTO로 반환 (가장 확실한 방법)
            return getSupplyById(id);
            // 또는 입력 DTO에 ID와 수정된 시간만 설정하여 반환 (조회 없이)
            // dto.setId(id);
            // dto.setModifiedDate(now);
            // return dto;
        } else {
            // 업데이트가 실제로 이루어지지 않은 경우 (이론상 앞서 확인했으므로 거의 발생 안 함)
            throw new RuntimeException("ID " + id + " 비품 정보 업데이트에 실패했습니다.");
        } // end if else
} // end updateSupply

    // 5. 비품 삭제 기능
    public boolean deleteSupply(Long id){
        // === 기존 코드 (existsById + deleteById 사용) ===
        // (1) 삭제할 대상 엔티티 존재 여부 확인
//        if (manageSuppliesRepository.existsById(id)) {
//            manageSuppliesRepository.deleteById(id);
//            return true; // 존재하면 삭제
//        } // end if
//        return false; // 삭제할 대상 없음


        // === 네이티브 쿼리 사용 ===
        // 1. 네이티브 DELETE 쿼리 실행하고 영향받은 행(row) 수 받기
        int deletedRows = manageSuppliesRepository.deleteByIdNative(id);

        // 2. 영향받은 행 수가 0보다 크면 삭제 성공
        return deletedRows > 0;
    } // end deletesupply

    // 6. 페이징 기능이 적용된 비품 목록 조회
    public Page<ManageSuppliesDto> getSuppliesByPage(int page, int size) {
        // === 기존 코드 (findAll(Pageable) 사용) ===
        // (1) Pageable 객체 생성 (JPA 페이지는 0부터 시작, ID 내림차순 정렬 예시)
//        Pageable pageable = PageRequest.of(page -1, size, Sort.by(Sort.Direction.DESC, "id"));

        // (2) Repository 의 findAll(Pageable) 호출
//        Page<ManageSuppliesEntity> entityPage = manageSuppliesRepository.findAll(pageable);

        // (3) Page<Entity> -> Page<DTO> 변환
        // map() 메소드는 Page 내부의 내용물(List<Entity>)을 변환하는 기능을 제공
//        Page<ManageSuppliesDto> dtoPage = entityPage.map(ManageSuppliesEntity::toDto);

        // 페이징 정보 로깅
//        System.out.println("Total Pages: " + dtoPage.getTotalPages());
//        System.out.println("Total Elements: " + dtoPage.getTotalElements());
//        System.out.println("Current Page Number: " + (dtoPage.getNumber() + 1));
//        System.out.println("Current Page Size: " + dtoPage.getSize());

//        return dtoPage;


        // === 네이티브 쿼리 사용 ===
        // 1. Pageable 객체 생성 (JPA 페이지는 0부터 시작)
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id"));

        // 2. 네이티브 쿼리 페이징 메소드 호출
        Page<ManageSuppliesEntity> entityPage = manageSuppliesRepository.findAllNativePage(pageable);

        // 3. Page<Entity> -> Page<DTO> 변환 (기존과 동일)
        return entityPage.map(ManageSuppliesEntity::toDto);
    } // end getSuppliesByPage
} // end class

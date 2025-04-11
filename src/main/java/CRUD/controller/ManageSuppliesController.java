package CRUD.controller;


import CRUD.model.dto.ManageSuppliesDto;
import CRUD.service.ManageSuppliesService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/supplies") // http://localhost:8080/api/supplies
@CrossOrigin("*") // 플러터 dio (web)
public class ManageSuppliesController {

    private final ManageSuppliesService manageSuppliesService;

    // 1. 비품 등록 기능
    @PostMapping // http://localhost:8080/api/supplies
    // { "name": "test", "description": "test", "quantity": 10 }
    public ResponseEntity<ManageSuppliesDto> createSupply(@RequestBody ManageSuppliesDto dto){
        ManageSuppliesDto createdDto = manageSuppliesService.createSupply(dto);
        return new ResponseEntity<>(createdDto, HttpStatus.CREATED); // 201 Created
    } // end createSupply

    // 2. 전체 비품 조회 기능
    @GetMapping // http://localhost:8080/api/supplies
    public ResponseEntity<List<ManageSuppliesDto>> getAllSupplies(){
        List<ManageSuppliesDto> dtoList = manageSuppliesService.getAllSupplies();
        return ResponseEntity.ok(dtoList); // 200 OK
    } // end getAllSupplies

    // 3. 개별 비품 조회 기능
    @GetMapping("/{id}") // http://localhost:8080/api/supplies/{id}
    public ResponseEntity<ManageSuppliesDto> getSupplyById(@PathVariable Long id){
        try {
            ManageSuppliesDto dto = manageSuppliesService.getSupplyById(id);
            return ResponseEntity.ok(dto); // 200 OK
        } catch (EntityNotFoundException e){
            return ResponseEntity.notFound().build(); // 404 Not Found
        } // end try
    } // end getSupplyById

    // 4. 비품 수정 기능
    @PutMapping("/{id}") // http://localhost:8080/api/supplies/{id}
    public ResponseEntity<ManageSuppliesDto> updateSupply(@PathVariable Long id, @RequestBody ManageSuppliesDto dto){
        try {
            ManageSuppliesDto updatedDto = manageSuppliesService.updateSupply(id, dto);
            return ResponseEntity.ok(updatedDto); // 200 ok
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        } // end try
    } // end updateSupply

    // 5. 비품 삭제 기능
    @DeleteMapping("/{id}") // http://localhost:8080/api/supplies/{id}
    public ResponseEntity<Void> deleteSupply(@PathVariable Long id){
        boolean deleted = manageSuppliesService.deleteSupply(id);
        if (deleted) {
            return ResponseEntity.noContent().build(); // 204 No Content (성공)
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found (삭제할 대상 없음)
        } // end if else
    } // end deleteSupply

    // 6. 페이징 기능이 적용된 비품 목록 조회
    @GetMapping("/page") // http://localhost:8080/api/supplies/page?page=1&size=10
    public ResponseEntity<Page<ManageSuppliesDto>> getSuppliesByPage(
            @RequestParam(defaultValue = "1") int page, // 기본값 1페이지
            @RequestParam(defaultValue = "10") int size) { // 기본값 페이지당 10개
        Page<ManageSuppliesDto> dtoPage = manageSuppliesService.getSuppliesByPage(page, size);
        return ResponseEntity.ok(dtoPage); // 200 ok , 페이지 정보 포함하여 반환

    } // end getSuppliesByPage

} // end class

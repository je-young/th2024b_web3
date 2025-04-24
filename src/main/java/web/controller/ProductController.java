package web.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.model.dto.ProductDto;
import web.service.MemberService;
import web.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ProductController {
    // *
    private final ProductService productService;
    private final MemberService memberService;

    // 1. 제품등록
    /* 제품등록 설계
        1. Post , "/product/register"
        2. '로그인회원이 등록한다'
            토큰( Authorizstion ) , 등록할 값들( pname , pcontent , pprice , 여러개사진들 , cno )
        3. boolean 반환
     */
    @PostMapping("/register")
    public ResponseEntity<Boolean> registerProduct(
            @RequestHeader("Authorization") String token, // - 토큰 받기
            @ModelAttribute ProductDto productDto) { // - multipart/form(첨부파일) 받기
        System.out.println("token +  = " + token + ", productDto = " + productDto);

        // 1. 현재 토큰의 회원번호(작성자) 구하기.
        int loginMno;
        try {
            loginMno = memberService.info(token).getMno();
        } catch (Exception e) {
            return ResponseEntity.status(401).body(false); // 401 Unauthorized 와 false 반환
        }
        // 2. 저장할 DTO 와 회원번호를 서비스 에게 전달.
        boolean result = productService.registerProduct(productDto, loginMno);
        if (result == false) return ResponseEntity.status(400).body(false); // 400 Bad Request 와 false-return
        // 3. 요청 성공시 200 반환
        return ResponseEntity.status(201).body(true); // 201 (저장) 요청성공 과 true-return
    } // end registerProduct

    // 2. (카테고리별) 제품 전체조회 : 설계 : (카테고리조회)?cno=3  , (전체조회)?cno
    @GetMapping("/all")
    public ResponseEntity<List<ProductDto>> allProducts(
            @RequestParam(required = false) long cno) { // required = false : cno 는 필수는 아니다 뜻.
        List<ProductDto> productDtoList = productService.allProducts(cno);
        return ResponseEntity.status(200).body(productDtoList); // 200 성공 과 값 반환
    } // end allProducts

    // 3. 제품 개별조회 : 설계 : ?pno=1
    @GetMapping("/view")
    public ResponseEntity<ProductDto> viewProduct(@RequestParam long pno) { // required 생략시 pno 필수
        ProductDto productDto = productService.viewProduct(pno);
        if (productDto == null) {
            return ResponseEntity.status(404).body(null); // 404 Not Found 와 null-return
        } else {
            return ResponseEntity.status(200).body(productDto); // 200 과 값 반환
        } // end if else
    } // end viewProduct

    // 4. 제품 개별삭제 : 설계 : 토큰 , 삭제할제품번호
    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> deleteProduct(
            @RequestHeader("Authorezation") String token,
            @RequestParam int pno) {
        // 1. 권한 확인
        int loginMno;
        try {
            loginMno = memberService.info(token).getMno();
        } catch (Exception e) {
            return ResponseEntity.status(401).body(false);
        } // end try catch
        // 2.
        boolean reqult = productService.deleteProduct(pno, loginMno);
        // 3.
        if (reqult == false) return ResponseEntity.status(400).body(false);
        // 4.
        return ResponseEntity.status(200).body(true);
    } // end deleteProduct

    // 5. 제품 수정 ( + 이미지 추가 )

    /**
     * 매핑 : Put , /product/update , boolean
     * 매개변수 : 수정할 값 ( pname , pcontent , pprice , cno , files ) , 수정할 대상 : pno , 권한 : (token)
     */
    @PutMapping("/update")
    public ResponseEntity<Boolean> updateProduct(
            @RequestHeader("Authorization") String token,
            @ModelAttribute ProductDto productDto) {

        // 1. 토큰의 mno 추출
        int loginMno;
        try {
            loginMno = memberService.info(token).getMno();
        } catch (Exception e) {
            return ResponseEntity.status(401).body(false);
        } // end try catch
        // 2. 수정 서비스 호출
        boolean result = productService.updateProduct(productDto, loginMno);
        // 3. 수정 성공시 200-return
        if (result == false) return ResponseEntity.status(400).body(false);
        return ResponseEntity.status(200).body(true);
    } // end updateProduct

    // 6. 이미지 개별 삭제
    /**
     *   매핑 : Delete , /product/image , boolean
     *   매개변수 : 삭제할 대상 : ino , 권한 : (token)
     * */

    // 7. 카테고리 조회
    /**
     *   매핑 : Get , /product/category , List< CategoryDto >
     *   매개변수 : X
     * */

    // 2. 검색 + 페이징 처리 , 위에서 작업한 2번 메소드 주석처리 후 진행. ( 웹/앱 : 무한스크롤 )
    /**
     *   매핑 : Get , /product/all , List< ProductDto >
     매개변수 : cno(없으면 전체조회) , page(현재페이지번호 없으면 1페이지) , keyword(없으면 전체조회)
     * */

} // end class

package web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import web.model.dto.MemberDto;
import web.service.MemberService;

@RestController // Spring MVC2 controller
@RequestMapping("/member") // 공통 URL 정의
@RequiredArgsConstructor // final(수정불가) 필드의 생성자 자동 생성
@CrossOrigin // CORS 정의 flutter -> spring
// -> 관례적으로 클래스 내부에서 사용하는 모든 필드들을 수정불가능 상태로 사용한다.
public class MemberController {
    // -> 관례적으로 다른 곳에 해당하는 필드를 수정못하도록 final 사용한다. (안전성 보장)
    // -> 즉 final 사용시 @RequiredArgsConstructor 때문에 @Autowired 안해도 된다.
    private final MemberService memberService;
    
    // [1] 회원가입 { "memail": "user@example.com", "mpwd": "password123", "mname": "사용자이름" }
    @PostMapping("/signup") // http://localhost:8080/member/signup
    public boolean signup(@RequestBody MemberDto memberDto) {
        return memberService.signup(memberDto);
    } // end signup

    // [2] 로그인 { "memail": "user@example.com", "mpwd": "password123" }
    @PostMapping("/login") // http://localhost:8080/member/login
    public String login(@RequestBody MemberDto memberDto) {
        return memberService.login(memberDto);
    } // end login

    // [3] 로그인된 회원 검증 / 내정보 조회
    @GetMapping("/info") // http://localhost:8080/member/info
    // @RequestHeader : HTTP 헤더 정보를 매핑 하는 어노테이션 , JWT 정보는 HTTP 헤더 에 담을 수 있다.
        // Authorization : 인증 속성 , { Authorization : token 값 }
    // @RequestParam : HTTP 헤더의 경로 쿼리스트링 매핑 하는 어노테이션
    // @RequestBody : HTTP 본문의 객체를 매핑 하는 어노테이션
    // @PathVariable : HTTP 헤더의 경로 값 매핑 하는 어노테이션
    public MemberDto info( @RequestHeader("Authorization") String token ) {
        return memberService.info(token);
    } // end info

    // [4] 로그아웃
    @GetMapping("/logout") // http://localhost:8080/member/logout
    public void logout( @RequestHeader("Authorization") String token ) {
        memberService.logout(token);
    } // end logout

    // [5] 최근 24시간 로그인 한 접속자 수
    @GetMapping("/login/count") // http://localhost:8080/member/login/count
    public int loginCount() {
        return memberService.loginCount();
    } // end loginCount

} // end MemberController

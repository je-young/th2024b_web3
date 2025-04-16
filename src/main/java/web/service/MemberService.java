package web.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import web.model.dto.MemberDto;
import web.model.entity.MemberEntity;
import web.model.repository.MemberEntityRepository;

@Service // Spring MVC2 service
@RequiredArgsConstructor
@Transactional // 트랜잭션 : 여러개의 SQL 을 하나의 논리 단위 묶음
// 트랜젝션은 성공 또는 실패 , 부분 성공은 없다.
// 메서드 안에서 여러가지 SQL 실행할 경우 하나라도 오류가 발생하면 롤백(취소) * JPA 엔티티 수정 필수!
public class MemberService {
    private final MemberEntityRepository memberEntityRepository;
    
    // [1] 회원가입
    public boolean signup(MemberDto memberDto) {
        // 1. 이메일 중복검사
        if(memberEntityRepository.existsByMemail(memberDto.getMemail())) {
            return false;
        } // end if

        // 2. 비밀번호 암호화
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // 암호화 비크립트 객체 생성
        String hashedPwd = passwordEncoder.encode(memberDto.getMpwd()); // 암호화 지원하는 함수 .encode( 아호화할데이터 )
        memberDto.setMpwd(hashedPwd);

        // 3. dto -> entity 변환
        MemberEntity memberEntity = memberDto.toEntity();

        // 4. repository 이용한 entity 영속화하기 , 영속된 결과 반환
        MemberEntity saveEntity = memberEntityRepository.save(memberEntity);

        // 5. 영속된 엔티티의 (자동 셍성된) pk 확인
        if(saveEntity.getMno() >= 1) { return true; }
        return false;
    } // end signup

    // [2] 로그인
    public String login(MemberDto memberDto) {
        // 1. 이메일(아이디)를 DB에서 조회하여 엔티티 찾기
        MemberEntity memberEntity = memberEntityRepository.findByMemail(memberDto.getMemail());

        // 2. 조회된 엔티티가 없으면
        if(memberEntity == null) { return null; } // 로그인 실패

        // 3. 조회된 엔티티의 비밀번호 검증. .matches(입력받은 비밀번호, 암호화된 비밀번호)
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // Bcrypt 객체 생성
        boolean inMatch = passwordEncoder.matches(memberDto.getMpwd(), memberEntity.getMpwd());

        // 4. 비밀번호 검증 실패이면
        if(!inMatch == false) { return null; } // 로그인 실패

        // 5. 비밀번호 검증 성공이면 ,


    } // end login

} // end MemberService

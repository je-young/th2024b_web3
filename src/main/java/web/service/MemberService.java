package web.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import web.model.dto.MemberDto;
import web.model.entity.MemberEntity;
import web.model.repository.MemberEntityRepository;
import web.util.JwtUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service // Spring MVC2 service
@RequiredArgsConstructor
@Transactional // 트랜잭션 : 여러개의 SQL 을 하나의 논리 단위 묶음
// 트랜젝션은 성공 또는 실패 , 부분 성공은 없다.
// 메서드 안에서 여러가지 SQL 실행할 경우 하나라도 오류가 발생하면 롤백(취소) * JPA 엔티티 수정 필수!
public class MemberService {
    private final MemberEntityRepository memberEntityRepository;
    private final JwtUtil jwtUtil; // // JwtUtil 필드 추가
    
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

    // [2] 로그인 , 로그인 성공시 token 실패시 null
    public String login(MemberDto memberDto) {
        // 1. 이메일(아이디)를 DB에서 조회하여 엔티티 찾기
        MemberEntity memberEntity = memberEntityRepository.findByMemail(memberDto.getMemail());

        // 2. 조회된 엔티티가 없으면
        if(memberEntity == null) { return null; } // 로그인 실패

        // 3. 조회된 엔티티의 비밀번호 검증. .matches(입력받은 비밀번호, 암호화된 비밀번호)
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // Bcrypt 객체 생성
        boolean inMatch = passwordEncoder.matches(memberDto.getMpwd(), memberEntity.getMpwd());

        // 4. 비밀번호 검증 실패이면
        if( inMatch == false ) { return null; } // 로그인 실패

        // 5. 비밀번호 검증 성공이면 Token 발급
        String token = jwtUtil.createToken(memberEntity.getMemail());
        System.out.println(">>> 발급된 token = " + token);

        // + 레디스에 24시간만 저장되는 로그인 로그(기록) 하기.
        stringRedisTemplate.opsForValue().set("RECENT_LOGIN:" + memberEntity.getMemail() , "true" , 1 , TimeUnit.DAYS);

        return token;
    } // end login

    // [3] 전달받은 token 으로 token 검증하여 유효한 token 은 회원정보(dto) 반환 유효하지 않은 token null 반환
    public MemberDto info(  String token ){
        // 1. 전달받은 token 으로 검증하기. vs 세션 호출/검증
        String memail = jwtUtil.validateToken( token );
        // 2. 검증이 실패이면 '비로그인중' 이거나 유효기간 만료 , 실패
        if( memail == null ) return null;
        // 3. 검증이 성공이면 토큰에 저장된 이메일을 가지고 엔티티 조회
        MemberEntity memberEntity
                = memberEntityRepository.findByMemail( memail );
        // 4. 조회된 엔티티가 없으면 실패
        if( memberEntity == null ) return null;
        // 5. 조회 성공시 조회된 엔티티를 dto로 변환하여 반환한다.
        return memberEntity.toDto();
    } // end info

    // [4] 로그아웃
    public void logout( String token ) {
        // 1. 해당 token 의 이메일 조회
        String memail = jwtUtil.validateToken( token );
        // 2. 조회된 이메일의 redis 이용한 토큰 삭제
        jwtUtil.deleteToken( memail );
    } // end logout

    private final StringRedisTemplate stringRedisTemplate;

    // [5] 최근 24시간내 로그인 한 접속자 수
    public int loginCount() {
        // 레디스에 저장된 키 들 중에서 ""RECENT_LOGIN:" 으로 시작하는 모든 KEY 반환
        Set<String> keys = stringRedisTemplate.keys("RECENT_LOGIN:*");
        // 반환된 개수 확인 , 비어있으면 0 아니면 size() 함수 이용한 key 개수 반환
        return keys == null ? 0 : keys.size();
    } // end loginCount

} // end MemberService

package com.yoonsub.config.jwt;

import com.yoonsub.domain.User;
import com.yoonsub.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TokenProviderTest {

  @Autowired
  private TokenProvider tokenProvider;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private JwtProperties jwtProperties;

  @DisplayName("generateToken(): 유저 정보와 만료 기간을 전달해 토큰 생성 가능")
  @Test
  void generateToken() {

    //given - 테스트 유저 생성
    User testUser = userRepository.save(User.builder()
            .email("user@gmail.com")
            .password("test")
            .build());

    //when - 토근 생성
    String token = tokenProvider.generateToken(testUser, Duration.ofDays(14));

    //then  - 토큰 복호화, 토큰 생성시 클레임으로 넣어둔 Id 값과 유저 Id와 비교
    Long userId = Jwts.parser()
            .setSigningKey(jwtProperties.getSecretKey())
            .parseClaimsJws(token)
            .getBody()
            .get("id", Long.class);

    assertThat(userId).isEqualTo(testUser.getId());
  }

  //validToken() 테스트
  @DisplayName("validToken() : 만료된 토큰인 때에 유효성 검증에 실패.")
  @Test
  void validToken_invalidToken() {

    //given - jjwt 라이브러리로 만료된 토큰 생성, 7주일, 토큰은 밀리초 단위이므로 toMillis()
    String token = JwtFactory.builder()
            .expiration(new Date(new Date().getTime() - Duration.ofDays(7).toMillis()))
            .build().createToken(jwtProperties);
    System.out.println("-----------------------------" + token);
    //when - 토큰 검증
    boolean result = tokenProvider.validToken(token);

    //then - false : 유요한 토큰이 아님 확인
    assertThat(result).isFalse();


  }

  
  // getAuthentication() 테스트
  @DisplayName("getAuthentication(): 토큰 기반으로 인증 정보를 가져올 수 있다.")
  @Test
  void getAuthentication() {

    //given - jjwt 라이브러리로 토큰 생성, subject 이름 설정
    String userEmail = "user@email.com";
    String token = JwtFactory.builder()
            .subject(userEmail)
            .build()
            .createToken(jwtProperties);

    //when - tokenProvider 의 getAuthentication() 으로 인증 객체 반환
    Authentication authentication = tokenProvider.getAuthentication(token);

    //then - 반환받은 인증 객체의 유저이름과 given 에서 설정한 subject 와 같은지 확인
    assertThat(((UserDetails) authentication.getPrincipal()).getUsername()).isEqualTo(userEmail);

  }
  
  //getUserId() 테스트
  @DisplayName("getUserId()")
  @Test
  void getUserId() {

    //given - jwt 라이브러리로 토큰 생성, 클레임추가 k : id, v : 1L
    Long userId = 1L;
    String token = JwtFactory.builder()
            .claims(Map.of("id", userId))
            .build()
            .createToken(jwtProperties);

    //when - tokenProvider 의 getUserId()로 유저 id 반환
    Long userIdByToken = tokenProvider.getUserId(token);

    //then - 반환받은 유저 id가 설정한 유저 id값(1) 과 같은지 확인
    assertThat(userIdByToken).isEqualTo(userId);
  }
}

package com.yoonsub.config.jwt;


import com.yoonsub.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TokenProvider {

  private final JwtProperties jwtProperties;

  public String generateToken(User user, Duration expiredAt) {
    Date now = new Date();
    return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
  }

  // JWT 토근 생성 메서드
  private String makeToken(Date expiry, User user) {
  Date now = new Date();

  return Jwts.builder()
          .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // 헤더 설정
          .setIssuer(jwtProperties.getIssuer())
          .setIssuedAt(now)   // 내용 iat : 현재 시간
          .setExpiration(expiry)
          .setSubject(user.getEmail())    // 내용 sub : 유저의 이메일
          .claim("id", user.getId())    // 클레임 id : 유저 id
          //서명 : 비밀값과 해시값을 HS256 방식으로 암호화
          .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
          .compact();


  }

  // 토큰 기반으로 인증 정보를 가져오는 메서드
  public Authentication getAuthentication(String token) {
    Claims claims = getClaims(token);
    Set<SimpleGrantedAuthority> authorities = Collections.singleton(
            new SimpleGrantedAuthority("ROLE_USER"));

    return new UsernamePasswordAuthenticationToken(
            new org.springframework.security.core.userdetails.User(claims.getSubject()
            , "", authorities), token, authorities);
  }

  // 토큰 유효성 검증 메서드
  public boolean validToken(String token){

    try{
      Jwts.parser()
              .setSigningKey(jwtProperties.getSecretKey())
              .parseClaimsJws(token);

      return true;
    } catch (Exception e) {     // 에러 = 유효하지 않는 토큰
      return false;
    }
  }

  // 토큰 기반 유저 ID 가져오는 메서드
  public Long getUserId(String token) {
    Claims claims = getClaims(token);
      return claims.get("id", Long.class);
    }


  public Claims getClaims(String token) {
    return Jwts.parser()  //클레임 조회
            .setSigningKey(jwtProperties.getSecretKey())
            .parseClaimsJws(token)
            .getBody();
  }

}

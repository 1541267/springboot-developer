package com.yoonsub.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto Increment
  @Column(name = "id", updatable = false)
  private Long id;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "password")
  private String password;

  @Builder
  public User(String email, String password) {
    this.email = email;
    this.password = password;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {  //권한 변환
    return List.of(new SimpleGrantedAuthority("user"));
  }

  @Override   
  public String getUsername() {       // 사용자 id 반환
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {  // 계정 만료 확인
    return UserDetails.super.isAccountNonExpired();
  }

  @Override
  public boolean isAccountNonLocked() {   // 계정 잠금 확인
    return UserDetails.super.isAccountNonLocked();
  }

  @Override
  public boolean isCredentialsNonExpired() {  // 패스워드 만료 확인
    return UserDetails.super.isCredentialsNonExpired();
  }

  @Override
  public boolean isEnabled() {    // 계정 사용 가능 여부
    return UserDetails.super.isEnabled();
  }

}

package com.yoonsub.service;

import com.yoonsub.domain.User;
import com.yoonsub.dto.AddUserRequest;
import com.yoonsub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

  private final UserRepository userRepository;
  //323p OAuth2 인증 성공 핸들러
//  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  public Long save(AddUserRequest dto) {		// AddUserRequest 객체를 인수로save()

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    return userRepository.save(User.builder()	// save()
            .email(dto.getEmail())
            .password(encoder.encode(dto.getPassword()))
            .build()).getId();
  }


  //290p
  public User findById(Long userId) {
    return userRepository.findById(userId).orElseThrow(() ->
    new IllegalArgumentException("Unexpected user"));
  }

  //324p 추가 OAuth2 제공하는 이메일은 PK값, 메서드를 사용해 유저 찾기 가능
  public User findByEmail(String email) {
    return userRepository.findByEmail(email).orElseThrow(() ->
    new IllegalArgumentException("Unexpected user"));
  }
}
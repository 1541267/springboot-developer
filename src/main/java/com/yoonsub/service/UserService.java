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
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  public Long save(AddUserRequest dto) {		// AddUserRequest 객체를 인수로save()
    return userRepository.save(User.builder()	// save()
            .email(dto.getEmail())
            .password(bCryptPasswordEncoder.encode(dto.getPassword()))
            .build()).getId();
  }

}
package com.yoonsub.service;

import com.yoonsub.config.jwt.TokenProvider;
import com.yoonsub.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {

  private final TokenProvider tokenProvider;
  private final RefreshTokenService refreshTokenService;
  private final UserService userService;

  public String createNewAccessToken(String refreshToken) throws IllegalAccessException {
    if(!tokenProvider.validToken(refreshToken)) {
      throw new IllegalAccessException("Unexpected Token");
    }
    Long userId= refreshTokenService.findByRefreshToken(refreshToken).getUserId();
    User user = userService.findById(userId);

    return tokenProvider.generateToken(user, Duration.ofHours(2));
  }
}

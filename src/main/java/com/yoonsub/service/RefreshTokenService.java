package com.yoonsub.service;

import com.yoonsub.config.jwt.TokenProvider;
import com.yoonsub.domain.RefreshToken;
import com.yoonsub.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.channels.FileChannel;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {

  private final RefreshTokenRepository refreshTokenRepository;
  private final TokenProvider tokenProvider;

  public RefreshToken findByRefreshToken(String refreshToken) {
    return refreshTokenRepository.findByRefreshToken(refreshToken)
            .orElseThrow(() -> new IllegalArgumentException("Unexpected Token"));
  }

}

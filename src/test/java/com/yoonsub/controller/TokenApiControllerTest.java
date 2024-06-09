package com.yoonsub.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoonsub.config.jwt.JwtFactory;
import com.yoonsub.config.jwt.JwtProperties;
import com.yoonsub.domain.User;
import com.yoonsub.dto.CreateAccessTokenRequest;
import com.yoonsub.domain.RefreshToken;
import com.yoonsub.repository.RefreshTokenRepository;
import com.yoonsub.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TokenApiControllerTest {
  @Autowired
  protected MockMvc mockMvc;
  @Autowired
  protected ObjectMapper objectMapper;
  @Autowired
  private WebApplicationContext context;
  @Autowired
  JwtProperties jwtProperties;
  @Autowired
  UserRepository userRepository;
  @Autowired
  RefreshTokenRepository refreshTokenRepository;

  @BeforeEach
  public void mockMvcSetup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    userRepository.deleteAll();
  }

  @DisplayName("createNewAccessToken: 새로운 액세스 토큰 발급")
  @Test
  public void createNewAccessToken() throws Exception {
    
    //given - 테스트 유저생성, jjwt 라이브러리 사용해 토큰 생성해 DB에 저장
    // 토큰 생성 API 의 요청 본문에 리프레시 토큰을 포함해 요청 객체 생성
    final String url = "/api/token";

    User testUser = userRepository.save(User.builder()
            .email("user@gmail.cojm")
            .password("test")
            .build());

    String refreshToken = JwtFactory.builder()
            .claims(Map.of("id", testUser.getId()))
            .build()
            .createToken(jwtProperties);

    refreshTokenRepository.save(new RefreshToken(testUser.getId(), refreshToken));

    CreateAccessTokenRequest request = new CreateAccessTokenRequest();
    request.setRefreshToken(refreshToken);
    final String requestBody = objectMapper.writeValueAsString(request);

    //when - API 토큰 추가 요청, 타입은 JSON, given에서 만들어둔 객체를 오청 본문에 같이 보냄
    ResultActions resultActions = mockMvc.perform(post(url)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(requestBody));

    //then - 응답 코드가 201 Created 인지 확인하고 응답으로 온 액세스 토큰이 null이 아닌지 확인
    resultActions
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.accessToken").isNotEmpty());

  }
}
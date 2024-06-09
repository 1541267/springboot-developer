package com.yoonsub.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;

@Getter
@Setter
public class CreateAccessTokenRequest {

  private String refreshToken;
}

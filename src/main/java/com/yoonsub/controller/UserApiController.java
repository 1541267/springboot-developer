package com.yoonsub.controller;

import com.yoonsub.dto.AddUserRequest;
import com.yoonsub.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserApiController {

  private final UserService userService;

  @PostMapping("/user")
  public String signup(AddUserRequest request) {
    userService.save(request);
    return "redirect:login";
  }

  @GetMapping("/logout")
  public String logout(HttpServletRequest request, HttpServletResponse response) {
    new SecurityContextLogoutHandler().logout(request, response,
            SecurityContextHolder.getContext().getAuthentication());
    // getContext : 현재 실행중인 스레드의 SecurityContext(보안 관련 정보 객체: 현재 인증 사용자에 대한 정보)를 반환
    // getAuthentication : 현재 사용자의 인증 정보 반환 (자격 증명 : 사용자 이름, 암호, 권한 정보(역할) 을 포함)

    return "redirect:/login";
  }
}

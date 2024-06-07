package com.yoonsub.config;


import com.yoonsub.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

  private final UserDetailService userService;


  // 시큐리티 기능 비활성화
  @Bean
  public WebSecurityCustomizer configure() {
    return (web) -> web.ignoring()
            .requestMatchers(toH2Console())
            .requestMatchers(new AntPathRequestMatcher("/static/**"));
  }

  // 특정 HTTP 요청에 대한 웹 기반 보안 구성
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
            .csrf(AbstractHttpConfigurer::disable)

            .authorizeHttpRequests(auth -> auth.requestMatchers(  // 인증, 인가 설정 (permitAll)
                            new AntPathRequestMatcher("/login"),
                            new AntPathRequestMatcher("/signup"),
                            new AntPathRequestMatcher("/user"))
                    .permitAll().anyRequest().authenticated())

            .formLogin(formLogin -> formLogin
                    .loginPage("/login")
                    .defaultSuccessUrl("/articles"))

            .logout(logout -> logout
                    .logoutSuccessUrl("/login")
                    .invalidateHttpSession(true)
            )

            .build();
  }

  @Bean   //인증 관리자 관련 설정, 조회된 사용자의 정보, 암호화된 비밀번호를 인증 매니저에 저장
  public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailService userDetailService) throws Exception {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userService);      // 사용자 정보를 가져올 서비스 설정, UserDetailsService를 상속받은 클래스여야 한다.
    authProvider.setPasswordEncoder(bCryptPasswordEncoder); // 패스워드 암호화 인코더 설정
    return new ProviderManager(authProvider);
  }


  @Bean // 패스워드 인코더 빈 등록
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

}

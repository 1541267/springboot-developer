package com.yoonsub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableJpaAuditing
@SpringBootApplication
public class SpringbootDeveloperApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringbootDeveloperApplication.class, args);
  }

}

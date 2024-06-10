package com.yoonsub.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserViewController {

    @GetMapping("/login")
    public String login() {
//      return "Login";
      return "oauthLogin";
    }

    @GetMapping("/signup")
    public String singup() {
      return "singup";
    }
}

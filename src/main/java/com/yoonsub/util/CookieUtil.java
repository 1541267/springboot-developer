package com.yoonsub.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.SerializationUtils;

import java.util.Base64;

public class CookieUtil {

  
  // 요청값(이름, 값, 만료 기간)을 바탕으로 HTTP 응답에 쿠키 추가,
  public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
    Cookie cookie = new Cookie(name, value);
    cookie.setPath("/");
    cookie.setMaxAge(maxAge);
    response.addCookie(cookie);
  }

  //쿠키의 이름을 입력받아 쿠키 삭제
  // 실제로 삭제하는 방법은 없어 파라미터로 넘어온 쿠키를 빈 값으로 바꾸고 만료 시간을 0으로 설정
  // 쿠키가 재생성 되자마자 만료 처리.
  public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
    Cookie[] cookies = request.getCookies();
    if(cookies == null) {
      return;
    }

    for(Cookie cookie : cookies) {
      if(name.equals(cookie.getName())) {
        cookie.setValue("");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
      }
    }
  }

  // 객체를 직렬화해 쿠키의 값으로 변환, 쿠키에 들어갈 값으로 변환
  // 직렬화 : 자바 시스템 Object or Data 를 외부 자바 시스템에서도 사용할 수 있도록 byte 형태로 변환하는 기술
  public static String serialize(Object obj) {
    return Base64.getUrlEncoder()
            .encodeToString(SerializationUtils.serialize(obj));
  }

  //쿠키를 역직렬화해 객체로 변환
  // deserialize deprecated -> 커스텀 직렬화 or Kryo, Protostuff 라이브러리 or JSON/XML 데이터 포맷 사용
  public static <T> T deserialize(Cookie cookie, Class<T> cls) {
    return cls.cast(
            SerializationUtils.deserialize(Base64.getUrlDecoder().decode(cookie.getValue()))
    );
  }
//  public static <T> T deserialize(Cookie cookie, Class<T> cls) {      //gpt
//    ObjectMapper objectMapper = new ObjectMapper();
//    byte[] data = Base64.getUrlDecoder().decode(cookie.getValue());
//    try {
//      return objectMapper.readValue(data, cls);
//    } catch (IOException e) {
//      throw new RuntimeException("Failed to deserialize object", e);
//    }
//  }




}

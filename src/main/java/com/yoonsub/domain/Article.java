package com.yoonsub.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity   //엔티티 지정
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY) //기본키 자동 1씩 증가
  @Column(name = "id", updatable = false)
  private Long id;

  @Column(name = "title", nullable = false)  // 'title'을, not null 컬럼 매핑
  private String title;

  @Column(name = "content", nullable = false)
  private String content;

  @Builder // 빌더 패턴으로 객체 생성
  public Article(String title, String content){
    this.title = title;
    this.content = content;
  }

  public void update(String title, String content) {
    this.title = title;
    this.content = content;
  }

//  protected Article() { // 기본 생성자 , NoArgsConstrucotor 사용
//  }
//  public Long getId() {
//    return id;
//  }
//
//  public String getTitle() {
//    return title;
//  }
//
//  public String getContent() {
//    return content;
//  }
}

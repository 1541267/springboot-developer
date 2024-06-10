package com.yoonsub.service;

import com.yoonsub.domain.Article;
import com.yoonsub.dto.AddArticleRequest;
import com.yoonsub.dto.UpdateArticleRequest;
import com.yoonsub.repository.BlogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor  //final or @NotNull 붙은 필드 생성자 추가
@Service  //빈 등록
public class BlogService {

  private final BlogRepository blogRepository;

  public Article save(AddArticleRequest req, String userName) {
    return blogRepository.save(req.toEntity(userName)); // 블로그 글 추가 메서드
  }

  public List<Article> findAll() { return blogRepository.findAll();}


  public Article findById(long id) {
    return blogRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
  }


//  public void delete(long id) { blogRepository.deleteById(id); }
//  337p 삭제 로직, 토큰을 이용해 작성자 글인지 검증
  public void delete(long id) {
    Article article = blogRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
    // 337p 작성자 토큰 검증
    authorizeArticleAuthor(article);
    blogRepository.delete(article);
  }

  @Transactional
  public Article update(long id, UpdateArticleRequest request) {
    Article article = blogRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("not found: " + id));

    // 337p 작성자 토큰 검증
    authorizeArticleAuthor(article);
    article.update(request.getTitle(), request.getContent());

    return article;
  }

  // 337p 작성자 토큰 검증
  private static void authorizeArticleAuthor(Article article) {
    String userName = SecurityContextHolder.getContext().getAuthentication().getName();

    if(!article.getAuthor().equals(userName)) {
      throw new IllegalArgumentException("not authorized");
    }

  }
}

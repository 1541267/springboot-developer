package com.yoonsub.service;

import com.yoonsub.domain.Article;
import com.yoonsub.dto.AddArticleRequest;
import com.yoonsub.dto.UpdateArticleRequest;
import com.yoonsub.repository.BlogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor  //final or @NotNull 붙은 필드 생성자 추가
@Service  //빈 등록
public class BlogService {
  
  private final BlogRepository blogRepository;

  public Article save(AddArticleRequest req) {
    return blogRepository.save(req.toEntity()); // 블로그 글 추가 메서드
  }

  public List<Article> findAll() { return blogRepository.findAll();}


  public Article findById(long id) {
    return blogRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
  }


  public void delete(long id) { blogRepository.deleteById(id); }

  @Transactional
  public Article update(long id, UpdateArticleRequest req) {
    Article article = blogRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("not found: " + id));

    article.update(req.getTitle(), req.getContent());

    return article;
  }
}

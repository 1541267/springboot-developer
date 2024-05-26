package com.yoonsub.controller;


import com.yoonsub.domain.Article;
import com.yoonsub.dto.AddArticleRequest;
import com.yoonsub.dto.ArticleResponse;
import com.yoonsub.dto.UpdateArticleRequest;
import com.yoonsub.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BlogApiController {

  private final BlogService blogService;

  @PostMapping("/api/articles")
  public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest req) {

    Article savedArticle = blogService.save(req);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
  }

  @GetMapping("/api/articles")
  public ResponseEntity<List<ArticleResponse>> findAllArticles() {
    List<ArticleResponse> articles = blogService.findAll()
            .stream()
            .map(ArticleResponse::new)
            .toList();

    return ResponseEntity.ok()
            .body(articles);

  }
  @GetMapping("/api/articles/{id}")
  public ResponseEntity<ArticleResponse> findArticle(@PathVariable long id) {
    Article article = blogService.findById(id);

    return ResponseEntity.ok()
            .body(new ArticleResponse(article));
  }

  @DeleteMapping("/api/articles/{id}")
  public ResponseEntity<Void> deleteArticle(@PathVariable long id) {
    blogService.delete(id);

    return ResponseEntity.ok().build();
  }

  @PutMapping("/api/articles/{id}")
  public ResponseEntity<Article> updateArticle(@PathVariable long id, @RequestBody UpdateArticleRequest req) {
    Article updatedArticle = blogService.update(id, req);

    return ResponseEntity.ok()
            .body(updatedArticle);
  }

}

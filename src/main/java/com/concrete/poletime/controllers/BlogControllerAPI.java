package com.concrete.poletime.controllers;

import com.concrete.poletime.blog.BlogPostService;
import com.concrete.poletime.dto.BlogPostDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/blog")
@Validated
public class BlogControllerAPI {

  private BlogPostService blogPostService;

  @Autowired
  public BlogControllerAPI(BlogPostService blogPostService) {
    this.blogPostService = blogPostService;
  }

  @GetMapping("/all")
  public ResponseEntity getAllBlogPosts() {
    try {
      return ResponseEntity.status(200).body(blogPostService.getAllBlogPosts());
    } catch (Exception e) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          e.getMessage(),
          e
      );
    }
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @PostMapping("post")
  public ResponseEntity postNew(@Valid @RequestBody BlogPostDTO blogPostDTO) {
    try {
      return ResponseEntity.status(200).body(blogPostService.createNewBlogPost(blogPostDTO));
    } catch (Exception e) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          e.getMessage(),
          e
      );
    }
  }
}

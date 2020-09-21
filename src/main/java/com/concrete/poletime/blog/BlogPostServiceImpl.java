package com.concrete.poletime.blog;

import com.concrete.poletime.dto.BlogPostDTO;
import com.concrete.poletime.exceptions.DateConversionException;
import com.concrete.poletime.exceptions.NoBlogPostRepresentedException;
import com.concrete.poletime.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.util.Date;
import java.util.List;

@Service
public class BlogPostServiceImpl implements BlogPostService {

  private BlogPostRepository blogPostRepository;

  @Autowired
  public BlogPostServiceImpl(BlogPostRepository blogPostRepository) {
    this.blogPostRepository = blogPostRepository;
  }

  @Override
  public List<BlogPost> getAllBlogPosts() throws NoBlogPostRepresentedException {
    return blogPostRepository.getAllBlogPosts().orElseThrow(
        () -> new NoBlogPostRepresentedException("No any blogposts have been found!")
    );
  }

  @Override
  public BlogPost createNewBlogPost(BlogPostDTO blogPostDTO) throws DateConversionException, ValidationException {
    Date createdAt = new Date(System.currentTimeMillis());
    BlogPost newPost = new BlogPost(blogPostDTO.getTitle(), blogPostDTO.getDescription(), createdAt);
    return saveBlogPost(newPost);
  }

  @Override
  public BlogPost saveBlogPost(BlogPost blogPost) throws PersistenceException {
    try {
      return blogPostRepository.save(blogPost);
    } catch (Exception e) {
      throw new PersistenceException("Could not save given blogpost to DB !", e);
    }
  }
}

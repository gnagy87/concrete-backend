package com.concrete.poletime.blog;

import com.concrete.poletime.dto.BlogPostDTO;
import com.concrete.poletime.exceptions.DateConversionException;
import com.concrete.poletime.exceptions.NoBlogPostRepresentedException;
import com.concrete.poletime.exceptions.ValidationException;

import javax.persistence.PersistenceException;
import java.util.List;

public interface BlogPostService {
  List<BlogPost> getAllBlogPosts() throws NoBlogPostRepresentedException;
  BlogPost createNewBlogPost(BlogPostDTO blogPostDTO) throws DateConversionException, ValidationException;
  BlogPost saveBlogPost(BlogPost blogPost) throws PersistenceException;
}

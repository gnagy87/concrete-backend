package com.concrete.poletime.blog;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "blogposts")
public class BlogPost {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String title;
  private String description;
  @Column(name = "created_at", updatable = false)
  @CreationTimestamp
  private Date createdAt;

  public BlogPost(String title, String description, Date createdAt) {
    this.title = title;
    this.description = description;
    this.createdAt = createdAt;
  }
}

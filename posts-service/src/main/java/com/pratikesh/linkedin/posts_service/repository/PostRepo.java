package com.pratikesh.linkedin.posts_service.repository;

import com.pratikesh.linkedin.posts_service.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepo extends JpaRepository<Post, Long> {

}

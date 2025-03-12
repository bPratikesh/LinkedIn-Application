package com.pratikesh.linkedin.posts_service.service;

import com.pratikesh.linkedin.posts_service.entity.PostLike;
import com.pratikesh.linkedin.posts_service.exception.BadRequestException;
import com.pratikesh.linkedin.posts_service.exception.ResourceNotFoundException;
import com.pratikesh.linkedin.posts_service.repository.PostLikeRepo;
import com.pratikesh.linkedin.posts_service.repository.PostRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostLikeService {

    private final PostLikeRepo postLikeRepo;
    private final PostRepo postRepo;

    public void likePost(Long postId, Long userId){
        log.info("Attempting to like the post with id: {}", postId);
        boolean exists = postRepo.existsById(postId);
        if(!exists) throw new ResourceNotFoundException("Post not found with id: "+ postId);

        boolean alreadyLiked = postLikeRepo.existsByUserIdAndPostId(userId, postId);
        if (alreadyLiked) throw new BadRequestException("Cannot like the same post again!!");

        PostLike postLike = new PostLike();
        postLike.setPostId(postId);
        postLike.setUserId(userId);
        postLikeRepo.save(postLike);
        log.info("You have liked the post with id: {} successfully", postId);
    }

    public void unlikePost(Long postId, long userId) {
        log.info("Attempting to unlike the post with id: {}", postId);
        boolean exists = postRepo.existsById(postId);
        if(!exists) throw new ResourceNotFoundException("Post not found with id: "+ postId);

        boolean alreadyLiked = postLikeRepo.existsByUserIdAndPostId(userId, postId);
        if (!alreadyLiked) throw new BadRequestException("Cannot unlike the post which is not liked");

        postLikeRepo.deleteByUserIdAndPostId(userId, postId);

        log.info("You have unliked the post with id: {} successfully", postId);
    }
}

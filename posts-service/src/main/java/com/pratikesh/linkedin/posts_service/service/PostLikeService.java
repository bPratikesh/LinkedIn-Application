package com.pratikesh.linkedin.posts_service.service;

import com.pratikesh.linkedin.posts_service.auth.UserContextHolder;
import com.pratikesh.linkedin.posts_service.entity.Post;
import com.pratikesh.linkedin.posts_service.entity.PostLike;
import com.pratikesh.linkedin.posts_service.event.PostLikedEvent;
import com.pratikesh.linkedin.posts_service.exception.BadRequestException;
import com.pratikesh.linkedin.posts_service.exception.ResourceNotFoundException;
import com.pratikesh.linkedin.posts_service.repository.PostLikeRepo;
import com.pratikesh.linkedin.posts_service.repository.PostRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostLikeService {

    private final PostLikeRepo postLikeRepo;
    private final PostRepo postRepo;
    private final KafkaTemplate<Long, PostLikedEvent> kafkaTemplate;

    public void likePost(Long postId){
        Long userId = UserContextHolder.getCurrentUserId();
        log.info("Attempting to like the post with id: {}", postId);

        Post post = postRepo.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post not found with id: "+postId));

        boolean alreadyLiked = postLikeRepo.existsByUserIdAndPostId(userId, postId);
        if (alreadyLiked) throw new BadRequestException("Cannot like the same post again!!");

        PostLike postLike = new PostLike();
        postLike.setPostId(postId);
        postLike.setUserId(userId);
        postLikeRepo.save(postLike);
        log.info("You have liked the post with id: {} successfully", postId);

        PostLikedEvent postLikedEvent = PostLikedEvent.builder()
                .postId(postId)
                .likedByUserId(userId)
                .creatorId(post.getUserId())
                .build();
        kafkaTemplate.send("post-liked-topic", postId, postLikedEvent);
    }

    public void unlikePost(Long postId) {
        Long userId = UserContextHolder.getCurrentUserId();
        log.info("Attempting to unlike the post with id: {}", postId);
        boolean exists = postRepo.existsById(postId);
        if(!exists) throw new ResourceNotFoundException("Post not found with id: "+ postId);

        boolean alreadyLiked = postLikeRepo.existsByUserIdAndPostId(userId, postId);
        if (!alreadyLiked) throw new BadRequestException("Cannot unlike the post which is not liked");

        postLikeRepo.deleteByUserIdAndPostId(userId, postId);

        log.info("You have unliked the post with id: {} successfully", postId);
    }
}

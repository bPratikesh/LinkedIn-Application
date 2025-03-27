package com.pratikesh.linkedin.posts_service.service;

import com.pratikesh.linkedin.posts_service.auth.UserContextHolder;
import com.pratikesh.linkedin.posts_service.clients.ConnectionsClient;
import com.pratikesh.linkedin.posts_service.dto.PersonDto;
import com.pratikesh.linkedin.posts_service.dto.PostCreateRequestDto;
import com.pratikesh.linkedin.posts_service.dto.PostDto;
import com.pratikesh.linkedin.posts_service.entity.Post;
import com.pratikesh.linkedin.posts_service.event.PostCreatedEvent;
import com.pratikesh.linkedin.posts_service.exception.ResourceNotFoundException;
import com.pratikesh.linkedin.posts_service.repository.PostRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepo postRepo;
    private final ModelMapper modelMapper;
    private final ConnectionsClient connectionsClient;

    private final KafkaTemplate<Long, PostCreatedEvent> kafkaTemplate;


    public PostDto createPost(PostCreateRequestDto postDto) {
        Long userId = UserContextHolder.getCurrentUserId();
        Post post = modelMapper.map(postDto, Post.class);
        post.setUserId(userId);

        Post savedPost = postRepo.save(post);
        PostCreatedEvent postCreatedEvent = PostCreatedEvent.builder()
                .postId(savedPost.getId())
                .creatorId(userId)
                .content(savedPost.getContent())
                .build();
        kafkaTemplate.send("post-created-topic", postCreatedEvent);
        return modelMapper.map(savedPost, PostDto.class);
    }

    public PostDto getPostById(Long postId) {
        log.debug("Retrieving post with id: {}", postId);
        Post post = postRepo.findById(postId).orElseThrow(()->
                new ResourceNotFoundException("Post not found with id: "+postId));
        return modelMapper.map(post, PostDto.class);
    }

    public List<PostDto> getAllPostsOfUser(Long userId) {
        List<Post> post = postRepo.findByUserId(userId);

        return post
                .stream()
                .map((element) -> modelMapper.map(element, PostDto.class))
                .collect(Collectors.toList());
    }
}

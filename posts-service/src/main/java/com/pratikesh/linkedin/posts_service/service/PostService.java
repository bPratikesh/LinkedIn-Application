package com.pratikesh.linkedin.posts_service.service;

import com.pratikesh.linkedin.posts_service.auth.UserContextHolder;
import com.pratikesh.linkedin.posts_service.clients.ConnectionsClient;
import com.pratikesh.linkedin.posts_service.dto.PersonDto;
import com.pratikesh.linkedin.posts_service.dto.PostCreateRequestDto;
import com.pratikesh.linkedin.posts_service.dto.PostDto;
import com.pratikesh.linkedin.posts_service.entity.Post;
import com.pratikesh.linkedin.posts_service.exception.ResourceNotFoundException;
import com.pratikesh.linkedin.posts_service.repository.PostRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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

    public PostDto createPost(PostCreateRequestDto postDto, Long userId) {
        Post post = modelMapper.map(postDto, Post.class);
        post.setUserId(userId);

        Post savedPost = postRepo.save(post);
        return modelMapper.map(savedPost, PostDto.class);
    }

    public PostDto getPostById(Long postId) {
        log.debug("Retrieving post with id: {}", postId);

        Long userId = UserContextHolder.getCurrentUserId();

        List<PersonDto> firstConnections = connectionsClient.getFirstConnections();

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

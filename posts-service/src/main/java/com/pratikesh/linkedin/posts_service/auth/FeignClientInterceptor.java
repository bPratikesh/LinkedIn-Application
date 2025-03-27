package com.pratikesh.linkedin.posts_service.auth;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FeignClientInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {

        Long userId = UserContextHolder.getCurrentUserId();
        log.info("User id:"+userId);

        if (userId != null){
            requestTemplate.header("X-User-Id", userId.toString());
        }
    }
}

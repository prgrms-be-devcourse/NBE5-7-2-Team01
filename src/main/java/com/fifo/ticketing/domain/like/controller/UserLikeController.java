package com.fifo.ticketing.domain.like.controller;

import com.fifo.ticketing.domain.like.dto.LikeRequest;
import com.fifo.ticketing.domain.like.service.LikeService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/like")
public class UserLikeController {

    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<String> toggleLike(@RequestBody LikeRequest likeRequest){
        boolean liked = likeService.toggleLike(likeRequest);
        return ResponseEntity.ok(liked ? "Liked" : "Unliked");
    }

}


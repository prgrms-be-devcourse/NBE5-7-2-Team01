package com.fifo.ticketing.domain.like.controller;

import com.fifo.ticketing.domain.like.dto.LikeRequest;
import com.fifo.ticketing.domain.like.service.LikeService;
import com.fifo.ticketing.domain.user.dto.SessionUser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/like")
public class UserLikeController {

    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<String> toggleLike(@RequestBody LikeRequest likeRequest
        ,HttpSession httpSession){
        //LoginSuccessHandler에서 SessionUser로 저장했기 때문에
        SessionUser sessionUser = (SessionUser) httpSession.getAttribute("loginUser");

        // record라서 .getId()가 아니라 .id()
        boolean liked = likeService.toggleLike(sessionUser.id() , likeRequest);
        return ResponseEntity.ok(liked ? "Liked" : "Unliked");
    }

}


package com.sim.webflux.controller;

import com.sim.webflux.controller.dto.ProfileImageResponse;
import com.sim.webflux.controller.dto.UserResponse;
import com.sim.webflux.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    public Mono<UserResponse> getUserById(@PathVariable String userId){
        return userService.findById(userId)
                .map(user -> new UserResponse(user.getId(),
                        user.getName(),
                        user.getAge(),
                        user.getFollowCount()
                )).switchIfEmpty(
                        Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))
                );
    }
}

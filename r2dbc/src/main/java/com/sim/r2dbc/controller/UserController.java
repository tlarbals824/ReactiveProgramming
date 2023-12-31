package com.sim.r2dbc.controller;

import com.sim.r2dbc.controller.dto.ProfileImageResponse;
import com.sim.r2dbc.controller.dto.SignupUserRequest;
import com.sim.r2dbc.controller.dto.UserResponse;
import com.sim.r2dbc.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    public Mono<UserResponse> getUserById(@PathVariable String userId) {
        return ReactiveSecurityContextHolder.getContext()
                .flatMap(context -> {
                    String name = context.getAuthentication().getName();

                    if (!Objects.equals(name, userId)) {
                        return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED));
                    }

                    return userService.findById(userId)
                            .map(user -> new UserResponse(user.getId(),
                                    user.getName(),
                                    user.getAge(),
                                    user.getFollowCount(),
                                    user.getProfileImage()
                                            .map(profileImage ->
                                                    new ProfileImageResponse(profileImage.getId(), profileImage.getName(), profileImage.getUrl()))
                            )).switchIfEmpty(
                                    Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))
                            );
                }).switchIfEmpty(
                        Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))
                );
    }

    @PostMapping("/signup")
    public Mono<UserResponse> signupUser(@RequestBody SignupUserRequest signupUserRequest) {
        return userService.signupUser(signupUserRequest)
                .map(user -> new UserResponse(user.getId(),
                        user.getName(),
                        user.getAge(),
                        user.getFollowCount(),
                        user.getProfileImage()
                                .map(image -> new ProfileImageResponse(image.getId(), image.getName(), image.getUrl()))));
    }
}

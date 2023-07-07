package com.sim.r2dbc.service;

import com.sim.r2dbc.common.EmptyImage;
import com.sim.r2dbc.common.Image;
import com.sim.r2dbc.common.User;
import com.sim.r2dbc.common.repository.AuthEntity;
import com.sim.r2dbc.common.repository.UserEntity;
import com.sim.r2dbc.controller.dto.SignupUserRequest;
import com.sim.r2dbc.repository.UserR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final R2dbcEntityTemplate entityTemplate;
    private final UserR2dbcRepository userR2dbcRepository;
    private WebClient webClient = WebClient.create("http://localhost:8081");

    public Mono<User> findById(String userId) {
        return userR2dbcRepository.findById(Long.valueOf(userId))
                .flatMap(userEntity -> {
                    final String profileImageId = userEntity.getProfileImageId();

                    return requestImageUrl(profileImageId)
                            .map(image -> {
                                Optional<Image> profileImage = Optional.empty();
                                if (!(image instanceof EmptyImage)) {
                                    profileImage = Optional.of(image);
                                }
                                return map(userEntity, profileImage);
                            });

                });
    }

    private static User map(UserEntity userEntity, Optional<Image> profileImage) {
        return new User(userEntity.getId(),
                userEntity.getName(),
                userEntity.getAge(),
                profileImage,
                List.of(),
                0L);
    }

    public Mono<User> signupUser(SignupUserRequest signupUserRequest) {
        return userR2dbcRepository.save(new UserEntity(signupUserRequest.getName(), signupUserRequest.getAge(), signupUserRequest.getProfileImageId(), signupUserRequest.getPassword()))
                .flatMap(userEntity -> {
                    final String token = generateRandomToken();
                    final AuthEntity auth = new AuthEntity(userEntity.getId(), token);

                    return entityTemplate.insert(auth)
                            .map(authEntity -> userEntity);
                }).map(userEntity -> map(userEntity, Optional.of(new EmptyImage())));
    }

    private Mono<Image> requestImageUrl(String profileImageId) {
        Map<String, String> uriVariableMap = Map.of("imageId", profileImageId);
        return webClient.get()
                .uri("/api/images/{imageId}", uriVariableMap)
                .retrieve()
                .toEntity(ImageResponse.class)
                .map(resp -> resp.getBody())
                .map(imageResp -> new Image(imageResp.getId(), imageResp.getName(), imageResp.getUrl()))
                .switchIfEmpty(Mono.just(new EmptyImage()));
    }

    private String generateRandomToken(){
        StringBuilder token = new StringBuilder();
        for(int i =0;i<6;i++){
            char item = (char) ('A' + (Math.random() * 26));
            token.append(item);
        }
        return token.toString();
    }


}

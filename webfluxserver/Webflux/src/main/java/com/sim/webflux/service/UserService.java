package com.sim.webflux.service;

import com.sim.webflux.common.EmptyImage;
import com.sim.webflux.common.Image;
import com.sim.webflux.common.User;
import com.sim.webflux.repository.UserReactorRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private WebClient webClient = WebClient.create("http://localhost:8081");

    private final UserReactorRepository userReactorRepository = new UserReactorRepository();

    public Mono<User> findById(String userId) {
        return userReactorRepository.findById(userId)
                .flatMap(userEntity -> {
                    final String profileImageId = userEntity.getProfileImageId();

                    Map<String, String> uriVariableMap = Map.of("imageId", profileImageId);
                    return webClient.get()
                            .uri("/api/images/{imageId}", uriVariableMap)
                            .retrieve()
                            .toEntity(ImageResponse.class)
                            .map(resp -> resp.getBody())
                            .map(imageResp -> new Image(imageResp.getId(), imageResp.getName(), imageResp.getUrl()))
                            .switchIfEmpty(Mono.just(new EmptyImage()))
                            .map(image -> {
                                Optional<Image> profileImage = Optional.empty();
                                if(!(image instanceof EmptyImage)){
                                    profileImage = Optional.of(image);
                                }
                                return new User(userEntity.getId(),
                                        userEntity.getName(),
                                        userEntity.getAge(),
                                        profileImage,
                                        List.of(),
                                        0L);
                            });

                });
    }
}

package com.sim.r2dbc.repository;

import com.sim.r2dbc.common.repository.UserEntity;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
public class UserReactorRepository {

    private final Map<Long, UserEntity> userMap;

    public UserReactorRepository() {
        var user = new UserEntity(
                1L, "sim", 24, "1", "1q2w3e4r!");

        userMap = Map.of(1L, user);
    }

    @SneakyThrows
    public Mono<UserEntity> findById(Long userId) {
        return Mono.create(sink -> {
            log.info("UserRepository.findById: {}", userId);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            UserEntity user = userMap.get(userId);
            if (user == null) {
                sink.success();
            } else {
                sink.success(user);
            }
        });
    }
}

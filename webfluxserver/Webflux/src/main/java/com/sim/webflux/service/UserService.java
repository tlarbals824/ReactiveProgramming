package com.sim.webflux.service;

import com.sim.webflux.common.User;
import com.sim.webflux.repository.UserReactorRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class UserService {

    private final UserReactorRepository userReactorRepository = new UserReactorRepository();

    public Mono<User> findById(String userId) {
        return userReactorRepository.findById(userId)
                .map(userEntity -> {
                    return new User(userEntity.getId(),
                            userEntity.getName(),
                            userEntity.getAge(),
                            null,
                            List.of(),
                            0L);
                });
    }
}

package com.sim.reactor.blocking;

import com.sim.reactor.blocking.repository.ArticleRepository;
import com.sim.reactor.blocking.repository.FollowRepository;
import com.sim.reactor.blocking.repository.ImageRepository;
import com.sim.reactor.blocking.repository.UserRepository;
import com.sim.reactor.common.Article;
import com.sim.reactor.common.Image;
import com.sim.reactor.common.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class UserBlockingService {
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final ImageRepository imageRepository;
    private final FollowRepository followRepository;

    public Optional<User> getUserById(String id) {
        long startTime = System.currentTimeMillis();
        Optional<User> optionalUser = userRepository.findById(id)
                .map(user -> {
                    var image = imageRepository.findById(user.getProfileImageId())
                            .map(imageEntity -> {
                                return new Image(imageEntity.getId(), imageEntity.getName(), imageEntity.getUrl());
                            });

                    var articles = articleRepository.findAllByUserId(user.getId())
                            .stream().map(articleEntity ->
                                    new Article(articleEntity.getId(), articleEntity.getTitle(), articleEntity.getContent()))
                            .collect(Collectors.toList());

                    var followCount = followRepository.countByUserId(user.getId());

                    return new User(
                            user.getId(),
                            user.getName(),
                            user.getAge(),
                            image,
                            articles,
                            followCount
                    );
                });
        long endTime = System.currentTimeMillis();
        log.info("blocking time : {}", endTime - startTime);
        return optionalUser;
    }
}

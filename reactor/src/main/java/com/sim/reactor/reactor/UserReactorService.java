package com.sim.reactor.reactor;

import com.sim.reactor.common.Article;
import com.sim.reactor.common.EmptyImage;
import com.sim.reactor.common.Image;
import com.sim.reactor.common.User;
import com.sim.reactor.common.repository.UserEntity;
import com.sim.reactor.reactor.repository.ArticleReactorRepository;
import com.sim.reactor.reactor.repository.FollowReactorRepository;
import com.sim.reactor.reactor.repository.ImageReactorRepository;
import com.sim.reactor.reactor.repository.UserReactorRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class UserReactorService {
    private final UserReactorRepository userReactorRepository;
    private final ArticleReactorRepository articleReactorRepository;
    private final ImageReactorRepository imageReactorRepository;
    private final FollowReactorRepository followReactorRepository;

    @SneakyThrows
    public Mono<User> getUserById(String id) {
        return userReactorRepository.findById(id)
                .flatMap(this::getUser);
    }

    @SneakyThrows
    private Mono<User> getUser(UserEntity userEntity) {
        Context context = Context.of("user", userEntity);

        var imageMono = imageReactorRepository.findWithContext()
                .map(imageEntity ->
                        new Image(imageEntity.getId(), imageEntity.getName(), imageEntity.getUrl())
                ).onErrorReturn(new EmptyImage())
                .contextWrite(context);

        var articlesMono = articleReactorRepository.findAllWithContext()
                .skip(5)
                .take(2)
                .map(articleEntity ->
                        new Article(articleEntity.getId(), articleEntity.getTitle(), articleEntity.getContent())
                ).collectList()
                .contextWrite(context);

        var followCountMono = followReactorRepository.countWithContext()
                .contextWrite(context);

        return Mono.zip(imageMono, articlesMono, followCountMono)
                .map(resultTuple -> {
                    Image image = resultTuple.getT1();
                    List<Article> articles = resultTuple.getT2();
                    Long followCount = resultTuple.getT3();

                    Optional<Image> imageOptional = Optional.empty();
                    if (!(image instanceof EmptyImage)) {
                        imageOptional = Optional.of(image);
                    }

                    return new User(
                            userEntity.getId(),
                            userEntity.getName(),
                            userEntity.getAge(),
                            imageOptional,
                            articles,
                            followCount
                    );
                });
    }

}

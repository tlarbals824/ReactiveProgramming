package com.sim.completablefuture.future;

import com.sim.completablefuture.blocking.repository.ArticleRepository;
import com.sim.completablefuture.blocking.repository.FollowRepository;
import com.sim.completablefuture.blocking.repository.ImageRepository;
import com.sim.completablefuture.blocking.repository.UserRepository;
import com.sim.completablefuture.common.Article;
import com.sim.completablefuture.common.Image;
import com.sim.completablefuture.common.User;
import com.sim.completablefuture.common.repository.UserEntity;
import com.sim.completablefuture.future.repository.ArticleFutureRepository;
import com.sim.completablefuture.future.repository.FollowFutureRepository;
import com.sim.completablefuture.future.repository.ImageFutureRepository;
import com.sim.completablefuture.future.repository.UserFutureRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class UserNonBlockingService {
    private final UserFutureRepository userFutureRepository;
    private final ArticleFutureRepository articleFutureRepository;
    private final ImageFutureRepository imageFutureRepository;
    private final FollowFutureRepository followFutureRepository;

    @SneakyThrows
    public CompletableFuture<Optional<User>> getUserById(String id) {
        return userFutureRepository.findById(id)
                .thenComposeAsync(this::getUser);
    }

    @SneakyThrows
    private CompletableFuture<Optional<User>> getUser(Optional<UserEntity> userEntityOptional) {
        if (userEntityOptional.isEmpty()) {
            return CompletableFuture.completedFuture(Optional.empty());
        }

        var userEntity = userEntityOptional.get();

        var imageFuture = imageFutureRepository.findById(userEntity.getProfileImageId())
                .thenApplyAsync(imageEntityOptional ->
                        imageEntityOptional.map(imageEntity ->
                                new Image(imageEntity.getId(), imageEntity.getName(), imageEntity.getUrl()))
                );

        var articleFuture = articleFutureRepository.findAllByUserId(userEntity.getId())
                .thenApplyAsync(articleEntities ->
                        articleEntities.stream()
                                .map(articleEntity -> new Article(articleEntity.getId(), articleEntity.getTitle(), articleEntity.getContent()))
                                .collect(Collectors.toList())
                );

        var followFuture = followFutureRepository.countByUserId(userEntity.getId());

        return CompletableFuture.allOf(imageFuture, articleFuture, followFuture)
                .thenApplyAsync(v -> {
                    try {
                        var image = imageFuture.get();
                        var articles = articleFuture.get();
                        var followCount = followFuture.get();
                        return Optional.of(new User(
                                userEntity.getId(),
                                userEntity.getName(),
                                userEntity.getAge(),
                                image,
                                articles,
                                followCount
                        ));
                    } catch (InterruptedException | ExecutionException e) {
                        return Optional.empty();
                    }
                });
    }


//    public Optional<User> getUserById(String id){
//        try {
//            Optional<User> optionalUser = userFutureRepository.findById(id)
//                    .get()
//                    .map(user -> {
//                                var articleFuture = articleFutureRepository.findAllByUserId(user.getId());
//                                var imageFuture = imageFutureRepository.findById(user.getProfileImageId());
//                                var followFuture = followFutureRepository.countByUserId(user.getId());
//                                return CompletableFuture.allOf(articleFuture, imageFuture, followFuture)
//                                        .thenApply(v -> {
//                                            try {
//                                                var articles = articleFuture.get()
//                                                        .stream().map(articleEntity ->
//                                                                new Article(articleEntity.getId(), articleEntity.getTitle(), articleEntity.getContent()))
//                                                        .collect(Collectors.toList());
//
//                                                var image = imageFuture.get()
//                                                        .map(imageEntity -> new Image(imageEntity.getId(), imageEntity.getName(), imageEntity.getUrl()));
//
//                                                var followCount = followFuture.get();
//
//                                                return new User(
//                                                        user.getId(),
//                                                        user.getName(),
//                                                        user.getAge(),
//                                                        image,
//                                                        articles,
//                                                        followCount
//                                                );
//                                            } catch (InterruptedException | ExecutionException e) {
//                                                throw new RuntimeException(e);
//                                            }
//                                        }).join();
//                            }
//                    );
//            return optionalUser;
//        } catch (InterruptedException | ExecutionException e) {
//            throw new RuntimeException(e);
//        }
//    }

}

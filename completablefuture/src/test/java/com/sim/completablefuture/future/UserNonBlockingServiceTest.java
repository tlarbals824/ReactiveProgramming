package com.sim.completablefuture.future;

import com.sim.completablefuture.common.User;
import com.sim.completablefuture.future.repository.ArticleFutureRepository;
import com.sim.completablefuture.future.repository.FollowFutureRepository;
import com.sim.completablefuture.future.repository.ImageFutureRepository;
import com.sim.completablefuture.future.repository.UserFutureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

class UserNonBlockingServiceTest {
    UserNonBlockingService userNonBlockingService;
    UserFutureRepository userFutureRepository;
    ArticleFutureRepository articleFutureRepository;
    ImageFutureRepository imageFutureRepository;
    FollowFutureRepository followFutureRepository;

    @BeforeEach
    void setUp() {
        userFutureRepository = new UserFutureRepository();
        articleFutureRepository = new ArticleFutureRepository();
        imageFutureRepository = new ImageFutureRepository();
        followFutureRepository = new FollowFutureRepository();

        userNonBlockingService = new UserNonBlockingService(
                userFutureRepository, articleFutureRepository, imageFutureRepository, followFutureRepository
        );
    }

    @Test
    void getUserEmptyIfInvalidUserIdIsGiven() throws ExecutionException, InterruptedException {
        // given
        String userId = "invalid_user_id";

        // when
        Optional<User> user = userNonBlockingService.getUserById(userId).get();

        // then
        assertTrue(user.isEmpty());
    }

    @Test
    void testGetUser() throws ExecutionException, InterruptedException {
        // given
        String userId = "1234";

        // when
        Optional<User> optionalUser = userNonBlockingService.getUserById(userId).get();

        // then
        assertFalse(optionalUser.isEmpty());
        var user = optionalUser.get();
        assertEquals(user.getName(), "taewoo");
        assertEquals(user.getAge(), 32);

        assertFalse(user.getProfileImage().isEmpty());
        var image = user.getProfileImage().get();
        assertEquals(image.getId(), "image#1000");
        assertEquals(image.getName(), "profileImage");
        assertEquals(image.getUrl(), "https://dailyone.com/images/1000");

        assertEquals(2, user.getArticleList().size());

        assertEquals(1000, user.getFollowCount());
    }


}
package com.sim.completablefuture.future.repository;

import com.sim.completablefuture.common.repository.ImageEntity;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class ImageFutureRepository {

    private final Map<String, ImageEntity> imageMap;

    public ImageFutureRepository() {
        imageMap = Map.of(
                "image#1000", new ImageEntity("image#1000", "profileImage", "https://dailyone.com/images/1000")
        );
    }

    @SneakyThrows
    public CompletableFuture<Optional<ImageEntity>> findById(String id) {
        log.info("ImageRepository.findById: {}", id);
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            var image = imageMap.get(id);
            return Optional.ofNullable(image);
        });
    }
}

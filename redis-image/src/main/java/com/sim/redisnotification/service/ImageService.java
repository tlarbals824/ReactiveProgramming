package com.sim.redisnotification.service;

import com.sim.redisnotification.entity.common.Image;
import com.sim.redisnotification.entity.common.repository.ImageEntity;
import com.sim.redisnotification.repository.ImageReactorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageReactorRepository imageReactorRepository;

    public Mono<Image> getImageById(String imageId){
        return imageReactorRepository.findById(imageId)
                .map(getMapper());
    }

    public Mono<Image> createImage(String imageId, String name, String url){
        return imageReactorRepository.save(imageId, name, url)
                .map(getMapper());
    }

    private static Function<ImageEntity, Image> getMapper() {
        return imageEntity -> new Image(imageEntity.getId(), imageEntity.getName(), imageEntity.getUrl());
    }

}

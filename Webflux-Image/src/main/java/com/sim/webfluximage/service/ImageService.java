package com.sim.webfluximage.service;

import com.sim.webfluximage.entity.common.Image;
import com.sim.webfluximage.repository.ImageReactorRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ImageService {

    private ImageReactorRepository imageReactorRepository = new ImageReactorRepository();

    public Mono<Image> getImageById(String imageId){
        return imageReactorRepository.findById(imageId)
                .map(imageEntity -> new Image(imageEntity.getId(), imageEntity.getName(), imageEntity.getUrl()));
    }
}

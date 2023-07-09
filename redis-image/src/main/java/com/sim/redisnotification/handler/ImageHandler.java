package com.sim.redisnotification.handler;

import com.sim.redisnotification.handler.dto.CreateRequest;
import com.sim.redisnotification.handler.dto.ImageResponse;
import com.sim.redisnotification.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class ImageHandler {
    private final ImageService imageService;

    public Mono<ServerResponse> getImageById(ServerRequest serverRequest){
        final String imageId = serverRequest.pathVariable("imageId");

        return imageService.getImageById(imageId)
                .map(image -> new ImageResponse(image.getId(), image.getName(), image.getUrl()))
                .flatMap(imageResponse -> ServerResponse.ok().bodyValue(imageResponse))
                .onErrorMap(e -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Mono<ServerResponse> addImage(ServerRequest serverRequest){
        return serverRequest.bodyToMono(CreateRequest.class)
                .flatMap(createRequest -> imageService.createImage(createRequest.getId(), createRequest.getName(), createRequest.getUrl()))
                .flatMap(imageResponse -> ServerResponse.ok().bodyValue(imageResponse));
    }
}

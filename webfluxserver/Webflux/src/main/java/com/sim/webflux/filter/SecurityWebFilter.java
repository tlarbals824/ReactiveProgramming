package com.sim.webflux.filter;

import com.sim.webflux.auth.IamAuthentication;
import com.sim.webflux.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.Objects;

@RequiredArgsConstructor
@Component
public class SecurityWebFilter implements WebFilter {

    private final AuthService authService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        final ServerHttpResponse resp = exchange.getResponse();
        final String iam = exchange.getRequest().getHeaders().getFirst("X-I-AM");

        if (Objects.isNull(iam)) {
            resp.setStatusCode(HttpStatus.UNAUTHORIZED);
            return resp.setComplete();
        }

        return authService.getNameByToken(iam)
                .map(IamAuthentication::new)
                .flatMap(authentication -> chain.filter(exchange)
                        .contextWrite(context -> {
                            Context newContext = ReactiveSecurityContextHolder.withAuthentication(authentication);

                            return context.putAll(newContext);
                        })
                ).switchIfEmpty(Mono.defer(() -> {
                    resp.setStatusCode(HttpStatus.UNAUTHORIZED);
                    return resp.setComplete();
                }));
    }
}

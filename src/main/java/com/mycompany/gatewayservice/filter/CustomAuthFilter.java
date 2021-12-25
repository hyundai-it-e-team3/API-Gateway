package com.mycompany.gatewayservice.filter;

import java.util.List;
import java.util.Objects;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.mycompany.gatewayservice.security.JwtUtil;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class CustomAuthFilter extends AbstractGatewayFilterFactory<CustomAuthFilter.Config> {
    public CustomAuthFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

    		String jwt = null;
    		if(request.getHeaders().containsKey("Authorization")&& 
    				request.getHeaders().get("Authorization").get(0).startsWith("Bearer")) {
    			jwt = request.getHeaders().get("Authorization").get(0).substring(7);
    		} else {
    			 log.info("----------no toekn");
    			 return handleUnAuthorized(exchange); // 토큰이 일치하지 않을 때
    		}
    		
    			
    		
    		log.info("jwt : " + jwt);
    		
    		if(jwt != null) {
    			Claims claims = JwtUtil.validateToken(jwt);
    			if(claims != null) {
    				log.info("유효한 토큰");
    				String memberId = JwtUtil.getMemberId(claims);
    				String authority = JwtUtil.getAuthority(claims);
    				log.info("memberId : " + memberId);
    				log.info("authority : " + authority);
    				
    				
    			} else {
    				log.info("유효하지 않은 토큰");
    			}
    		}else {
    			return handleUnAuthorized(exchange); // 토큰이 일치하지 않을 때
    		}

            return chain.filter(exchange); // 토큰이 일치할 때

        });
    }

    private Mono<Void> handleUnAuthorized(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    public static class Config {

    }
}

package com.mycompany.gatewayservice.config;

import javax.servlet.annotation.WebFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;

@Configuration
public class CorsConfiguration {

  private static final String ALLOWED_HEADERS = "x-requested-with, authorization, Content-Type, Content-Length, Authorization, credential, X-XSRF-TOKEN";
  private static final String ALLOWED_METHODS = "GET, PUT, POST, DELETE, OPTIONS, PATCH";
  private static final String ALLOWED_ORIGIN = "*";
  private static final String MAX_AGE = "7200"; //2 hours (2 * 60 * 60) 

}
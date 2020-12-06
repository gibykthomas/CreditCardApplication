package com.sapient.creditcardApplication.route;

import com.sapient.creditcardApplication.handler.CreditcardHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;

@Configuration
public class CreditcardRouter {
  @Autowired
  CreditcardHandler creditcardHandler;
  @Bean
  RouterFunction<ServerResponse> route() {
    return RouterFunctions.nest(path("/creditcard"),
        RouterFunctions.nest(
            accept(MediaType.APPLICATION_JSON).or(contentType(MediaType.APPLICATION_JSON)),
            RouterFunctions.route(POST("/add"),creditcardHandler::addCreditcard)
                .andRoute(GET("/get_all"),creditcardHandler::getCreditcards)
        ));
  }
}


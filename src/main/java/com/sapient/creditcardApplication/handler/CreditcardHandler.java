package com.sapient.creditcardApplication.handler;

import com.sapient.creditcardApplication.domain.Creditcard;
import com.sapient.creditcardApplication.repository.CreditcardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Configuration
public class CreditcardHandler {
  @Autowired
  CreditcardRepository creditcardRepository;

  public Mono<ServerResponse> addCreditcard(ServerRequest request) {
      Mono<Creditcard> cardMono = request.bodyToMono(Creditcard.class);

      return cardMono
             .flatMap(card ->   ServerResponse.status(HttpStatus.CREATED)
                .contentType(APPLICATION_JSON)
                .body(creditcardRepository.save(card), Creditcard.class)
          );
    }

  public Mono<ServerResponse> getCreditcards(ServerRequest request) {
    Flux<Creditcard> cards = creditcardRepository.findAll();
    return ServerResponse.ok().
        contentType(APPLICATION_JSON)
        .body(cards, Creditcard.class);
  }
}

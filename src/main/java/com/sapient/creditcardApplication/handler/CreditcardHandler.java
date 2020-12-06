package com.sapient.creditcardApplication.handler;

import com.sapient.creditcardApplication.domain.Creditcard;
import com.sapient.creditcardApplication.domain.CreditcardResponse;
import com.sapient.creditcardApplication.domain.ErrorResponse;
import com.sapient.creditcardApplication.mapper.CreditcardMapper;
import com.sapient.creditcardApplication.repository.CreditcardRepository;
import com.sapient.creditcardApplication.utlils.PayloadValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.validation.AbstractBindingResult;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_IMPLEMENTED;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Configuration
public class CreditcardHandler {
  @Autowired
  CreditcardRepository creditcardRepository;
  @Autowired
  PayloadValidator payloadValidator;

  public Mono<ServerResponse> addCreditcard(ServerRequest request) {
      Mono<Creditcard> cardMono = request.bodyToMono(Creditcard.class);

    return cardMono.flatMap(card ->  creditcardRepository.findByNumber(card.getNumber())
        .flatMap(existingId ->
            ServerResponse.status(BAD_REQUEST).contentType(APPLICATION_JSON)
                .body(BodyInserters.fromValue(new ErrorResponse(String.valueOf(BAD_REQUEST.value()),"Creditcard is already added")))
        )
        .switchIfEmpty(
            validateUser(card)
                .switchIfEmpty(
                    creditcardRepository.save(card)
                        .flatMap(newCard -> ServerResponse.status(CREATED).contentType(APPLICATION_JSON)
                            .body(BodyInserters.fromValue(CreditcardMapper.responseDto(card))))
                )
        ));

  }

  public Mono<ServerResponse> getCreditcards(ServerRequest request) {
    Flux<Creditcard> cards = creditcardRepository.findAll();
    return ServerResponse.ok().
        contentType(APPLICATION_JSON)
        .body(cards.map(CreditcardMapper::responseDto), CreditcardResponse.class);
  }

  private Mono<ServerResponse> validateUser(Creditcard card) {
    return Mono.just(new BeanPropertyBindingResult(card, Creditcard.class.getName()))
        .doOnNext(err -> payloadValidator.validate(card, err))
        .filter(AbstractBindingResult::hasErrors)
        .flatMap(err ->
            ServerResponse.status(NOT_IMPLEMENTED)
                .contentType(APPLICATION_JSON)
                .body(BodyInserters.fromValue(new ErrorResponse(err.getAllErrors().get(0).getCode(),err.getAllErrors().get(0).getDefaultMessage())))

        );
  }


}

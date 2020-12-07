package com.sapient.creditcardApplication.handler;

import com.sapient.creditcardApplication.domain.Creditcard;
import com.sapient.creditcardApplication.domain.CreditcardResponse;
import com.sapient.creditcardApplication.domain.ErrorResponse;
import com.sapient.creditcardApplication.mapper.CreditcardMapper;
import com.sapient.creditcardApplication.repository.CreditcardRepository;
import com.sapient.creditcardApplication.utlils.Crypto;
import com.sapient.creditcardApplication.utlils.PayloadValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.validation.AbstractBindingResult;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@Configuration
public class CreditcardHandler {
  @Autowired
  CreditcardRepository creditcardRepository;
  @Autowired
  PayloadValidator payloadValidator;

  public Mono<ServerResponse> addCreditcard(ServerRequest request) {
      Mono<Creditcard> cardMono = request.bodyToMono(Creditcard.class);
      return cardMono.flatMap(card ->  creditcardRepository.findByNumber(Crypto.encrypt(card.getNumber()))
        .flatMap(existingId -> {
          log.info(String.format("Creditcard  - %s already exists",card.getNumber().replaceAll(".(?=.{4})", "X")));
          return ServerResponse.status(BAD_REQUEST).contentType(APPLICATION_JSON)
                  .body(BodyInserters.fromValue(new ErrorResponse(String.valueOf(BAD_REQUEST.value()), "Creditcard is already added")));
            }
        )
        .switchIfEmpty(
            validateCard(card)
                .switchIfEmpty(
                    creditcardRepository.save(CreditcardMapper.encryptCardDto(card))
                        .flatMap(newCard ->
                          ServerResponse.status(CREATED).contentType(APPLICATION_JSON)
                              .body(BodyInserters.fromValue(CreditcardMapper.responseDto(newCard)))
                        )
                )
        ));

  }

  public Mono<ServerResponse> getCreditcards(ServerRequest request) {
    Flux<Creditcard> cards = creditcardRepository.findAll();
    return ServerResponse.ok().
        contentType(APPLICATION_JSON)
        .body(cards.map(CreditcardMapper::responseDto), CreditcardResponse.class);
  }

  private Mono<ServerResponse> validateCard(Creditcard card) {
    return Mono.just(new BeanPropertyBindingResult(card, Creditcard.class.getName()))
        .doOnNext(err -> payloadValidator.validate(card, err))
        .filter(AbstractBindingResult::hasErrors)
        .flatMap(err -> {
          log.info(String.format("Error in validating the credit card - %s - %s",card.getNumber().replaceAll(".(?=.{4})", "X"),err.getAllErrors().get(0).getDefaultMessage()));
          return ServerResponse.status(BAD_REQUEST)
              .contentType(APPLICATION_JSON)
              .body(BodyInserters.fromValue(new ErrorResponse(err.getAllErrors().get(0).getCode(), err.getAllErrors().get(0).getDefaultMessage())));

        });
  }
}

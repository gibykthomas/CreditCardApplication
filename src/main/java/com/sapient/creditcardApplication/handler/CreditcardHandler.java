package com.sapient.creditcardApplication.handler;

import com.sapient.creditcardApplication.domain.Creditcard;
import com.sapient.creditcardApplication.domain.CreditcardResponse;
import com.sapient.creditcardApplication.domain.ErrorResponse;
import com.sapient.creditcardApplication.domain.Paging;
import com.sapient.creditcardApplication.mapper.CreditcardMapper;
import com.sapient.creditcardApplication.repository.CreditcardRepository;
import com.sapient.creditcardApplication.utlils.Crypto;
import com.sapient.creditcardApplication.utlils.PayloadValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.AbstractBindingResult;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@Configuration
public class CreditcardHandler {
  @Autowired
  CreditcardRepository creditcardRepository;
  @Autowired
  PayloadValidator payloadValidator;

  public Mono<ServerResponse> addCreditcard(ServerRequest request) {
      return request.bodyToMono(Creditcard.class)
              .doOnNext(payloadValidator::validate)
              .flatMap(card -> creditcardRepository.findByNumber(Crypto.encrypt(card.getNumber()))
                      .flatMap(existingId -> {
                                  log.info(String.format("Creditcard  - %s already exists",card.getNumber().replaceAll(".(?=.{4})", "X")));
                                  return ServerResponse.status(BAD_REQUEST).contentType(APPLICATION_JSON)
                                          .body(BodyInserters.fromValue(new ErrorResponse(BAD_REQUEST.value(), "Creditcard is already added")));
                              }
                      )
                      .switchIfEmpty(creditcardRepository.save(CreditcardMapper.encryptCardDto(card))
                              .flatMap(newCard ->
                                      ServerResponse.status(CREATED).contentType(APPLICATION_JSON)
                                              .body(BodyInserters.fromValue(CreditcardMapper.responseDto(newCard)))
                              )))
              .onErrorResume(ServerWebInputException.class, e -> ServerResponse.status(BAD_REQUEST).contentType(APPLICATION_JSON)
                      .body(BodyInserters.fromValue(new ErrorResponse(BAD_REQUEST.value(),e.getReason()))));
  }
  public Mono<ServerResponse> getCreditcards(ServerRequest request) {
      Paging paging = new Paging();
      Integer page =   request.queryParam("page").isPresent() ?Integer.parseInt(request.queryParam("page").get()) : paging.getPage();
      Integer size =   request.queryParam("size").isPresent() ?Integer.parseInt(request.queryParam("size").get()) : paging.getSize();
      Mono<PageRequest> pageRequest = Mono.just(PageRequest.of(page,size));

      Flux<Creditcard> cards = pageRequest.flatMapMany(pr -> creditcardRepository.findAllBy(pr));
      return cards
              .collectList()
              .flatMap(card -> {
                  if(card.isEmpty()) {
                      return ServerResponse.notFound().build();
                  }
                  else {
                      return ServerResponse.ok().body(cards.map(CreditcardMapper::responseDto), CreditcardResponse.class);
                  }
              });
        }
}

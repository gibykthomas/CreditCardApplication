package com.sapient.creditcardApplication.handler;

import java.math.BigDecimal;
import com.sapient.creditcardApplication.config.R2DBCConfiguration;
import com.sapient.creditcardApplication.domain.Creditcard;
import com.sapient.creditcardApplication.domain.CreditcardResponse;
import com.sapient.creditcardApplication.domain.ErrorResponse;
import com.sapient.creditcardApplication.repository.CreditcardRepository;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.springSecurity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
@AutoConfigureWebTestClient
@EnableAutoConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
class CreditcardHandlerTest {

  @Autowired
  private WebTestClient webClient;

  @Autowired
  private ApplicationContext context;

  @Autowired
  CreditcardRepository creditcardRepository;
  @Autowired
  R2DBCConfiguration r2DBCConfiguration;

  @BeforeAll
  public void setupWebTestClient() {
    webClient = WebTestClient.bindToApplicationContext(context)
        .apply(springSecurity())
        .configureClient()
        .build();
  }

  @Test
  void addCreditcard() {
    Creditcard card = new Creditcard("123","test1", BigDecimal.ZERO);

    this.creditcardRepository.save(card)
        .as(StepVerifier::create)
        .expectNextCount(1)
        .verifyComplete();

    creditcardRepository.findAll()
        .as(StepVerifier::create)
        .assertNext(card::equals)
        .verifyComplete();

    creditcardRepository.delete(card);
  }

  @Test
  void add_valid_creditcard() {
    Creditcard card = new Creditcard( "1234567890987658","1234567890987658","test1",BigDecimal.ZERO,BigDecimal.ZERO);
    CreditcardResponse creditcardResponse = new CreditcardResponse("1234567890987658","test1", BigDecimal.ZERO,BigDecimal.ZERO);

    webClient.mutateWith(csrf()).post()
        .uri("/creditcard/add")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(card))
        .exchange()
        .expectStatus()
        .isCreated()
        .expectBody(CreditcardResponse.class).consumeWith(exchange -> {
      CreditcardResponse created = exchange.getResponseBody();
      assertThat(created.getNumber(), is(card.getNumber()));
      assertThat(created.getCreditlimit(), is(card.getCreditLimit()));
    });
    creditcardRepository.delete(card);
  }

  void add_invalid_creditcard_more_length_card_number() {
    Creditcard card = new Creditcard( "1234567890987612345567","1234567890987612345567","test1",BigDecimal.ZERO,BigDecimal.ZERO);

    webClient.mutateWith(csrf()).post()
        .uri("/creditcard/add")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(card))
        .exchange()
        .expectStatus()
        .is4xxClientError()
        .expectBody(ErrorResponse.class).consumeWith(exchange -> {
      ErrorResponse errorResponse = exchange.getResponseBody();
      assertThat(exchange.getStatus(), is(HttpStatus.BAD_REQUEST));
      assertThat(errorResponse.getCode(), is("400"));
    });
    creditcardRepository.delete(card);
  }

  @Test
  void add_invalid_creditcard_non_digit_card_number() {
    Creditcard card = new Creditcard( "12345678909876123A","12345678909876123A","test1",BigDecimal.ZERO,BigDecimal.ZERO);

    webClient.mutateWith(csrf()).post()
        .uri("/creditcard/add")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(card))
        .exchange()
        .expectStatus()
        .is4xxClientError()
        .expectBody(ErrorResponse.class).consumeWith(exchange -> {
      ErrorResponse errorResponse = exchange.getResponseBody();
      assertThat(exchange.getStatus(), is(HttpStatus.BAD_REQUEST));
      assertThat(errorResponse.getCode(), is("400"));
    });
  }

  @Test
  void add_invalid_creditcard_non_luhn10_card_number() {
    Creditcard card = new Creditcard( "12345678909876121","12345678909876121","test1",BigDecimal.ZERO,BigDecimal.ZERO);

    webClient.mutateWith(csrf()).post()
        .uri("/creditcard/add")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(card))
        .exchange()
        .expectStatus()
        .is4xxClientError()
        .expectBody(ErrorResponse.class).consumeWith(exchange -> {
      ErrorResponse errorResponse = exchange.getResponseBody();
      assertThat(exchange.getStatus(), is(HttpStatus.BAD_REQUEST));
      assertThat(errorResponse.getCode(), is("400"));
    });
    creditcardRepository.delete(card);
  }

  @Test
  void add_invalid_creditcard_blank_card_number() {
    Creditcard card = new Creditcard( "","","test1",BigDecimal.ZERO,BigDecimal.ZERO);

    webClient.mutateWith(csrf()).post()
        .uri("/creditcard/add")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(card))
        .exchange()
        .expectStatus()
        .is4xxClientError()
        .expectBody(ErrorResponse.class).consumeWith(exchange -> {
      ErrorResponse errorResponse = exchange.getResponseBody();
      assertThat(exchange.getStatus(), is(HttpStatus.BAD_REQUEST));
      assertThat(errorResponse.getCode(), is("400"));
    });
    creditcardRepository.delete(card);
  }

  @Test
  void add_invalid_creditcard_blank_card_name() {
    Creditcard card = new Creditcard( "1234567890987658","1234567890987658","",BigDecimal.ZERO,BigDecimal.ZERO);

    webClient.mutateWith(csrf()).post()
        .uri("/creditcard/add")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(card))
        .exchange()
        .expectStatus()
        .is4xxClientError()
        .expectBody(ErrorResponse.class).consumeWith(exchange -> {
      ErrorResponse errorResponse = exchange.getResponseBody();
      assertThat(exchange.getStatus(), is(HttpStatus.BAD_REQUEST));
      assertThat(errorResponse.getCode(), is("400"));
    });
    creditcardRepository.delete(card);
  }





}

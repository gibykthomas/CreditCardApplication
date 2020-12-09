package com.sapient.creditcardApplication.repository;

import com.sapient.creditcardApplication.domain.Creditcard;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CreditcardRepository extends ReactiveSortingRepository<Creditcard,String> {
  Mono<Creditcard> findByNumber(String number);
  Flux<Creditcard> findAllBy(Pageable pageable);
}

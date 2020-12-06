package com.sapient.creditcardApplication.repository;

import com.sapient.creditcardApplication.domain.Creditcard;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditcardRepository extends ReactiveCrudRepository<Creditcard,String> {
}

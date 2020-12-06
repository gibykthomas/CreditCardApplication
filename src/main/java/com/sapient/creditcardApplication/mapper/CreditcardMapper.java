package com.sapient.creditcardApplication.mapper;

import com.sapient.creditcardApplication.domain.Creditcard;
import com.sapient.creditcardApplication.domain.CreditcardResponse;

public class CreditcardMapper {
  public static CreditcardResponse responseDto(Creditcard card) {
    return CreditcardResponse
        .builder()
        .number(card.getNumber())
        .name(card.getName())
        .balance(card.getBalance())
        .creditlimit(card.getCreditLimit())
        .build();
  }

}

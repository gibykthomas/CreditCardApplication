package com.sapient.creditcardApplication.mapper;

import com.sapient.creditcardApplication.domain.Creditcard;
import com.sapient.creditcardApplication.domain.CreditcardResponse;
import com.sapient.creditcardApplication.utlils.Crypto;

public class CreditcardMapper {
  public static CreditcardResponse responseDto(Creditcard card) {
    return CreditcardResponse
        .builder()
        .number(Crypto.decrypt(card.getNumber()))
        .name(card.getName())
        .balance(card.getBalance())
        .creditlimit(card.getCreditLimit())
        .build();
  }

  public static Creditcard encryptCardDto(Creditcard card) {
    return Creditcard
        .builder()
        .number(Crypto.encrypt(card.getNumber()))
        .name(card.getName())
        .balance(card.getBalance())
        .creditLimit(card.getCreditLimit())
        .build();
  }

}

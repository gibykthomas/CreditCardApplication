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

  public static Creditcard encryptCardDto(Creditcard t) {
    return Creditcard
        .builder()
        .number(Crypto.encrypt(t.getNumber()))
        .name(t.getName())
        .balance(t.getBalance())
        .creditLimit(t.getCreditLimit())
        .build();
  }

}

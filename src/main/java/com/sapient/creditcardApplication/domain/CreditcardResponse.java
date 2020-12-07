package com.sapient.creditcardApplication.domain;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreditcardResponse {
  private  String number;
  private  String name;
  private BigDecimal balance;
  private  BigDecimal creditlimit;
}

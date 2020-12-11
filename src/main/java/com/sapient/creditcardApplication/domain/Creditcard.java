package com.sapient.creditcardApplication.domain;

import java.math.BigDecimal;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@Getter
@AllArgsConstructor
@Table("creditcard")
public class Creditcard implements Persistable<String>{
  @Id
  private String id;
  private String number;
  private String name;
  private BigDecimal balance;
  private BigDecimal creditLimit;

  public Creditcard() {
    this.balance = this.balance != null ? this.balance : BigDecimal.ZERO;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  @Transient
  public boolean isNew() {
    return id==null;
  }
}

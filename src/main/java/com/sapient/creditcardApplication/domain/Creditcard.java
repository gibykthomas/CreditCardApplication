package com.sapient.creditcardApplication.domain;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
@Table("creditcard")
public class Creditcard implements Persistable<String>{
  @Id
  private String id;
  private String number;
  private String name;
  private BigDecimal balance;
  private BigDecimal creditLimit;

  @Override
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigDecimal getBalance() {
    return balance;
  }

  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

  public BigDecimal getCreditLimit() {
    return creditLimit;
  }

  public void setCreditLimit(BigDecimal creditLimit) {
    this.creditLimit = creditLimit;
  }
  public Creditcard() {
    this.balance = BigDecimal.ZERO;
  }

  public Creditcard(String number, String name, BigDecimal creditLimit) {
    this.number = number;
    this.name = name;
    this.creditLimit = creditLimit;
  }


  @Override
  @Transient
  public boolean isNew() {
    return true;
  }
}

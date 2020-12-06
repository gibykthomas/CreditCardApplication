package com.sapient.creditcardApplication.domain;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Table("creditcard")
public class Creditcard implements Persistable<String> {
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

  public Creditcard(String number, String name, BigDecimal balance, BigDecimal creditLimit) {
    this.number = number;
    this.name = name;
    this.balance = balance;
    this.creditLimit = creditLimit;
  }

  @Transient
  private boolean newCard;

  @Override
  @Transient
  public boolean isNew() {
    return this.newCard || getId() == null;
  }

  public Creditcard setAsNew() {
    this.newCard = true;
    return this;
  }
}

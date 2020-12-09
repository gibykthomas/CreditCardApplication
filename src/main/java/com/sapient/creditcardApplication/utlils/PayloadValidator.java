package com.sapient.creditcardApplication.utlils;

import com.sapient.creditcardApplication.domain.Creditcard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.server.ServerWebInputException;
@Slf4j
@Component
public class PayloadValidator {
  @Autowired
  private Environment env;

  public void validate(Creditcard creditcard) {
    if (creditcard == null) {
      log.info("Body of the request is missing");
      throw new ServerWebInputException("Body of the request is missing");
    }
    if (creditcard.getNumber() == null || creditcard.getNumber().isEmpty()) {
      log.info("Creditcard number is missing in the request");
      throw new ServerWebInputException("Creditcard number is not provided");
    }
    if (creditcard.getName() == null || creditcard.getName().isEmpty()) {
      log.info(String.format("Creditcard  - %s name is missing",creditcard.getNumber().replaceAll(".(?=.{4})", "X")));
      throw new ServerWebInputException("Creditcard name is not provided");
    }
    if (creditcard.getCreditLimit() == null) {
      log.info(String.format("Creditcard  - %s limit is missing",creditcard.getNumber().replaceAll(".(?=.{4})", "X")));
      throw new ServerWebInputException("Creditcard Limit is not provided");
    }
    if (creditcard.getNumber().length() > Integer.parseInt(env.getProperty("config.MAX_CARDNUMBER_LENGTH"))) {
      log.info(String.format("Creditcard  - %s number has more than allowed characters",creditcard.getNumber().replaceAll(".(?=.{4})", "X")));
      throw new ServerWebInputException(String.format("Creditcard number is more than %d chars",Integer.parseInt(env.getProperty("config.MAX_CARDNUMBER_LENGTH"))));
    }
    validateLuhn10(creditcard);
  }

  private void validateLuhn10(Creditcard creditcard) {
    //Validating Luhn10 Algorithm
    int sum = 0;
    int digit = 0;
    for (int i=creditcard.getNumber().length() -1;i >= 0; i--) {
      try {
        digit = Integer.parseInt(String.valueOf(creditcard.getNumber().charAt(i)));
      } catch (Exception e) {
        log.info(String.format("Creditcard  - %s number has non digit character - %s",creditcard.getNumber().replaceAll(".(?=.{4})", "X"),creditcard.getNumber().charAt(i)));
        throw new ServerWebInputException("Creditcard number has non digit characters");
      }
      if (i%2 == 0) digit = digit*2;
      if (digit > 9) digit -= 9;
      sum += digit;
    }
    if (sum == 0 || sum%10 !=0) {
      log.info(String.format("Creditcard  - %s number is not satisfying Luhns Algorithm",creditcard.getNumber().replaceAll(".(?=.{4})", "X")));
      throw new ServerWebInputException("Creditcard number is not valid");
    }

  }
}


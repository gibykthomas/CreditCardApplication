package com.sapient.creditcardApplication.utlils;

import com.sapient.creditcardApplication.domain.Creditcard;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PayloadValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return Creditcard.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    Creditcard credit = (Creditcard) target;
    validateCardNumber(errors,credit.getNumber());
  }

  private void validateCardNumber(Errors errors, String number) {
    int numberLength = number.length();
    System.out.println(number);

    if (numberLength < 16 || numberLength > 19) {
      errors.rejectValue("number", "400","length of creditcard number has to be between 16 and 19");
    }
    int sum = 0;
    int digit = 0;
    for (int i=number.length() -1;i >= 0; i--) {
      try {
        digit = Integer.parseInt(String.valueOf(number.charAt(i)));
      } catch (Exception e) {
        errors.rejectValue("number", "400","creditcard number has non digit characters");
        break;
      }
      if (i%2 !=0) {
        sum += digit;
      } else {
        digit = digit*2;
        if (digit > 9) {
          sum += digit%10 + 1;
        } else {
          sum += digit;
        }
      }
    }
    if (sum == 0 || sum%10 !=0) {
      errors.rejectValue("number", "400","creditcard number is not valid");
   }

  }
}


package com.sapient.creditcardApplication.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ErrorResponse {
  private String code;
  private String errorMessage;

}

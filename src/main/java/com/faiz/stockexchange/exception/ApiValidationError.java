package com.faiz.stockexchange.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EqualsAndHashCode
public class ApiValidationError {

  private String object;
  private String field;
  private Object rejectedValue;
  private String message;

  public ApiValidationError() {
  }

  public ApiValidationError(String object, String field, Object rejectedValue,
      String message) {
    this.object = object;
    this.field = field;
    this.rejectedValue = rejectedValue;
    this.message = message;
  }

}

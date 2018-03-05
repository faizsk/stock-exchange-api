package com.faiz.stockexchange.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Setter
@Getter
@ToString
public class CustomException extends RuntimeException {

  private HttpStatus httpStatus;

  public CustomException(String message, HttpStatus httpStatus) {
    super(message);
    this.httpStatus = httpStatus;
  }

  public CustomException(String message, Throwable cause) {
    super(message, cause);
  }

  public CustomException(String message) {
    super(message);
  }

  public CustomException(Throwable cause) {
    super(cause);
  }

}

package com.faiz.stockexchange.exception;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(CustomException.class)
  protected ResponseEntity<Object> handleIndexNotFound(
      CustomException ex) {
    ApiError apiError = new ApiError(ex.getHttpStatus());
    apiError.setMessage(ex.getMessage());
    return buildResponseEntity(apiError);
  }

  private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
    return new ResponseEntity<>(apiError, apiError.getStatus());
  }

  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    List<ApiValidationError> errors = new ArrayList<>();

    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      ApiValidationError apiValidationError = new ApiValidationError();
      apiValidationError.setField(error.getField());
      apiValidationError.setMessage(error.getDefaultMessage());
      apiValidationError.setObject(error.getObjectName());
      apiValidationError.setRejectedValue(error.getRejectedValue());
      errors.add(apiValidationError);
    }

    for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
      ApiValidationError apiValidationError = new ApiValidationError();
      apiValidationError.setMessage(error.getDefaultMessage());
      apiValidationError.setObject(error.getObjectName());
      errors.add(apiValidationError);
    }

    ApiError apiError =
        new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
    apiError.setMessage("API Validation Failed");
    return buildResponseEntity(apiError);
  }

}
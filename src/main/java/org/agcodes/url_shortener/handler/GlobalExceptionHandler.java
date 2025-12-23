package org.agcodes.url_shortener.handler;

import org.agcodes.url_shortener.dto.ErrorResponse;
import org.agcodes.url_shortener.exception.UrlExpiredException;
import org.agcodes.url_shortener.exception.UrlNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(UrlNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFound(UrlNotFoundException ex) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(new ErrorResponse("URL_NOT_FOUND", ex.getMessage()));
  }

  @ExceptionHandler(UrlExpiredException.class)
  public ResponseEntity<ErrorResponse> handleExpired(UrlExpiredException ex) {
    return ResponseEntity
        .status(HttpStatus.GONE)
        .body(new ErrorResponse("URL_EXPIRED", ex.getMessage()));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException ex) {
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse("BAD_REQUEST", ex.getMessage()));
  }
}

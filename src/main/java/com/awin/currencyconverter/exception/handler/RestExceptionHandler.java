package com.awin.currencyconverter.exception.handler;

import com.awin.currencyconverter.exception.ConverterApplicationException;
import com.awin.currencyconverter.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Collections.emptyMap;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    @NonNull
    protected ResponseEntity<Object> handleBindException(BindException ex,
                                                         HttpHeaders headers,
                                                         HttpStatus status,
                                                         WebRequest request) {
        List<String> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + " -> " + fieldError.getDefaultMessage())
                .collect(Collectors.toList());
        var validationException = new ConverterApplicationException("Validation Exception");
        return handleExceptionInternal(validationException, fieldErrors, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex,
                                                             @Nullable Object body,
                                                             HttpHeaders headers,
                                                             HttpStatus status,
                                                             WebRequest request) {
        Map<String, Object> errors = body == null ? emptyMap() : Map.of("info", body);
        log.error("{}", ex.getMessage(), ex);
        return ResponseEntity
                .status(status)
                .headers(headers)
                .body(ErrorResponse.builder()
                        .status(status.value())
                        .message(ex.getMessage())
                        .details(errors)
                        .timestamp(Instant.now())
                        .build());
    }

    @ExceptionHandler(ConverterApplicationException.class)
    protected ResponseEntity<ErrorResponse> handleConverterApplicationException(ConverterApplicationException ex) {
        log.error("{}, details: {}", ex.getMessage(), ex.getDetails(), ex);
        return ResponseEntity.status(ex.getStatus())
                .body(ErrorResponse.builder()
                        .errorId(ex.getErrorId())
                        .status(ex.getStatus())
                        .message(ex.getMessage())
                        .details(ex.getDetails())
                        .build());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error("{}", ex.getMessage(), ex);
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder()
                        .status(INTERNAL_SERVER_ERROR.value())
                        .message(ex.getMessage())
                        .details(emptyMap())
                        .timestamp(Instant.now())
                        .build());
    }
}

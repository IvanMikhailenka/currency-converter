package com.awin.currencyconverter.exception.handler;

import com.awin.currencyconverter.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {


    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex,
                                                             @Nullable Object body,
                                                             HttpHeaders headers,
                                                             HttpStatus status,
                                                             WebRequest request){
        //todo: log request/response
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        }
        Map<String, Object> errors = body == null ? Collections.emptyMap() : Map.of("info", body);
        return buildErrorResponse(ex, status, headers, errors);
    }


    private ResponseEntity<Object> buildErrorResponse(Exception e,
                                                             HttpStatus httpStatus,
                                                             HttpHeaders headers,
                                                             Map<String, Object> errors) {
        log.error("{}", e.getMessage(), e);
        return ResponseEntity
                .status(httpStatus)
                .headers(headers)
                .body(ErrorResponse.builder()
                        .errorCode(httpStatus.value())
                        .message(e.getMessage())
                        .details(errors)
                        .timestamp(Instant.now())
                        .build());
    }
}

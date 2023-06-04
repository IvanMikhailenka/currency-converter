package com.awin.currencyconverter.exception.handler;

import com.awin.currencyconverter.exception.ConverterApplicationException;
import com.awin.currencyconverter.exception.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ValidationException;
import java.time.Instant;
import java.util.Collections;

import static com.awin.currencyconverter.exception.handler.RestExceptionHandlerTest.Fixture.createErrorResponse;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RestExceptionHandlerTest {

    @Mock
    private WebRequest webRequest;

    @InjectMocks
    private RestExceptionHandler restExceptionHandler;

    @Test
    public void should_HandleBindException_And_ReturnErrorResponse() {
        //given
        var bindingResult = mock(BindingResult.class);
        var bindException = new BindException(bindingResult);
        var status = HttpStatus.BAD_REQUEST;
        var fieldError = new FieldError("error", "field", "incorrect field");

        //when
        when(bindingResult.getFieldErrors()).thenReturn(singletonList(fieldError));

        ResponseEntity<Object> responseEntity = restExceptionHandler.handleBindException(
                bindException, new HttpHeaders(), status, webRequest);

        //then
        assertEquals(status, responseEntity.getStatusCode());
        var errorResponse = (ErrorResponse) responseEntity.getBody();
        assertEquals("Validation Exception", errorResponse.getMessage());
    }

    @Test
    public void should_HandleExceptionInternal_And_ReturnErrorResponse() {
        //given
        var exception = new Exception("Internal Server Error");
        var status = HttpStatus.INTERNAL_SERVER_ERROR;
        var expectedErrorResponse = createErrorResponse(exception.getMessage(), status);

        //when
        ResponseEntity<Object> responseEntity = restExceptionHandler.handleExceptionInternal(
                exception, null, new HttpHeaders(), status, webRequest);

        //then
        assertEquals(status, responseEntity.getStatusCode());
        var errorResponse = (ErrorResponse) responseEntity.getBody();
        assertEquals(expectedErrorResponse.getMessage(), errorResponse.getMessage());
    }

    @Test
    public void should_HandleConverterApplicationException_And_ReturnErrorResponse() {
        //given
        var exception = new ConverterApplicationException("Converter Application Error")
                .setStatus(HttpStatus.NOT_FOUND.value());
        var expectedErrorResponse = createErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND);

        //when
        var responseEntity = restExceptionHandler.handleConverterApplicationException(exception);

        //then
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        var errorResponse = (ErrorResponse) responseEntity.getBody();
        assertEquals(expectedErrorResponse.getMessage(), errorResponse.getMessage());
    }

    @Test
    public void should_HandleValidationException_WithConverterApplicationException_And_ReturnErrorResponse() {
        //given
        var converterException = new ConverterApplicationException("Validation Exception")
                .setStatus(HttpStatus.BAD_REQUEST.value());
        var validationException = new ValidationException("Validation Error", converterException);
        var expectedErrorResponse = createErrorResponse(converterException.getMessage(), HttpStatus.BAD_REQUEST);

        //when
        ResponseEntity<ErrorResponse> responseEntity = restExceptionHandler.handleValidationException(validationException);

        //then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        assertEquals(expectedErrorResponse.getMessage(), responseEntity.getBody().getMessage());
    }

    @Test
    public void should_HandleValidationException_WithGeneralException_And_ReturnErrorResponse() {
        //given
        var generalException = new Exception("General Error");
        var validationException = new ValidationException("Validation Error", generalException);
        var expectedErrorResponse = createErrorResponse(generalException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        //when
        ResponseEntity<ErrorResponse> responseEntity = restExceptionHandler.handleValidationException(validationException);

        //then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        assertEquals(expectedErrorResponse.getMessage(), responseEntity.getBody().getMessage());
    }

    @Test
    public void should_HandleException_And_ReturnErrorResponse() {
        //given
        var exception = new Exception("Internal Server Error");
        var errorResponse = createErrorResponse(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        //when
        ResponseEntity<ErrorResponse> responseEntity = restExceptionHandler.handleException(exception);

        //then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        assertEquals(errorResponse.getMessage(), responseEntity.getBody().getMessage());
    }

    static class Fixture {

        static ErrorResponse createErrorResponse(String message, HttpStatus status) {
            return ErrorResponse.builder()
                    .status(status.value())
                    .message(message)
                    .details(Collections.emptyMap())
                    .timestamp(Instant.now())
                    .build();
        }
    }
}
package com.awin.currencyconverter.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class CurrencyConverterApplicationException extends RuntimeException {

    private Integer status;
    private String errorId;
    private Map<String, Object> details;

    public CurrencyConverterApplicationException(String message) {
        super(message);
    }

    public CurrencyConverterApplicationException withStatus(Integer status) {
        this.status = status;
        return this;
    }

    public CurrencyConverterApplicationException withErrorId(String errorId) {
        this.errorId = errorId;
        return this;
    }

    public CurrencyConverterApplicationException withDetails(Map<String, Object> details) {
        this.details = details;
        return this;
    }

}

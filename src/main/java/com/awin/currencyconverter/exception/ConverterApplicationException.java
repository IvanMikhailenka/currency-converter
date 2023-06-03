package com.awin.currencyconverter.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
//todo: use it somewhere
public class ConverterApplicationException extends RuntimeException {

    private String errorId;
    private Integer status;
    private Map<String, Object> details;

    public ConverterApplicationException(String message) {
        super(message);
    }

    public ConverterApplicationException withStatus(Integer status) {
        this.status = status;
        return this;
    }

    public ConverterApplicationException withErrorId(String errorId) {
        this.errorId = errorId;
        return this;
    }

    public ConverterApplicationException withDetails(Map<String, Object> details) {
        this.details = details;
        return this;
    }

}

package com.awin.currencyconverter.exception;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ConverterApplicationException extends RuntimeException {

    private String errorId;
    private Integer status;
    private Map<String, Object> details;

    public ConverterApplicationException(String message) {
        super(message);
    }

}

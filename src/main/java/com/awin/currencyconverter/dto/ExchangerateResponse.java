package com.awin.currencyconverter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExchangerateResponse {

    private String base;
    private Map<String, Double> rates;
    private Calendar date;//todo: do we need it?
    private Boolean success;//todo: do we need it?

}

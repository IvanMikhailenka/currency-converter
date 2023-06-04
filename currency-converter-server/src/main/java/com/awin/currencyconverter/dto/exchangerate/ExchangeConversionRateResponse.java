package com.awin.currencyconverter.dto.exchangerate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeConversionRateResponse {

    private String base;
    private Map<String, BigDecimal> rates;

}

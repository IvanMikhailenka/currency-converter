package com.awin.currencyconverter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Calendar;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExchangerateResponse {

    private BigDecimal result;
    private ExchangerateRateInfo info;//todo: do we need it?
    private Calendar date;//todo: do we need it?
    private Boolean success;//todo: do we need it?

}

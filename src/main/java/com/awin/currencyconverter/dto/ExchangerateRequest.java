package com.awin.currencyconverter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExchangerateRequest {

    private String from;
    private String to;
    private Double amount;

}
package com.awin.currencyconverter.contract;

import com.awin.currencyconverter.validator.ValidCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConversionRequest {

    @NotNull
    @ValidCode
    private String source;
    @NotNull
    @ValidCode
    private String target;
    @NotNull
    @Positive
    private BigDecimal amount;

}

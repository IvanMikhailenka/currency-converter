package com.awin.currencyconverter.service;

import com.awin.currencyconverter.client.ExchangerateClient;
import com.awin.currencyconverter.contract.ConversionRequest;
import com.awin.currencyconverter.contract.CurrencyConvertResponse;
import com.awin.currencyconverter.dto.ExchangerateConvertRequest;
import com.awin.currencyconverter.dto.ExchangerateResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static java.math.BigDecimal.valueOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyExchangeRateServiceTest {

    @Mock
    private ExchangerateClient exchangerateClient;
    @InjectMocks
    CurrencyExchangeRateService service;

    @Test
    void Should_CalculateConversionResult_When_SourceTargetAmountProvided() {
        //given
        var source = "EUR";
        var target = "USD";
        BigDecimal amount = valueOf(10d);
        ConversionRequest conversionRequest = ConversionRequest.builder()
                .source(source)
                .target(target)
                .amount(amount)
                .build();
        var exchangerateRequest = ExchangerateConvertRequest.builder()
                .from(source)
                .to(target)
                .amount(amount)
                .build();
        var exchangerateResponse = ExchangerateResponse.builder()
                .result(valueOf(123d))
                .build();
        var expectedResult = new CurrencyConvertResponse(valueOf(123d));
        //when
        when(exchangerateClient.getConversionRate(exchangerateRequest)).thenReturn(exchangerateResponse);

        var result = service.convert(conversionRequest);
        //then
        assertEquals(result, expectedResult);
    }
}
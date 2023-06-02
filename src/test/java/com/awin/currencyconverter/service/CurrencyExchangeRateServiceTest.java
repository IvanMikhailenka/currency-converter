package com.awin.currencyconverter.service;

import com.awin.currencyconverter.client.ExchangerateClient;
import com.awin.currencyconverter.dto.ExchangerateRequest;
import com.awin.currencyconverter.dto.ExchangerateResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

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
        var exchangerateRequest = ExchangerateRequest.builder()
                .base(source)
                .target(target)
                .build();
        var exchangerateResponse = ExchangerateResponse.builder()
                .rates(Map.of("USD", 123d))
                .build();
        var expectedResult = 1230d;
        //when
        when(exchangerateClient.getConversionRate(exchangerateRequest)).thenReturn(exchangerateResponse);

        double result = service.convert(source, target, 10);
        //then
        assertEquals(result, expectedResult);
    }
}
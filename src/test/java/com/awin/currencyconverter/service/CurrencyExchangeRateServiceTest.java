package com.awin.currencyconverter.service;

import com.awin.currencyconverter.cache.ConversionRatesCache;
import com.awin.currencyconverter.contract.ConversionRequest;
import com.awin.currencyconverter.contract.CurrencyConvertResponse;
import com.awin.currencyconverter.dto.exchangerate.ExchangeConversionRateResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static com.awin.currencyconverter.service.CurrencyExchangeRateServiceTest.Fixtures.getConversionRateResponse;
import static com.awin.currencyconverter.service.CurrencyExchangeRateServiceTest.Fixtures.getConversionRequest;
import static com.awin.currencyconverter.service.CurrencyExchangeRateServiceTest.Fixtures.getCurrencyConvertResponse;
import static java.math.BigDecimal.valueOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyExchangeRateServiceTest {

    @Mock
    private ConversionRatesCache conversionRatesCache;
    @InjectMocks
    CurrencyExchangeRateService service;

    @Test
    void Should_CalculateConversionResult_When_SourceTargetAmountProvided() {
        //given
        var conversionRequest = getConversionRequest();
        var conversionRateResponse = getConversionRateResponse();
        var expectedResult = getCurrencyConvertResponse();
        //when
        when(conversionRatesCache.getConversionRates(conversionRequest.getSource())).thenReturn(conversionRateResponse);

        var result = service.convert(conversionRequest);
        //then
        assertEquals(expectedResult, result);
    }

    static class Fixtures {

        static CurrencyConvertResponse getCurrencyConvertResponse() {
            return CurrencyConvertResponse.builder()
                    .value(valueOf(1230))
                    .build();
        }

        static ConversionRequest getConversionRequest() {
            return ConversionRequest.builder()
                    .source("EUR")
                    .target("USD")
                    .amount(valueOf(10))
                    .build();
        }

        static ExchangeConversionRateResponse getConversionRateResponse() {
            return ExchangeConversionRateResponse.builder()
                    .base("EUR")
                    .rates(Map.of("USD", valueOf(123)))
                    .build();
        }

    }
}
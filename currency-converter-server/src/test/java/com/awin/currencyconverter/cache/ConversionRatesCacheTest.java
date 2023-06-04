package com.awin.currencyconverter.cache;

import com.awin.currencyconverter.client.ExchangerateClient;
import com.awin.currencyconverter.dto.exchangerate.ExchangeConversionRateRequest;
import com.awin.currencyconverter.dto.exchangerate.ExchangeConversionRateResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

import static com.awin.currencyconverter.cache.ConversionRatesCacheTest.Fixtures.getExchangeConversionRateResponse;
import static java.util.Collections.emptyMap;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ConversionRatesCacheTest {

    @MockBean
    private ExchangerateClient exchangerateClient;

    @Autowired
    private ConversionRatesCache cache;

    @Test
    public void should_callExchangerateClientOnlyOnce_When_callingGetConversionRates() {
        //given
        var currency = "EUR";
        var exchangeCurrencyResponse = getExchangeConversionRateResponse();
        var request = new ExchangeConversionRateRequest(currency);
        //when
        when(exchangerateClient.getLatestConversionRates(any())).thenReturn(exchangeCurrencyResponse);

        cache.getConversionRates(currency);
        cache.getConversionRates(currency);
        cache.getConversionRates(currency);
        //then
        verify(exchangerateClient, times(1)).getLatestConversionRates(request);
    }

    @Test
    public void should_evictCash_When_callingEvictCurrencyCodesCash() {
        //given
        var currency = "EUR";
        var exchangeCurrencyResponse = getExchangeConversionRateResponse();
        var request = new ExchangeConversionRateRequest(currency);
        //when
        when(exchangerateClient.getLatestConversionRates(any())).thenReturn(exchangeCurrencyResponse);

        cache.getConversionRates(currency);
        cache.getConversionRates(currency);
        cache.evictConversionRates();
        cache.getConversionRates(currency);
        //then
        verify(exchangerateClient, times(2)).getLatestConversionRates(request);
    }

    static class Fixtures {

        static ExchangeConversionRateResponse getExchangeConversionRateResponse() {
            return ExchangeConversionRateResponse.builder()
                    .base("EUR")
                    .rates(emptyMap())
                    .build();
        }

    }

}
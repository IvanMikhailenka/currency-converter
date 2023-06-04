package com.awin.currencyconverter.cache;

import com.awin.currencyconverter.client.ExchangerateClient;
import com.awin.currencyconverter.dto.exchangerate.ExchangeCurrencyResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

import static com.awin.currencyconverter.cache.CurrencyCodeCacheTest.Fixtures.getExchangeCurrencyResponse;
import static java.util.Collections.emptyMap;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CurrencyCodeCacheTest {

    @MockBean
    private ExchangerateClient exchangerateClient;

    @Autowired
    private CurrencyCodeCache cache;

    @Test
    public void should_callExchangerateClientOnlyOnce_When_callingGetCurrencyCodes() {
        //given
        var exchangeCurrencyResponse = getExchangeCurrencyResponse();
        //when
        when(exchangerateClient.getCurrencyCodes()).thenReturn(exchangeCurrencyResponse);
        cache.getCurrencyCodes();
        cache.getCurrencyCodes();
        cache.getCurrencyCodes();
        //then
        verify(exchangerateClient, times(1)).getCurrencyCodes();
    }

    @Test
    public void should_evictCash_When_callingEvictCurrencyCodesCash() {
        //given
        var exchangeCurrencyResponse = getExchangeCurrencyResponse();
        //when
        when(exchangerateClient.getCurrencyCodes()).thenReturn(exchangeCurrencyResponse);
        cache.getCurrencyCodes();
        cache.getCurrencyCodes();
        cache.evictCurrencyCodesCash();
        cache.getCurrencyCodes();
        //then
        verify(exchangerateClient, times(2)).getCurrencyCodes();
    }

    static class Fixtures {

        static ExchangeCurrencyResponse getExchangeCurrencyResponse() {
            return ExchangeCurrencyResponse.builder()
                    .symbols(emptyMap())
                    .build();
        }

    }

}
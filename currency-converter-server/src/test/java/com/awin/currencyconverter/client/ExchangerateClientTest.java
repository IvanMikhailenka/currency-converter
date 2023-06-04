package com.awin.currencyconverter.client;

import com.awin.currencyconverter.configuration.properties.DestinationProperties;
import com.awin.currencyconverter.dto.exchangerate.ExchangeConversionRateRequest;
import com.awin.currencyconverter.dto.exchangerate.ExchangeConversionRateResponse;
import com.awin.currencyconverter.dto.exchangerate.ExchangeCurrencyResponse;
import com.awin.currencyconverter.exception.ConverterApplicationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExchangerateClientTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private DestinationProperties destinationProperties;

    @InjectMocks
    private ExchangerateClient exchangerateClient;

    @Test
    public void should_ReturnLatestConversionRates_When_ValidRequest() {
        //given
        var request = new ExchangeConversionRateRequest("USD");
        var expectedUrl = "http://testUrl/latest?base=USD";
        var expectedResponse = new ExchangeConversionRateResponse();

        //when
        when(destinationProperties.getExchangerateLatestRatesUrl()).thenReturn("http://testUrl/latest");
        when(restTemplate.getForEntity(expectedUrl, ExchangeConversionRateResponse.class))
                .thenReturn(ResponseEntity.ok(expectedResponse));

        var actualResponse = exchangerateClient.getLatestConversionRates(request);

        //then
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void should_ThrowConverterApplicationException_When_LatestConversionRatesNotFound() {
        //given
        var request = new ExchangeConversionRateRequest("USD");
        var expectedUrl = "http://testUrl/latest?base=USD";

        //when
        when(destinationProperties.getExchangerateLatestRatesUrl()).thenReturn("http://testUrl/latest");
        when(restTemplate.getForEntity(expectedUrl, ExchangeConversionRateResponse.class))
                .thenReturn(ResponseEntity.notFound().build());

        //then
        assertThrows(ConverterApplicationException.class, () -> exchangerateClient.getLatestConversionRates(request));
    }

    @Test
    public void should_ReturnCurrencyCodes_When_ValidRequest() {
        //given
        var expectedUrl = "http://testUrl/codes";
        var expectedResponse = new ExchangeCurrencyResponse();

        //when
        when(destinationProperties.getExchangerateCurrencyCodesUrl()).thenReturn("http://testUrl/codes");
        when(restTemplate.getForEntity(expectedUrl, ExchangeCurrencyResponse.class))
                .thenReturn(ResponseEntity.ok(expectedResponse));

        var actualResponse = exchangerateClient.getCurrencyCodes();

        //then
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void should_ThrowConverterApplicationException_When_CurrencyCodesNotFound() {
        //given
        var expectedUrl = "http://testUrl/codes";

        //when
        when(destinationProperties.getExchangerateCurrencyCodesUrl()).thenReturn("http://testUrl/codes");
        when(restTemplate.getForEntity(expectedUrl, ExchangeCurrencyResponse.class))
                .thenReturn(ResponseEntity.notFound().build());

        //then
        assertThrows(ConverterApplicationException.class, () -> exchangerateClient.getCurrencyCodes());
    }
}
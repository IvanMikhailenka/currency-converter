package com.awin.currencyconverter.client;

import com.awin.currencyconverter.dto.ExchangerateRequest;
import com.awin.currencyconverter.dto.ExchangerateResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExchangerateClientTest {

    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private ExchangerateClient client;

    @Test
    public void Should_CallForExchangerateResponse_When_ExchangerateRequestProvided() {
        // given
        var request = new ExchangerateRequest("EUR", "USD", 10d);
        var apiUrl = "https://api.exchangerate.host/convert?from=EUR&to=USD&amount=10.0";
        var expectedResponse = new ExchangerateResponse();
        var responseEntity = new ResponseEntity<>(expectedResponse, HttpStatus.OK);

        // when
        when(restTemplate.getForEntity(apiUrl, ExchangerateResponse.class)).thenReturn(responseEntity);
        var actualResponse = client.getConversionRate(request);

        // then
        assertEquals(expectedResponse, actualResponse);
        verify(restTemplate).getForEntity(apiUrl, ExchangerateResponse.class);
    }
}
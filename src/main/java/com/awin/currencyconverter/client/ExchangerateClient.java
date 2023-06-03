package com.awin.currencyconverter.client;

import com.awin.currencyconverter.dto.exchangerate.ExchangeConversionRateRequest;
import com.awin.currencyconverter.dto.exchangerate.ExchangeConversionRateResponse;
import com.awin.currencyconverter.dto.exchangerate.ExchangeCurrencyResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangerateClient {

    //todo: move to .yaml -> param class
    public static final String API_EXCHANGERATE_LATEST_RATES = "https://api.exchangerate.host/latest";
    public static final String API_EXCHANGERATE_SYMBOLS = "https://api.exchangerate.host/symbols";
    private final RestTemplate restTemplate;

    public ExchangeConversionRateResponse getLatestConversionRates(ExchangeConversionRateRequest request) {
        String url = fromUriString(API_EXCHANGERATE_LATEST_RATES)
                .queryParam("base", request.getBase())
                .toUriString();
        return restTemplate.getForEntity(url, ExchangeConversionRateResponse.class).getBody();
    }

    public ExchangeCurrencyResponse getCurrencyCodes() {
        return restTemplate.getForEntity(API_EXCHANGERATE_SYMBOLS, ExchangeCurrencyResponse.class).getBody();
    }

}

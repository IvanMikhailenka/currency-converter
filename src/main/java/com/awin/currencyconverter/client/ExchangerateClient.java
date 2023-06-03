package com.awin.currencyconverter.client;

import com.awin.currencyconverter.dto.ExchangerateConvertRequest;
import com.awin.currencyconverter.dto.ExchangerateCurrencyResponse;
import com.awin.currencyconverter.dto.ExchangerateResponse;
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
    public static final String API_EXCHANGERATE_LATEST = "https://api.exchangerate.host/convert";
    public static final String API_EXCHANGERATE_SYMBOLS = "https://api.exchangerate.host/symbols";
    private final RestTemplate restTemplate;

    public ExchangerateResponse getConversionRate(ExchangerateConvertRequest request) {
        String url = buildConvertUrl(request);
        return restTemplate.getForEntity(url, ExchangerateResponse.class).getBody();
    }

    public ExchangerateCurrencyResponse getCurrencyCodes() {
        return restTemplate.getForEntity(API_EXCHANGERATE_SYMBOLS, ExchangerateCurrencyResponse.class)
                .getBody();
    }

    //todo: move it to mapstruct
    private static String buildConvertUrl(ExchangerateConvertRequest request) {
        return fromUriString(API_EXCHANGERATE_LATEST)
                .queryParam("from", request.getFrom())
                .queryParam("to", request.getTo())
                .queryParam("amount", request.getAmount())
                .toUriString();
    }

}

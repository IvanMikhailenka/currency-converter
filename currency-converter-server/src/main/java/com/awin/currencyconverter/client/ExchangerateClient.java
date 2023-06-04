package com.awin.currencyconverter.client;

import com.awin.currencyconverter.configuration.properties.DestinationProperties;
import com.awin.currencyconverter.dto.exchangerate.ExchangeConversionRateRequest;
import com.awin.currencyconverter.dto.exchangerate.ExchangeConversionRateResponse;
import com.awin.currencyconverter.dto.exchangerate.ExchangeCurrencyResponse;
import com.awin.currencyconverter.exception.ConverterApplicationException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangerateClient {

    private final RestTemplate restTemplate;
    private final DestinationProperties destinationProperties;

    @NonNull
    public ExchangeConversionRateResponse getLatestConversionRates(@NonNull ExchangeConversionRateRequest request) {
        String url = fromUriString(destinationProperties.getExchangerateLatestRatesUrl())
                .queryParam("base", request.getBase())
                .toUriString();
        return Optional.of(restTemplate.getForEntity(url, ExchangeConversionRateResponse.class))
                .map(HttpEntity::getBody)
                .orElseThrow(() -> new ConverterApplicationException("Unable to fetch conversion rates"));
    }

    @NonNull
    public ExchangeCurrencyResponse getCurrencyCodes() {
        String url = destinationProperties.getExchangerateCurrencyCodesUrl();
        return Optional.of(restTemplate.getForEntity(url, ExchangeCurrencyResponse.class))
                .map(HttpEntity::getBody)
                .orElseThrow(() -> new ConverterApplicationException("Unable to fetch currency codes"));
    }

}

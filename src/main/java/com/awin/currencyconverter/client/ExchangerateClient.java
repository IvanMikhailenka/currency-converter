package com.awin.currencyconverter.client;

import com.awin.currencyconverter.dto.ExchangerateRequest;
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
    public static final String HTTPS_API_EXCHANGERATE_HOST_LATEST = "https://api.exchangerate.host/convert";
    private final RestTemplate restTemplate;

    public ExchangerateResponse getConversionRate(ExchangerateRequest request) {
        String url = buildConvertUrl(request);
        return restTemplate.getForEntity(url, ExchangerateResponse.class).getBody();
    }

    //todo: move it to mapstruct
    private static String buildConvertUrl(ExchangerateRequest request) {
        return fromUriString(HTTPS_API_EXCHANGERATE_HOST_LATEST)
                .queryParam("from", request.getFrom())
                .queryParam("to", request.getTo())
                .queryParam("amount", request.getAmount())
                .toUriString();
    }

}

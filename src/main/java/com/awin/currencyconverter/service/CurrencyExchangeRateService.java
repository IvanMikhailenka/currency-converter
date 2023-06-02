package com.awin.currencyconverter.service;

import com.awin.currencyconverter.client.ExchangerateClient;
import com.awin.currencyconverter.dto.ExchangerateRequest;
import com.awin.currencyconverter.dto.ExchangerateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrencyExchangeRateService implements CurrencyService {

    private final ExchangerateClient exchangerateClient;

    @Override
    public double convert(String source, String target, double amount) {
        var exchangerateRequest = ExchangerateRequest.builder()
                .base(source)
                .target(target)
                .build();
        ExchangerateResponse conversionRate = exchangerateClient.getConversionRate(exchangerateRequest);
        Double rate = conversionRate.getRates().get(target);
        return amount * rate;
    }

}

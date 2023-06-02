package com.awin.currencyconverter.service;

import com.awin.currencyconverter.client.ExchangerateClient;
import com.awin.currencyconverter.contract.CurrencyConvertResponse;
import com.awin.currencyconverter.dto.ExchangerateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrencyExchangeRateService implements CurrencyService {

    private final ExchangerateClient exchangerateClient;

    @Override
    public CurrencyConvertResponse convert(String source, String target, double amount) {
        var exchangerateRequest = ExchangerateRequest.builder()
                .from(source)
                .to(target)
                .amount(amount)
                .build();
        var conversionRate = exchangerateClient.getConversionRate(exchangerateRequest);
        return CurrencyConvertResponse.builder()
                .value(conversionRate.getResult())
                .build();
    }

}

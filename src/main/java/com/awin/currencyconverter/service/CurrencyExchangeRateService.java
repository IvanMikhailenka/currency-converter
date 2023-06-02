package com.awin.currencyconverter.service;

import com.awin.currencyconverter.client.ExchangerateClient;
import com.awin.currencyconverter.contract.ConversionRequest;
import com.awin.currencyconverter.contract.CurrencyConvertResponse;
import com.awin.currencyconverter.mapper.CurrencyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrencyExchangeRateService implements CurrencyService {

    private final ExchangerateClient exchangerateClient;
    private final CurrencyMapper currencyMapper;

    @Override
    public CurrencyConvertResponse convert(ConversionRequest conversionRequest) {
        var exchangerateRequest = currencyMapper.toExchangerateRequest(conversionRequest);
        var conversionRate = exchangerateClient.getConversionRate(exchangerateRequest);
        return currencyMapper.toCurrencyConvertResponse(conversionRate);
    }

}

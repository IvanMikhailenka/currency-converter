package com.awin.currencyconverter.service;

import com.awin.currencyconverter.cache.ConversionRatesCache;
import com.awin.currencyconverter.contract.ConversionRequest;
import com.awin.currencyconverter.contract.CurrencyConvertResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CurrencyExchangeRateService implements CurrencyService {

    private final ConversionRatesCache conversionRatesCache;

    @Override
    @NonNull
    public CurrencyConvertResponse convert(@NonNull ConversionRequest request) {
        var conversionRates = conversionRatesCache.getConversionRates(request.getSource());
        BigDecimal conversionRate = conversionRates.getRates().get(request.getTarget());
        BigDecimal resultValue = request.getAmount().multiply(conversionRate);
        return new CurrencyConvertResponse(resultValue);
    }

}

package com.awin.currencyconverter.cache;

import com.awin.currencyconverter.client.ExchangerateClient;
import com.awin.currencyconverter.dto.exchangerate.ExchangeConversionRateRequest;
import com.awin.currencyconverter.dto.exchangerate.ExchangeConversionRateResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConversionRatesCache {

    private final ExchangerateClient exchangerateClient;

    @NonNull
    @Cacheable("conversionRates")
    public ExchangeConversionRateResponse getConversionRates(@NonNull String baseCurrency) {
        var request = new ExchangeConversionRateRequest(baseCurrency);
        return exchangerateClient.getLatestConversionRates(request);
    }


    @Scheduled(cron = "0 0 0 * * ?") // Runs at midnight (00:00) every day
    @CacheEvict(value = "conversionRates", allEntries = true)
    public void evictConversionRates() {
    }

}

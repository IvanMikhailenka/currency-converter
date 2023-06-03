package com.awin.currencyconverter.cache;

import com.awin.currencyconverter.client.ExchangerateClient;
import com.awin.currencyconverter.dto.exchangerate.ExchangeCurrencyResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyCodeCache {

    private final ExchangerateClient exchangerateClient;

    @NonNull
    @Cacheable("currencyCodes")
    public Set<String> getCurrencyCodes() {
        ExchangeCurrencyResponse currencyResponse = exchangerateClient.getCurrencyCodes();
        return currencyResponse.getSymbols().keySet();
    }


    @Scheduled(cron = "0 0 0 * * ?") // Runs at midnight (00:00) every day
    @CacheEvict(value = "currencyCodes", allEntries = true)
    public void evictCurrencyCodesCash() {}


}

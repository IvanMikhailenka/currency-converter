package com.awin.currencyconverter.configuration.properties;

import com.awin.currencyconverter.exception.ConverterApplicationException;
import lombok.Data;
import lombok.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Data
@Component
@ConfigurationProperties("destinations")
public class DestinationProperties {

    public static final String ERROR_MESSAGE_FORMAT = "%s service URL is not set.";
    private ExchangerateDestination exchangerate;

    @Data
    private static class ExchangerateDestination {

        private String root;
        private String latestRates;
        private String currencyCodes;

    }

    @NonNull
    public String getExchangerateLatestRatesUrl() {
        return Optional.ofNullable(exchangerate)
                .map(ExchangerateDestination::getLatestRates)
                .orElseThrow(() -> new ConverterApplicationException(format(ERROR_MESSAGE_FORMAT, "Exchangerate"))
                        .setStatus(INTERNAL_SERVER_ERROR.value())
                        .setDetails(Map.of(ExchangerateDestination.class.getName(), "latestRates")));
    }

    @NonNull
    public String getExchangerateCurrencyCodesUrl() {
        return Optional.ofNullable(exchangerate)
                .map(ExchangerateDestination::getCurrencyCodes)
                .orElseThrow(() -> new ConverterApplicationException(format(ERROR_MESSAGE_FORMAT, "Exchangerate"))
                        .setStatus(INTERNAL_SERVER_ERROR.value())
                        .setDetails(Map.of(ExchangerateDestination.class.getName(), "currencyCodes")));
    }
}

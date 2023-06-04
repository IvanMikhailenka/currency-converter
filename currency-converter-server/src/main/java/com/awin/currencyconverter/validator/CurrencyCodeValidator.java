package com.awin.currencyconverter.validator;

import com.awin.currencyconverter.cache.CurrencyCodeCache;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;


@RequiredArgsConstructor
public class CurrencyCodeValidator implements ConstraintValidator<ValidCode, String> {

    private final CurrencyCodeCache currencyCodeCache;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Set<String> currencyCodes = currencyCodeCache.getCurrencyCodes();
        return currencyCodes.contains(value);
    }
}

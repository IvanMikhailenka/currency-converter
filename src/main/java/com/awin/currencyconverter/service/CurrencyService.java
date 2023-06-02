package com.awin.currencyconverter.service;

import com.awin.currencyconverter.contract.CurrencyConvertResponse;

public interface CurrencyService {

    CurrencyConvertResponse convert(String source, String target, double amount);

}

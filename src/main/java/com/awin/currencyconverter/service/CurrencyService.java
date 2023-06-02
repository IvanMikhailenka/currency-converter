package com.awin.currencyconverter.service;

import com.awin.currencyconverter.contract.ConversionRequest;
import com.awin.currencyconverter.contract.CurrencyConvertResponse;

public interface CurrencyService {

    CurrencyConvertResponse convert(ConversionRequest conversionRequest);

}

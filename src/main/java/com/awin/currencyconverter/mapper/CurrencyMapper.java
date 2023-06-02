package com.awin.currencyconverter.mapper;

import com.awin.currencyconverter.contract.ConversionRequest;
import com.awin.currencyconverter.contract.CurrencyConvertResponse;
import com.awin.currencyconverter.dto.ExchangerateRequest;
import com.awin.currencyconverter.dto.ExchangerateResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CurrencyMapper { //todo: rename?

    @Mapping(target = "from", source = "source")
    @Mapping(target = "to", source = "target")
    ExchangerateRequest toExchangerateRequest(ConversionRequest request);

    @Mapping(target = "value", source = "result")
    CurrencyConvertResponse toCurrencyConvertResponse(ExchangerateResponse response);

}

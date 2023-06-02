package com.awin.currencyconverter.controller;

import com.awin.currencyconverter.contract.CurrencyConvertResponse;
import com.awin.currencyconverter.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
//todo: add swagger
public class CurrencyController {

    private final CurrencyService currencyService;

    //todo: add request param object
    //todo: add validation (source, target should be valid currency; amount greater then zero)
    @GetMapping(value = "currencies/convert", produces = MediaType.APPLICATION_JSON_VALUE)
    public CurrencyConvertResponse convert(
            @RequestParam("source") String source,
            @RequestParam("target") String target,
            @RequestParam("amount") double amount) {

        return currencyService.convert(source, target, amount);
    }

}

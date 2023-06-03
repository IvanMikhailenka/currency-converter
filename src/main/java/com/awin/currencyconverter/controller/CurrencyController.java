package com.awin.currencyconverter.controller;

import com.awin.currencyconverter.contract.ConversionRequest;
import com.awin.currencyconverter.contract.CurrencyConvertResponse;
import com.awin.currencyconverter.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@Slf4j
@RestController
@RequestMapping("/currencies")
@RequiredArgsConstructor
//todo: add swagger
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping(value = "/convert")
    public CurrencyConvertResponse convert(@Valid @ModelAttribute ConversionRequest request) {
        return currencyService.convert(request);
    }

}

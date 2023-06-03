package com.awin.currencyconverter.controller;

import com.awin.currencyconverter.contract.ConversionRequest;
import com.awin.currencyconverter.contract.CurrencyConvertResponse;
import com.awin.currencyconverter.service.CurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@Slf4j
@RestController
@RequestMapping("/currencies")
@RequiredArgsConstructor
@Tag(name = "Currency API", description = "Operations for currency conversion")
public class CurrencyController {

    private final CurrencyService currencyService;

    @Operation(summary = "Converts currency")
    @ApiResponse(responseCode = "200", description = "Currency conversion successful")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @Parameters({
            @Parameter(name = "source", example = "EUR"),
            @Parameter(name = "target", example = "USD"),
            @Parameter(name = "amount", example = "100")
    })
    @GetMapping(value = "/convert")
    public CurrencyConvertResponse convert(@ParameterObject @Valid @ModelAttribute ConversionRequest request) {
        return currencyService.convert(request);
    }

}

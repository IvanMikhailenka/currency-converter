package com.awin.currencyconverter.controller;

import com.awin.currencyconverter.cache.CurrencyCodeCache;
import com.awin.currencyconverter.contract.ConversionRequest;
import com.awin.currencyconverter.contract.CurrencyConvertResponse;
import com.awin.currencyconverter.service.CurrencyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CurrencyController.class)
class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CurrencyService currencyService;
    @MockBean
    private CurrencyCodeCache currencyCodeCache;

    @BeforeEach
    public void setUp() {
        when(currencyCodeCache.getCurrencyCodes()).thenReturn(new HashSet<>(Set.of("EUR", "USD")));
    }

    static Stream<Arguments> successfulConversionTestData() {
        return Stream.of(
                Arguments.of("EUR", "USD", "100", "118.0", "{\"value\":118.0}"),
                Arguments.of("USD", "EUR", "100", "84.75", "{\"value\":84.75}")
        );
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("successfulConversionTestData")
    public void should_convert_source_to_target_with_rate_provided(String source,
                                      String target,
                                      String amount,
                                      String returnValue,
                                      String expectedResponseBody) {
        var request = ConversionRequest.builder()
                .source(source)
                .target(target)
                .amount(new BigDecimal(amount))
                .build();
        var response = new CurrencyConvertResponse(new BigDecimal(returnValue));

        when(currencyService.convert(request)).thenReturn(response);

        mockMvc.perform(get("/currencies/convert")
                        .param("source", source)
                        .param("target", target)
                        .param("amount", amount)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseBody));
    }

    static Stream<Arguments> unsuccessfulConversionTestData() {
        return Stream.of(
                Arguments.of(null, "EUR", "100", "error_source_null.json"),
                Arguments.of("USD", null, "100", "error_target_null.json"),
                Arguments.of("USD", "EUR", "-100", "error_negative_amount.json"),
                Arguments.of("USD", "EUR", "0", "error_negative_amount.json"),
                Arguments.of("AUD", "EUR", "100", "error_invalid_source_code.json"),
                Arguments.of("USD", "JPY", "100", "error_invalid_target_code.json")
        );
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("unsuccessfulConversionTestData")
    public void should_fail_to_convert_with_400_and_error_message(String source,
                                                                  String target,
                                                                  String amount,
                                                                  String errorMessageFilePath) {
        String expectedErrorMessage = loadJsonFromFile(errorMessageFilePath);

        mockMvc.perform(get("/currencies/convert")
                        .param("source", source)
                        .param("target", target)
                        .param("amount", amount)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedErrorMessage));
    }

    private String loadJsonFromFile(String filePath) throws IOException {
        File file = ResourceUtils.getFile("classpath:error/message/" + filePath);
        return new String(Files.readAllBytes(file.toPath()));
    }
}
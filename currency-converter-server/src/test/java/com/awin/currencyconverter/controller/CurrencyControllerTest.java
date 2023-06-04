package com.awin.currencyconverter.controller;

import com.awin.currencyconverter.dto.exchangerate.ExchangeConversionRateResponse;
import com.awin.currencyconverter.dto.exchangerate.ExchangeCurrencyResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static java.math.BigDecimal.valueOf;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        var currencyResponse = new ExchangeCurrencyResponse(new HashMap<>(Map.of("EUR", "", "USD", "")));
        var currencyEntity = new ResponseEntity<>(currencyResponse, HttpStatus.OK);
        when(restTemplate.getForEntity(anyString(), eq(ExchangeCurrencyResponse.class))).thenReturn(currencyEntity);
    }

    static Stream<Arguments> successfulConversionTestData() {
        return Stream.of(
                Arguments.of("EUR", "USD", "100", valueOf(1.180), "{\"value\":118.0}"),
                Arguments.of("USD", "EUR", "100", valueOf(0.8475), "{\"value\":84.75}")
        );
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("successfulConversionTestData")
    public void should_convert_source_to_target_with_rate_provided(String source,
                                                                   String target,
                                                                   String amount,
                                                                   BigDecimal rate,
                                                                   String expectedResponseBody) {
        var rateResponse = new ExchangeConversionRateResponse(target, Map.of(target, rate));
        var rateEntity = new ResponseEntity<>(rateResponse, HttpStatus.OK);
        when(restTemplate.getForEntity(anyString(), eq(ExchangeConversionRateResponse.class))).thenReturn(rateEntity);

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
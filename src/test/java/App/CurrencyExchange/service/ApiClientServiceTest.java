package App.CurrencyExchange.service;

import App.CurrencyExchange.metrics.MetricsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ApiClientServiceTest {

    private ApiClientService apiClientService;

    @BeforeEach
    void setup() {
        apiClientService = new ApiClientService(new MetricsService());
    }

    @Test
    void testGetRatesFromFreeCurrencyApi_liveCall() {
        Map<String, Double> rates = apiClientService.fetchFromFreeCurrencyRates("EUR", List.of("USD"));
        assertTrue(rates.containsKey("USD"));
        assertTrue(rates.get("USD") > 0);
    }

    @Test
    void testGetRatesFromFrankfurter_liveCall() {
        Map<String, Double> rates = apiClientService.fetchFromFrankfurter("EUR", List.of("USD"));
        assertTrue(rates.containsKey("USD"));
        assertTrue(rates.get("USD") > 0);
    }
}

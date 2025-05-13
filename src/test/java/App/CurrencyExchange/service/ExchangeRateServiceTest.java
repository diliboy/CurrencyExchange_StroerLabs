package App.CurrencyExchange.service;

import App.CurrencyExchange.model.ExchangeRateResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExchangeRateServiceTest {

    @Mock
    private ApiClientService apiClientService;

    @InjectMocks
    private ExchangeRateService service;

    @Test
    void testGetExchangeRates_successfulAverage() {
        Map<String, Double> freeRates = Map.of("USD", 1.1, "NZD", 1.6);
        Map<String, Double> frankRates = Map.of("USD", 1.0, "NZD", 1.4);

        when(apiClientService.fetchFromFreeCurrencyRates("EUR", List.of("USD", "NZD"))).thenReturn(freeRates);
        when(apiClientService.fetchFromFrankfurter("EUR", List.of("USD", "NZD"))).thenReturn(frankRates);

        ExchangeRateResponse response = service.getRates("EUR", List.of("USD", "NZD"));

        assertEquals("EUR", response.getBase());
        assertEquals(1.05, response.getRates().get("USD"));
        assertEquals(1.5, response.getRates().get("NZD"));
    }

    @Test
    void testGetExchangeRates_usesCache() {
        List<String> symbols = List.of("USD");

        Map<String, Double> freeRates = Map.of("USD", 1.1);
        Map<String, Double> frankRates = Map.of("USD", 1.3);

        when(apiClientService.fetchFromFreeCurrencyRates("EUR", symbols)).thenReturn(freeRates);
        when(apiClientService.fetchFromFrankfurter("EUR", symbols)).thenReturn(frankRates);

        ExchangeRateResponse firstCall = service.getRates("EUR", symbols);
        ExchangeRateResponse secondCall = service.getRates("EUR", symbols);

        verify(apiClientService, times(1)).fetchFromFreeCurrencyRates("EUR", symbols);
        verify(apiClientService, times(1)).fetchFromFrankfurter("EUR", symbols);

        assertEquals(firstCall.getRates(), secondCall.getRates());
    }
}

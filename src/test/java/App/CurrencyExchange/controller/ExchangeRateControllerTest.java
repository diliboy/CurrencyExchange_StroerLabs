package App.CurrencyExchange.controller;

import App.CurrencyExchange.model.ExchangeRateResponse;
import App.CurrencyExchange.service.ExchangeRateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class ExchangeRateControllerTest {

    @Mock
    private ExchangeRateService service;

    @InjectMocks
    private ExchangeRateController controller;

    @Test
    void testGetExchangeRates_validRequest() {
        ExchangeRateResponse responseMock = new ExchangeRateResponse("EUR", Map.of("USD", 1.1, "NZD", 1.6));
        when(service.getRates("EUR", List.of("USD", "NZD"))).thenReturn(responseMock);

        ResponseEntity<ExchangeRateResponse> response = controller.getRates("EUR", List.of("USD", "NZD"));

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("EUR", response.getBody().getBase());
        assertEquals(1.1, response.getBody().getRates().get("USD"));
        verify(service).getRates("EUR", List.of("USD", "NZD"));
    }

    @Test
    void testGetExchangeRates_invalidSymbols() {
        ResponseEntity<ExchangeRateResponse> response = controller.getRates("EUR", List.of(""));
        assertEquals(400, response.getStatusCodeValue());
    }
}

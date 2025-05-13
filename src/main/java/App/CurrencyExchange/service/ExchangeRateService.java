package App.CurrencyExchange.service;

import App.CurrencyExchange.metrics.MetricsService;
import App.CurrencyExchange.model.ExchangeRateResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExchangeRateService {
    private final ApiClientService apiClientService;
    private final MetricsService metricsService;

    public ExchangeRateService(ApiClientService apiClientService, MetricsService metricsService) {
        this.apiClientService = apiClientService;
        this.metricsService = metricsService;
    }

    @Cacheable(value = "exchangeRates", key = "#base + '_' + #symbols")
    public ExchangeRateResponse getRates(String base, List<String> symbols) {
        metricsService.incrementQuery();

        Map<String, Double> rates1 = apiClientService.fetchFromFreeCurrencyRates(base, symbols);
        Map<String, Double> rates2 = apiClientService.fetchFromFrankfurter(base, symbols);

        Map<String, Double> averaged = new HashMap<>();
        for (String sym : symbols) {
            double rate1 = rates1.getOrDefault(sym, 0.0);
            double rate2 = rates2.getOrDefault(sym, 0.0);
            double avg = (rate1 + rate2) / ((rate1 > 0 && rate2 > 0) ? 2 : 1);
            averaged.put(sym, avg);
        }

        return new ExchangeRateResponse(base, averaged);
    }
}

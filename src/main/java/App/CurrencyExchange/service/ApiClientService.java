package App.CurrencyExchange.service;

import App.CurrencyExchange.metrics.MetricsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ApiClientService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final MetricsService metrics;

    public ApiClientService(MetricsService metrics) {
        this.metrics = metrics;
    }

    @Value("${currency-rate-freeCurrencyRates-api}")
    private String currencyRateFreeCurrencyRatesApiUrl;
    @Value("${currency-rate-frankfurter-api}")
    private String currencyRateFrankfurterApiUrl;


    public Map<String, Double> fetchFromFreeCurrencyRates(String base, List<String> symbols) {
        metrics.incrementApiRequest("freeCurrencyRates");
        String url = currencyRateFreeCurrencyRatesApiUrl + base;
        Map<String, Double> result = new HashMap<>();
        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            Map<String, Double> rates = (Map<String, Double>) response.get("rates");
            for (String sym : symbols) {
                result.put(sym, rates.get(sym));
            }
            metrics.incrementApiResponse("freeCurrencyRates");
        } catch (Exception e) {
            log.error("Error in fetching currency rates from FreeCurrencyRates Api : "+ e.getMessage());
        }
        return result;
    }

    public Map<String, Double> fetchFromFrankfurter(String base, List<String> symbols) {
        metrics.incrementApiRequest("frankfurter");
        String url = currencyRateFrankfurterApiUrl + base + "&to=" + String.join(",", symbols);
        Map<String, Double> result = new HashMap<>();
        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            Map<String, Double> rates = (Map<String, Double>) response.get("rates");
            result.putAll(rates);
            metrics.incrementApiResponse("frankfurter");
        } catch (Exception e) {
            log.error("Error in fetching currency rates from Frankfurter Api : "+ e.getMessage());
        }
        return result;
    }
}
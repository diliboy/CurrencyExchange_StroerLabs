package App.CurrencyExchange.controller;

import App.CurrencyExchange.metrics.MetricsService;
import App.CurrencyExchange.model.ExchangeRateResponse;
import App.CurrencyExchange.service.ExchangeRateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/exchangeRates")
public class ExchangeRateController {

    private final ExchangeRateService service;
    private final MetricsService metrics;

    public ExchangeRateController(ExchangeRateService service, MetricsService metrics) {
        this.service = service;
        this.metrics = metrics;
    }

    @GetMapping("/{base}")
    public ResponseEntity<ExchangeRateResponse> getRates(@PathVariable String base,
                                                         @RequestParam List<String> symbols) {
        return ResponseEntity.ok(service.getRates(base.toUpperCase(), symbols));
    }

    @GetMapping("/metrics")
    public ResponseEntity<Map<String, Object>> getMetrics() {
        return ResponseEntity.ok(metrics.getMetrics());
    }
}

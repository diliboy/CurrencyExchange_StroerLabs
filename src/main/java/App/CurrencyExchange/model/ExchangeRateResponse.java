package App.CurrencyExchange.model;

import lombok.AllArgsConstructor;
import lombok.*;

import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ExchangeRateResponse {
    private String base;
    private Map<String, Double> rates;
}

package App.CurrencyExchange.metrics;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class MetricsService {
    private AtomicInteger totalQueries = new AtomicInteger();
    private final Map<String, ApiMetrics> apiMetricsMap = new ConcurrentHashMap<>();

    public void incrementQuery() {
        totalQueries.incrementAndGet();
    }

    public void incrementApiRequest(String apiName) {
        apiMetricsMap.computeIfAbsent(apiName, k -> new ApiMetrics()).incrementRequest();
    }

    public void incrementApiResponse(String apiName) {
        apiMetricsMap.computeIfAbsent(apiName, k -> new ApiMetrics()).incrementResponse();
    }

    public Map<String, Object> getMetrics() {
        Map<String, Object> result = new HashMap<>();
        result.put("totalQueries", totalQueries.get());
        List<Map<String, Object>> apis = apiMetricsMap.entrySet().stream().map(entry -> {
            Map<String, Object> map = new HashMap<>();
            map.put("name", entry.getKey());
            map.put("metrics", entry.getValue());
            return map;
        }).collect(Collectors.toList());
        result.put("apis", apis);
        return result;
    }

    static class ApiMetrics {
        private AtomicInteger totalRequests = new AtomicInteger();
        private AtomicInteger totalResponses = new AtomicInteger();

        public void incrementRequest() {
            totalRequests.incrementAndGet();
        }

        public void incrementResponse() {
            totalResponses.incrementAndGet();
        }

        public int getTotalRequests() {
            return totalRequests.get();
        }

        public int getTotalResponses() {
            return totalResponses.get();
        }
    }
}

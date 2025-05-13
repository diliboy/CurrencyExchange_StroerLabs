# Currency Exchange App

## Overview

This Java Spring Boot application provides a RESTful endpoint to fetch and return currency exchange rates from two public APIs:

- [Free Currency Rates API](https://github.com/fawazahmed0/exchange-api)
- [Frankfurter API](https://www.frankfurter.app/docs/)

It averages the rates from both sources and returns them in a structured JSON format. Results are cached for repeated requests, and API usage metrics are exposed via a `/metrics` endpoint.

---

## Features

- Fetches exchange rates from two sources and averages them.
- Caches responses for identical requests to improve performance.
- Provides REST endpoint:
  - `/exchangeRates/{base}?symbols={SYM1,SYM2,...}`
- Provides metrics endpoint:
  - `/metrics`.
- Unit-tested with mocked API responses.

---

## Technologies Used

- Java 17
- Spring Boot 3.4.5
- Spring Web
- Spring Cache Abstraction
- JUnit 5 + Mockito for testing
- Maven (for build)

---

## API Usage
- Example Request:
- GET /exchangeRates/EUR?symbols=USD,NZD

## Metrics Endpoint
- Example Request
- GET /metrics

---

## Testing
- Unit tests are included for the following components:

  -ExchangeRateService – core logic including API querying, averaging, and caching
  -APIClientService
  -ExchangeRateController – REST layer input/output

---

## Design Rationale
- API Abstraction Layer: Each API client is separated via its own service class to encapsulate their unique formats.
- Rate Averaging: Rates are averaged only if both APIs return valid data. Missing rates are skipped and logged if and API is not returned any rate.
- Caching: Cache stores query results keyed by base:sortedSymbols, avoiding redundant API calls.
- Metrics: A custom service tracks API usage stats for observability. (implemented with the help of web)
- Configuration Management: API base URLs and other properties are stored in the application.properties file, keeping the codebase flexible.

---

## Possible Improvements
- Add retry logic and exponential backoff for failed API calls.
- Log all the details relevant to APIs
- Add Prometheus-style metrics
- Exception handling through custom exceptions

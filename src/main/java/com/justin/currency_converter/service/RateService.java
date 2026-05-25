package com.justin.currency_converter.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.justin.currency_converter.model.Rate;
import com.justin.currency_converter.repository.RateRepository;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class RateService {

    @Autowired
    RateRepository rateRepository;

    public RateService() {
    }

    public Rate getRateByCode(String code) throws Exception {
        Rate rate = rateRepository.findByCode(code);
        if (isExpired(rate.getDateUpdated())) {
            rate = updatePrice(rate);
            rateRepository.save(rate);
        }
        return rate;
    }

    private boolean isExpired(LocalDateTime time) {
        if (time == null)
            return true;

        LocalDateTime now = LocalDateTime.now();

        Long hoursBetween = ChronoUnit.HOURS.between(time, now);

        if (hoursBetween >= 24)
            return true;
        else
            return false;
    }

    private Rate updatePrice(Rate rate) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://data.fixer.io/api/latest?symbols="
                        + rate.getCode() + "&access_key=65810484e51394abac4b269df880c130"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.body());

        JsonNode rates = root.get("rates");
        double targetRate = rates.get(rate.getCode()).asDouble();
        
        JsonNode date = root.get("date");
        String targetDate = date.asString();
        LocalDateTime timeUpdated = LocalDate.parse(targetDate).atTime(LocalTime.now());

        rate.setPrice(BigDecimal.valueOf(targetRate));
        rate.setDateUpdated(timeUpdated);

        System.out.println("Made a request for "+rate.getCode());

        return rate;
    }
}

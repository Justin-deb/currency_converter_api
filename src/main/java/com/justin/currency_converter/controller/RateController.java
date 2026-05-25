package com.justin.currency_converter.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.justin.currency_converter.model.Rate;
import com.justin.currency_converter.service.RateService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/rates")
public class RateController {
    @Autowired
    RateService rateService;

    public RateController() {
    }
    
    @GetMapping("/{code}")
    public ResponseEntity<?> getRateByCode(@PathVariable String code) {
        Rate rate;
        Rate rateDollars;
        Rate[] rates = new Rate[2];
        try {
            rate = rateService.getRateByCode(code);
            rateDollars = rateService.getRateByCode("USD");
            rates[0] = rate;
            rates[1] = rateDollars;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.ok(rates);
    }
    
}

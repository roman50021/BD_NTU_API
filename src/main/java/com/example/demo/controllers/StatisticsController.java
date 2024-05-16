package com.example.demo.controllers;

import com.example.demo.services.StatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Long>> getStatistics() {
        Map<String, Long> statistics = statisticsService.generateStatistics();
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/filter")
    public ResponseEntity<Map<String, Long>> getFilteredStatistics(
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String searchTerm) {
        Map<String, Long> statistics = statisticsService.generateFilteredStatistics(minPrice, maxPrice, searchTerm);
        return ResponseEntity.ok(statistics);
    }
}
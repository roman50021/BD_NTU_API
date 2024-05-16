package com.example.demo.configs;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableCaching
public class StatisticsCacheConfig {
    @Bean
    @Primary
    public CacheManager cacheStatisticsManager() {
        return new ConcurrentMapCacheManager("statistics");
    }
}

package com.example.demo.configs;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@CacheConfig(cacheNames = "restaurantsCache")
public class RestaurantsCacheConfig {
    @Bean
    public CacheManager restaurantsCacheManager(){
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        ConcurrentMapCache cache = new ConcurrentMapCache("restaurantsCache");
        cacheManager.setCaches(Arrays.asList(cache));
        return cacheManager;
    }
}

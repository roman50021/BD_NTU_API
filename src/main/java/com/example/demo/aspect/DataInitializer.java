package com.example.demo.aspect;

import com.example.demo.models.*;
import com.example.demo.repositories.*;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.github.javafaker.Faker;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class DataInitializer {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantAddressRepository restaurantAddressRepository;

    @PostConstruct
    @Transactional
    public void init() {
        Faker faker = new Faker();

        for (int i = 0; i < 500; i++) {
            String name = faker.company().name();
            String description = faker.lorem().sentence();
            String phoneNumber = faker.phoneNumber().phoneNumber();
            LocalDateTime openTime = LocalDateTime.of(2024, 1, 1, 9, 0); // Установите время открытия
            LocalDateTime closeTime = LocalDateTime.of(2024, 1, 1, 21, 0); // Установите время закрытия
            String website = faker.internet().url();

            Restaurant restaurant = Restaurant.builder()
                    .name(name)
                    .description(description)
                    .phoneNumber(phoneNumber)
                    .openTime(openTime)
                    .closeTime(closeTime)
                    .website(website)
                    .build();

            restaurantRepository.save(restaurant);
        }

        List<Restaurant> restaurants = restaurantRepository.findAll();
        for (Restaurant restaurant : restaurants) {
            String city = faker.address().city();
            String street = faker.address().streetName();
            String houseNumber = faker.address().buildingNumber();

            RestaurantAddress restaurantAddress = RestaurantAddress.builder()
                    .restaurant(restaurant)
                    .city(city)
                    .street(street)
                    .houseNumber(houseNumber)
                    .build();

            restaurantAddressRepository.save(restaurantAddress);
        }
    }
}

package com.example.demo.controllers;

import com.example.demo.dto.MessageInfo;
import com.example.demo.dto.restaurant.requests.*;
import com.example.demo.dto.restaurant.responses.RestaurantWithAddressDto;
import com.example.demo.dto.user.responses.UserWithAddressDto;
import com.example.demo.models.Restaurant;
import com.example.demo.models.User;
import com.example.demo.services.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurant")
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService service;

    @PostMapping("/create")
    public ResponseEntity<MessageInfo> createRestaurant(@RequestBody CreateRestaurantDto request){
        return service.createRestaurant(request);
    }
    @GetMapping("/get")
    public ResponseEntity<RestaurantWithAddressDto> getRestaurant(@RequestBody GetRestaurantDto request){
        return service.getRestaurant(request);
    }
    @GetMapping("/all")
    public ResponseEntity<List<Restaurant>> getRestaurants() {
        return service.getRestaurants();
    }

    @PutMapping("/update")
    public ResponseEntity<MessageInfo> updateRestaurant(@RequestBody UpdateRestaurantDto request){
        return service.updateRestaurant(request);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<MessageInfo> deleteRestaurant(@RequestBody DeleteRestaurantDto request){
        return service.deleteRestaurant(request);
    }

}

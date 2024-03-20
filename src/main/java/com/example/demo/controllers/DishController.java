package com.example.demo.controllers;

import com.example.demo.dto.MessageInfo;
import com.example.demo.dto.dish.requests.CreateDishDto;
import com.example.demo.dto.dish.requests.DeleteDishDto;
import com.example.demo.dto.dish.requests.GetDishDto;
import com.example.demo.dto.dish.requests.UpdateDishDto;
import com.example.demo.dto.menu.responses.DishDto;
import com.example.demo.models.Dish;
import com.example.demo.models.Restaurant;
import com.example.demo.services.DishService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dish")
@RequiredArgsConstructor
public class DishController {
    private final DishService service;

    @PostMapping("/create")
    public ResponseEntity<MessageInfo> createRestaurant(@RequestBody CreateDishDto request){
        return service.createDish(request);
    }
    @GetMapping("/get")
    public ResponseEntity<DishDto> getMenu(@RequestBody GetDishDto request){
        return service.getDish(request);
    }
    @GetMapping("/all")
    public ResponseEntity<List<Dish>> getMenus() {
        return service.getDishes();
    }

    @PutMapping("/update")
    public ResponseEntity<MessageInfo> updateMenu(@RequestBody UpdateDishDto request){
        return service.updateDish(request);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<MessageInfo> deleteMenu(@RequestBody DeleteDishDto request){
        return service.deleteDish(request);
    }
}

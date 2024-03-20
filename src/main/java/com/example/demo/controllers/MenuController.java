package com.example.demo.controllers;

import com.example.demo.dto.MessageInfo;
import com.example.demo.dto.menu.requests.CreateMenuDto;
import com.example.demo.dto.menu.requests.DeleteMenuDto;
import com.example.demo.dto.menu.requests.GetMenuDto;
import com.example.demo.dto.menu.requests.UpdateMenuDto;
import com.example.demo.dto.menu.responses.MenuDto;
import com.example.demo.dto.menu.responses.MenuWithDishesDto;
import com.example.demo.models.Menu;
import com.example.demo.models.Restaurant;
import com.example.demo.services.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/menu")
@RequiredArgsConstructor
public class MenuController {
    private final MenuService service;

    @PostMapping("/create")
    public ResponseEntity<MessageInfo> createRestaurant(@RequestBody CreateMenuDto request){
        return service.createMenu(request);
    }
    @GetMapping("/get")
    public ResponseEntity<MenuWithDishesDto> getMenu(@RequestBody GetMenuDto request){
        return service.getMenu(request);
    }
    @GetMapping("/all")
    public ResponseEntity<List<MenuDto>> getMenus() {
        return service.getMenus();
    }

    @PutMapping("/update")
    public ResponseEntity<MessageInfo> updateMenu(@RequestBody UpdateMenuDto request){
        return service.updateMenu(request);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<MessageInfo> deleteMenu(@RequestBody DeleteMenuDto request){
        return service.deleteMenu(request);
    }

}

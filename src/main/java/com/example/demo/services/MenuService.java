package com.example.demo.services;

import com.example.demo.dto.MessageInfo;
import com.example.demo.dto.menu.requests.CreateMenuDto;
import com.example.demo.dto.menu.requests.DeleteMenuDto;
import com.example.demo.dto.menu.requests.GetMenuDto;
import com.example.demo.dto.menu.requests.UpdateMenuDto;
import com.example.demo.dto.menu.responses.DishDto;
import com.example.demo.dto.menu.responses.MenuDto;
import com.example.demo.dto.menu.responses.MenuWithDishesDto;
import com.example.demo.dto.restaurant.requests.RestaurantAddressDto;
import com.example.demo.models.Dish;
import com.example.demo.models.Menu;
import com.example.demo.models.Restaurant;
import com.example.demo.models.RestaurantAddress;
import com.example.demo.repositories.DishRepository;
import com.example.demo.repositories.MenuRepository;
import com.example.demo.repositories.RestaurantAddressRepository;
import com.example.demo.repositories.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;

    public ResponseEntity<MessageInfo> createMenu(CreateMenuDto request) {
        // Проверяем, существует ли уже меню с таким именем для данного ресторана
        Optional<Menu> existingMenu = menuRepository.findByNameAndRestaurantId(request.getName(), request.getRestaurantId());
        if (existingMenu.isPresent()) {
            // Если меню уже существует, возвращаем сообщение об ошибке
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(MessageInfo.builder()
                            .message("A menu with this name already exists for the specified restaurant")
                            .build());
        } else {
            // Создаем новое меню
            Menu menu = Menu.builder()
                    .name(request.getName())
                    .restaurant(restaurantRepository.findById(request.getRestaurantId()).orElse(null))
                    .build();

            // Сохраняем созданное меню в базе данных
            menuRepository.save(menu);

            return ResponseEntity.ok(new MessageInfo("Menu created successfully"));
        }
    }

    public ResponseEntity<MenuWithDishesDto> getMenu(GetMenuDto request) {
        // Ищем меню по его идентификатору
        Optional<Menu> menuOptional = menuRepository.findById(request.getId());
        if (menuOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Получаем список блюд для данного меню
        List<Dish> dishes = dishRepository.findByMenu(menuOptional.get());

        // Создаем DTO с информацией о меню и его блюдах
        MenuWithDishesDto menuWithDishesDto = new MenuWithDishesDto();
        menuWithDishesDto.setId(menuOptional.get().getId());
        menuWithDishesDto.setName(menuOptional.get().getName());
        menuWithDishesDto.setRestaurantId(menuOptional.get().getRestaurant().getId());

        // Добавляем блюда в DTO
        List<DishDto> dishDtos = dishes.stream()
                .map(dish -> DishDto.builder()
                        .id(dish.getId())
                        .name(dish.getName())
                        .description(dish.getDescription())
                        .price(dish.getPrice())
                        .build())
                .collect(Collectors.toList());
        menuWithDishesDto.setDishes(dishDtos);

        return ResponseEntity.ok(menuWithDishesDto);
    }

    public ResponseEntity<List<MenuDto>> getMenus() {
        List<Menu> menus = menuRepository.findAll();
        List<MenuDto> menuDtos = menus.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(menuDtos);
    }

    private MenuDto convertToDto(Menu menu) {
        return MenuDto.builder()
                .id(menu.getId())
                .restaurantName(menu.getRestaurant().getName())
                .name(menu.getName())
                .build();
    }

    public ResponseEntity<MessageInfo> updateMenu(UpdateMenuDto request) {
        // Поиск меню по его идентификатору
        Optional<Menu> menuOptional = menuRepository.findById(request.getId());
        if (menuOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageInfo("Menu not found with ID: " + request.getId()));
        }

        Menu menu = menuOptional.get();
        menu.setName(request.getName());

        // Обновление связи с рестораном (если это необходимо)
        if (request.getRestaurantId() != null) {
            Optional<Restaurant> restaurantOptional = restaurantRepository.findById(request.getRestaurantId());
            if (restaurantOptional.isPresent()) {
                menu.setRestaurant(restaurantOptional.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new MessageInfo("Restaurant not found with ID: " + request.getRestaurantId()));
            }
        }

        // Сохранение обновленного меню
        menuRepository.save(menu);

        return ResponseEntity.ok(new MessageInfo("Menu updated successfully"));
    }

    public ResponseEntity<MessageInfo> deleteMenu(DeleteMenuDto request) {
        Optional<Menu> menuOptional = menuRepository.findById(request.getId());
        if (menuOptional.isPresent()) {
            Menu menu = menuOptional.get();

            // Найти все блюда, связанные с удаляемым меню
            List<Dish> dishes = dishRepository.findByMenu(menu);

            // Удалить найденные блюда
            dishRepository.deleteAll(dishes);

            // Удалить меню
            menuRepository.delete(menu);

            return ResponseEntity.ok().body(new MessageInfo("Menu deleted successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageInfo("Menu not found with ID: " + request.getId()));
        }
    }
}

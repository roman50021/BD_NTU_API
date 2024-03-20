package com.example.demo.services;

import com.example.demo.dto.MessageInfo;
import com.example.demo.dto.dish.requests.CreateDishDto;
import com.example.demo.dto.dish.requests.DeleteDishDto;
import com.example.demo.dto.dish.requests.GetDishDto;
import com.example.demo.dto.dish.requests.UpdateDishDto;
import com.example.demo.dto.menu.responses.DishDto;
import com.example.demo.models.Dish;
import com.example.demo.models.Menu;
import com.example.demo.models.Restaurant;
import com.example.demo.models.User;
import com.example.demo.repositories.DishRepository;
import com.example.demo.repositories.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DishService {
    private final DishRepository dishRepository;
    private final MenuRepository menuRepository;
    public ResponseEntity<MessageInfo> createDish(CreateDishDto request) {
        // Поиск меню по его идентификатору
        Optional<Menu> menuOptional = menuRepository.findById(request.getMenuId());
        if (menuOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageInfo("Menu not found with ID: " + request.getMenuId()));
        }

        // Создание нового блюда
        Dish dish = Dish.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .menu(menuOptional.get()) // Установка объекта Menu
                .build();

        // Сохранение нового блюда
        dishRepository.save(dish);

        return ResponseEntity.ok(new MessageInfo("Dish created successfully"));
    }

    public ResponseEntity<DishDto> getDish(GetDishDto request) {
        // Найти блюдо по его идентификатору
        Optional<Dish> dishOptional = dishRepository.findById(request.getId());

        // Проверить, найдено ли блюдо
        if (dishOptional.isPresent()) {
            Dish dish = dishOptional.get();
            // Создать DTO для блюда с помощью Builder
            DishDto dishDto = DishDto.builder()
                    .id(dish.getId())
                    .name(dish.getName())
                    .description(dish.getDescription())
                    .price(dish.getPrice())
                    .build();
            // Вернуть блюдо в ответе
            return new ResponseEntity<>(dishDto, HttpStatus.OK);
        } else {
            // Если блюдо не найдено, вернуть сообщение об ошибке
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<List<Dish>> getDishes() {
        try {
            List<Dish> dishes = dishRepository.findAll();
            return new ResponseEntity<>(dishes, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    public ResponseEntity<MessageInfo> updateDish(UpdateDishDto request) {
        // Поиск блюда по его идентификатору
        Dish dish = dishRepository.findById(request.getDishId())
                .orElse(null);

        // Проверяем, найдено ли блюдо
        if (dish == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageInfo("Dish not found with ID: " + request.getDishId()));
        }

        // Обновление данных блюда с использованием Builder
        Dish updatedDish = Dish.builder()
                .id(dish.getId())
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .menu(dish.getMenu()) // Если поле меню не меняется, можно скопировать его из исходного блюда
                .build();

        // Сохранение обновленного блюда
        dishRepository.save(updatedDish);

        return ResponseEntity.ok(new MessageInfo("Dish updated successfully"));
    }

    public ResponseEntity<MessageInfo> deleteDish(DeleteDishDto request) {
        if (dishRepository.existsById(request.getId())) {
            dishRepository.deleteById(request.getId());
            return ResponseEntity.ok()
                    .body(MessageInfo.builder()
                            .message("Dish deleted successfully.")
                            .build());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(MessageInfo.builder()
                            .message("Dish not found.")
                            .build());
        }
    }
}

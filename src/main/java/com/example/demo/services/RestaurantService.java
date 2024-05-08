package com.example.demo.services;

import com.example.demo.dto.MessageInfo;
import com.example.demo.dto.restaurant.requests.*;
import com.example.demo.dto.restaurant.responses.RestaurantWithAddressDto;
import com.example.demo.dto.user.responses.UserWithAddressDto;
import com.example.demo.models.*;
import com.example.demo.repositories.RestaurantAddressRepository;
import com.example.demo.repositories.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantAddressRepository restaurantAddressRepository;
    public ResponseEntity<MessageInfo> createRestaurant(CreateRestaurantDto request) {
        try {
            // Проверяем существует ли ресторан с таким именем
            Optional<Restaurant> existingRestaurant = restaurantRepository.findByName(request.getName());
            if (existingRestaurant.isPresent()) {
                // Если ресторан с таким именем уже существует, возвращаем сообщение об ошибке
                return ResponseEntity.status(HttpStatus.CONFLICT).body(MessageInfo.builder().message("A restaurant with this name already exists").build());
            }

            // Создание нового ресторана
            Restaurant newRestaurant = Restaurant.builder()
                    .name(request.getName())
                    .description(request.getDescription())
                    .phoneNumber(request.getPhoneNumber())
                    .openTime(request.getOpenTime())
                    .closeTime(request.getCloseTime())
                    .website(request.getWebsite())
                    .build();

            // Сохранение ресторана в базе данных
            restaurantRepository.save(newRestaurant);

            // Создание и сохранение адресов ресторана
            List<RestaurantAddress> restaurantAddresses = new ArrayList<>();
            for (RestaurantAddressDto addressDto : request.getAddresses()) {
                RestaurantAddress restaurantAddress = RestaurantAddress.builder()
                        .restaurant(newRestaurant)
                        .city(addressDto.getCity())
                        .street(addressDto.getStreet())
                        .houseNumber(addressDto.getHouseNumber())
                        .build();
                restaurantAddresses.add(restaurantAddress);
            }
            restaurantAddressRepository.saveAll(restaurantAddresses);

            // Формирование сообщения об успешном создании ресторана
            MessageInfo messageInfo = MessageInfo.builder()
                    .message("Restaurant successfully created")
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(messageInfo);
        } catch (Exception e) {
            // Обработка исключений
            String errorMessage = "Error when creating restaurant: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(MessageInfo.builder().message(errorMessage).build());
        }
    }

    public ResponseEntity<RestaurantWithAddressDto> getRestaurant(GetRestaurantDto request) {
        // Ищем ресторан по имени
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findByName(request.getName());

        // Проверяем, найден ли ресторан
        if (optionalRestaurant.isPresent()) {
            Restaurant restaurant = optionalRestaurant.get();

            // Создаем DTO с информацией о ресторане и его адресах
            RestaurantWithAddressDto restaurantWithAddressDto = new RestaurantWithAddressDto();
            restaurantWithAddressDto.setId(restaurant.getId());
            restaurantWithAddressDto.setName(restaurant.getName());
            restaurantWithAddressDto.setDescription(restaurant.getDescription());
            restaurantWithAddressDto.setPhoneNumber(restaurant.getPhoneNumber());
            restaurantWithAddressDto.setOpenTime(restaurant.getOpenTime());
            restaurantWithAddressDto.setCloseTime(restaurant.getCloseTime());
            restaurantWithAddressDto.setWebsite(restaurant.getWebsite());

            // Добавляем адреса ресторана в DTO
            List<String> addresses = restaurant.getAddresses().stream()
                    .map(address -> address.getCity() + ", " + address.getStreet() + ", " + address.getHouseNumber())
                    .collect(Collectors.toList());
            restaurantWithAddressDto.setAddresses(addresses);

            return ResponseEntity.ok().body(restaurantWithAddressDto);
        } else {
            String errorMessage = "Restaurant not found";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(RestaurantWithAddressDto.builder().message(errorMessage).build());
        }
    }
    @Cacheable("restaurantsCache")
    public ResponseEntity<List<Restaurant>> getRestaurants() {
        try {
            List<Restaurant> restaurants = restaurantRepository.findAll();
            CompletableFuture<Void> completableFuture = CompletableFuture.allOf(
                    restaurants.stream().map(restaurant -> CompletableFuture.runAsync(() -> {
                        restaurant.getMenus().size(); // Загрузка связанных меню
                    })).toArray(CompletableFuture[]::new)
            );
            completableFuture.get(); // Ждем завершения всех CompletableFuture
            return ResponseEntity.ok(restaurants);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Transactional
    public ResponseEntity<MessageInfo> updateRestaurant(UpdateRestaurantDto request) {
        // Находим ресторан по старому имени
        Optional<Restaurant> restaurantOptional = restaurantRepository.findByName(request.getName());
        if (restaurantOptional.isPresent()) {
            Restaurant restaurant = restaurantOptional.get();

            // Проверяем, существует ли ресторан с новым именем
            Optional<Restaurant> existingRestaurant = restaurantRepository.findByName(request.getNewName());
            if (existingRestaurant.isPresent() && !existingRestaurant.get().getId().equals(restaurant.getId())) {
                // Если ресторан с новым именем уже существует и это не текущий ресторан, возвращаем сообщение об ошибке
                return ResponseEntity.status(HttpStatus.CONFLICT).body(MessageInfo.builder().message("A restaurant with this new name already exists").build());
            } else {
                // Обновляем информацию о ресторане
                restaurant.setName(request.getNewName());
                restaurant.setDescription(request.getDescription());
                restaurant.setPhoneNumber(request.getPhoneNumber());
                restaurant.setOpenTime(request.getOpenTime());
                restaurant.setCloseTime(request.getCloseTime());
                restaurant.setWebsite(request.getWebsite());

                // Удаляем все существующие адреса ресторана
                restaurantAddressRepository.deleteByRestaurant(restaurant);

                // Добавляем новые адреса
                List<RestaurantAddressDto> addressDtos = request.getAddresses();
                for (RestaurantAddressDto addressDto : addressDtos) {
                    RestaurantAddress address = new RestaurantAddress();
                    address.setCity(addressDto.getCity());
                    address.setStreet(addressDto.getStreet());
                    address.setHouseNumber(addressDto.getHouseNumber());
                    address.setRestaurant(restaurant);
                    restaurant.getAddresses().add(address);
                }

                // Сохраняем обновленный ресторан
                restaurantRepository.save(restaurant);

                return ResponseEntity.ok(new MessageInfo("Restaurant updated successfully"));
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Transactional
    public ResponseEntity<MessageInfo> deleteRestaurant(DeleteRestaurantDto request) {
        // Находим ресторан по имени
        Optional<Restaurant> restaurantOptional = restaurantRepository.findByName(request.getName());
        if (restaurantOptional.isPresent()) {
            // Получаем найденный ресторан
            Restaurant restaurant = restaurantOptional.get();

            // Удаляем все адреса ресторана
            restaurantAddressRepository.deleteByRestaurant(restaurant);

            // Удаляем ресторан
            restaurantRepository.delete(restaurant);

            return ResponseEntity.ok(new MessageInfo("Restaurant and its addresses deleted successfully"));
        } else {
            // Если ресторан с заданным именем не найден, возвращаем сообщение об ошибке
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageInfo("Restaurant not found"));
        }
    }

}

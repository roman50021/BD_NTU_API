package com.example.demo.services;

import com.example.demo.dto.tasks.DishDTO;
import com.example.demo.dto.tasks.UserDTO;
import com.example.demo.models.Dish;
import com.example.demo.models.User;
import com.example.demo.repositories.DishRepository;
import com.example.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final UserRepository userRepository;
    private final DishRepository dishRepository;

    public UserDTO findUserByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        return userOptional.map(this::convertToDTO).orElse(null);
    }

    public List<UserDTO> findUsersByFirstnameAndLastname(String firstname, String lastname) {
        List<User> users = userRepository.findByFirstnameAndLastname(firstname, lastname);
        return users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .build();
    }

    public List<DishDTO> findDishesByMenuId(Long menuId) {
        List<Dish> dishes = dishRepository.findByMenuId(menuId);
        return dishes.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private DishDTO convertToDTO(Dish dish) {
        return DishDTO.builder()
                .name(dish.getName())
                .description(dish.getDescription())
                .price(dish.getPrice())
                .build();
    }

}

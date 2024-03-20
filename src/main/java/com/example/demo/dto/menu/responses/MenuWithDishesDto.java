package com.example.demo.dto.menu.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuWithDishesDto {
    private Integer id;
    private String name;
    private Integer restaurantId;
    private List<DishDto> dishes;
}

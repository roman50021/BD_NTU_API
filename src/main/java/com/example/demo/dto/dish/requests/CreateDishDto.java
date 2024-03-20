package com.example.demo.dto.dish.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateDishDto {
    private String name; // Название блюда
    private String description; // Описание блюда
    private double price; // Цена блюда
    private Integer menuId; // Идентификатор меню, к которому привязано блюдо
}

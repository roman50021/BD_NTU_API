package com.example.demo.dto.tasks;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishDTO {
    private String name; // Название блюда
    private String description; // Описание блюда
    private double price; // Цена блюда
    private Integer menuId; // Идентификатор меню, к которому привязано блюдо
}

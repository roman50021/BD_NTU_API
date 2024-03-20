package com.example.demo.dto.menu.requests;

import com.example.demo.models.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateMenuDto {
    private String name; // Название меню
    private Integer restaurantId; // Идентификатор ресторана, к которому привязано меню
}

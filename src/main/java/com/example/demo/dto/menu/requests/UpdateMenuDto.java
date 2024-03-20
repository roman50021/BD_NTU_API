package com.example.demo.dto.menu.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMenuDto {
    private Integer id; // Идентификатор меню, которое нужно обновить
    private String name; // Новое название меню
    private Integer restaurantId; // Идентификатор нового ресторана (если требуется изменить связь с рестораном)

}

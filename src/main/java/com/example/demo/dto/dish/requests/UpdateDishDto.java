package com.example.demo.dto.dish.requests;

import com.example.demo.models.Menu;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDishDto {
    private Long dishId;
    private String name;
    private String description;
    private double price;

}

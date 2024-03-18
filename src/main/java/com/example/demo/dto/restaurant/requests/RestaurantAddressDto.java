package com.example.demo.dto.restaurant.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantAddressDto {
    private String city;
    private String street;
    private String houseNumber;
}

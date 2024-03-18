package com.example.demo.dto.restaurant.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateRestaurantDto {
    private String name;
    private String description;
    private String phoneNumber;
    private LocalDateTime openTime;
    private LocalDateTime closeTime;
    private String website;
    private List<RestaurantAddressDto> addresses;
}
